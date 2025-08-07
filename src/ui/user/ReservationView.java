package ui.user;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import exception.ChoiceOutOfBoundException;
import manager.MenuManger;
import manager.ShowManager;
import service.MovieService;
import service.ReservationService;
import ui.BaseView;
import vo.MovieVO;
import vo.ReservationVO;
import vo.ShowSeatVO;
import vo.ShowVO;
import vo.UserVO;

public class ReservationView extends BaseView implements MenuManger {

	private UserVO currentUser;
	private MovieService movieService;
	private ShowManager showManager;
	private ReservationService reservationService;

	public ReservationView(UserVO currentUser) {
		this.currentUser = currentUser;
		this.movieService = new MovieService();
		this.showManager = ShowManager.getInstance();
		this.reservationService = ReservationService.getInstance();
	}

	@Override
	public void execute() {
		System.out.println("==================================================");
		System.out.println("\t\t|| 영화 예매를 시작합니다. ||");
		System.out.println("==================================================");
		try {
			MovieVO movie = selectMovie();
			if (movie == null) {
				System.out.println("\n영화 선택이 취소되었습니다. 이전 메뉴로 돌아갑니다.");
				return;
			}

			ShowVO show = selectShow(movie);
			if (show == null) {
				System.out.println("\n상영 시간 선택이 취소되었습니다. 이전 메뉴로 돌아갑니다.");
				return;
			}

			List<ShowSeatVO> seats = selectSeats(show);
			if (seats == null || seats.isEmpty()) {
				System.out.println("\n좌석 선택이 취소되었습니다. 이전 메뉴로 돌아갑니다.");
				return;
			}

			confirmReservation(show, seats);

		} catch (Exception e) {
			System.out.println("\n오류가 발생하여 예매를 중단합니다.");
			e.printStackTrace();
		}
	}

	private MovieVO selectMovie() {
		List<MovieVO> movies = movieService.getAllMovies();
		if (movies == null || movies.isEmpty()) {
			System.out.println("현재 예매 가능한 영화가 없습니다.");
			return null;
		}

		while (true) {
			System.out.println("\n===== 예매할 영화 선택 =====");
			for (int i = 0; i < movies.size(); i++) {
				System.out.printf("%d. %s\n", i + 1, movies.get(i).getTitle());
			}
			System.out.println("0. 예매 취소");
			System.out.println("==========================");

			try {
				int choice = scanInt("\t번호를 선택하세요: ");
				if (choice == 0) {
					return null;
				}
				if (choice > 0 && choice <= movies.size()) {
					return movies.get(choice - 1);
				}
				System.out.println("오류: 잘못된 번호입니다. 다시 입력해주세요.");
			} catch (NumberFormatException e) {
				System.out.println("오류: 숫자만 입력해주세요.");
			}
		}
	}

	private ShowVO selectShow(MovieVO movie) {
		List<ShowVO> shows = movie.getShowList();
		if (shows == null || shows.isEmpty()) {
			System.out.printf("'%s'의 상영 정보가 없습니다.\n", movie.getTitle());
			return null;
		}

		while (true) {
			System.out.printf("\n===== '%s' 상영 시간 선택 =====\n", movie.getTitle());
			for (int i = 0; i < shows.size(); i++) {
				ShowVO show = shows.get(i);
				int remainingSeats = showManager.getRemainingSeats(show.getShowId());
				System.out.printf("%d. %s (남은 좌석: %d)\n", i + 1, show.getShowTime(), remainingSeats);
			}
			System.out.println("0. 이전 단계로");
			System.out.println("===============================");

			try {
				int choice = scanInt("\t번호를 선택하세요: ");
				if (choice == 0) {
					return null;
				}
				if (choice > 0 && choice <= shows.size()) {
					return shows.get(choice - 1);
				}
				System.out.println("오류: 잘못된 번호입니다. 다시 입력해주세요.");
			} catch (NumberFormatException e) {
				System.out.println("오류: 숫자만 입력해주세요.");
			}
		}
	}

	private List<ShowSeatVO> selectSeats(ShowVO show) {
		System.out.printf("\n===== '%s' 좌석 선택 =====\n", movieService.getMovieById(show.getMovieId()).getTitle());

		List<ShowSeatVO> selectedSeats = new ArrayList<>();
		while (true) {
			try {
				boolean[][] seatMap = showManager.getSeatMap(show.getShowId());
				displaySeatMap(seatMap, selectedSeats);

				if (!selectedSeats.isEmpty()) {
					System.out.println("현재 선택한 좌석: "
							+ selectedSeats.stream().map(ShowSeatVO::getSeatCode).collect(Collectors.joining(", ")));
				}

				String seatCode = scanStr("좌석을 선택하세요 (예: A3). 좌석을 하나씩 선택하시면 추가 구매가 됩니다  완료하려면 '완료' 입력: ").toUpperCase();

				if ("완료".equalsIgnoreCase(seatCode)) {
					if (selectedSeats.isEmpty()) {
						System.out.println("좌석이 선택되지 않았습니다. 예매를 취소합니다.");
						return null;
					}
					System.out.println("\n최종 선택 좌석: "
							+ selectedSeats.stream().map(ShowSeatVO::getSeatCode).collect(Collectors.joining(", ")));
					String confirm = scanStr("이 좌석으로 예매를 계속하시겠습니까? (Y/N): ");
					if (confirm.equalsIgnoreCase("Y")) {
						return selectedSeats;
					} else {
						System.out.println("좌석 선택을 다시 진행합니다.");
						selectedSeats.clear();
						continue;
					}
				}

				if (!showManager.isValidSeatCode(seatCode)) {
					System.out.println("오류: 좌석 형식이 올바르지 않습니다. (예: A1, B5)");
					continue;
				}

				boolean alreadySelected = false;
				for (ShowSeatVO s : selectedSeats) {
					if (s.getSeatCode().equalsIgnoreCase(seatCode)) {
						alreadySelected = true;
						break;
					}
				}
				if (alreadySelected) {
					System.out.println("이미 선택한 좌석입니다. 다른 좌석을 선택해주세요.");
					continue;
				}

				if (!showManager.isSeatAvailable(show.getShowId(), seatCode)) {
					System.out.println("이미 예매된 좌석입니다. 다른 좌석을 선택해주세요.");
					continue;
				}

				ShowSeatVO selectedSeat = showManager.getSeatByCode(show.getShowId(), seatCode);
				selectedSeats.add(selectedSeat);
				System.out.printf("'%s' 좌석이 추가되었습니다.\n", seatCode);

			} catch (Exception e) {
				System.out.println("오류가 발생했습니다: " + e.getMessage());
				return null;
			}
		}
	}

	private void displaySeatMap(boolean[][] seatMap, List<ShowSeatVO> selectedSeats) {
		System.out.println("\n--------------------- SCREEN ---------------------");
		System.out.print("   ");
		for (int col = 0; col < seatMap[0].length; col++) {
			System.out.printf(" %d ", col + 1);
		}
		System.out.println();

		for (int row = 0; row < seatMap.length; row++) {
			System.out.printf(" %c ", (char) ('A' + row));
			for (int col = 0; col < seatMap[row].length; col++) {
				String seatCode = "" + (char) ('A' + row) + (col + 1);
				boolean isSelectedByMe = false;
				for (ShowSeatVO s : selectedSeats) {
					if (s.getSeatCode().equalsIgnoreCase(seatCode)) {
						isSelectedByMe = true;
						break;
					}
				}

				if (isSelectedByMe) {
					System.out.print("[*]");
				} else if (seatMap[row][col]) {
					System.out.print("[X]");
				} else {
					System.out.print("[ ]");
				}
			}
			System.out.println();
		}
		System.out.println("--------------------------------------------------");
		System.out.println("[*] : 내가 선택한 좌석, [X] : 예매 완료 좌석");
	}

	private void confirmReservation(ShowVO show, List<ShowSeatVO> seats) {
		System.out.println("\n===== 최종 예매 정보 확인 =====");
		System.out.println("영화: " + movieService.getMovieById(show.getMovieId()).getTitle());
		System.out.println("상영 시간: " + show.getShowTime());
		System.out.println("선택 좌석: " + seats.stream().map(ShowSeatVO::getSeatCode).collect(Collectors.joining(", ")));
		System.out.println("총 결제 금액: " + (seats.size() * 10000) + "원");
		System.out.println("==============================");

		String confirm = scanStr("결제를 진행하시겠습니까? (Y/N): ");
		if (!"Y".equalsIgnoreCase(confirm)) {
			System.out.println("\n예매가 취소되었습니다. 이전 메뉴로 돌아갑니다.");
			return;
		}

		int successCount = 0;
		List<String> failedSeats = new ArrayList<>();

		for (ShowSeatVO seat : seats) {
			ReservationVO reservation = reservationService.makeReservation(currentUser.getUserNo(), show.getShowId(),
					seat.getShowSeatId());
			if (reservation != null) {
				successCount++;
			} else {
				failedSeats.add(seat.getSeatCode());
			}
		}

		if (successCount == seats.size()) {
			System.out.println("\n==================================================");
			System.out.println("\t\t|| 예매가 성공적으로 완료되었습니다! ||");
			System.out.println("==================================================");
			System.out.println("예매 번호는 'My 예매 내역'에서 확인하실 수 있습니다.");
		} else {
			System.out.println("\n일부 좌석 예매에 실패했습니다.");
			System.out.println("성공: " + successCount + "건");
			System.out.println("실패: " + failedSeats.size() + "건 (" + String.join(", ", failedSeats) + ")");
			System.out.println("실패한 좌석은 이미 다른 사용자가 예매했을 수 있습니다.");
		}
		System.out.println("\n잠시 후 메인 메뉴로 돌아갑니다.");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
	}
}