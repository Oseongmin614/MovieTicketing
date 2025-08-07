package ui.admin;

import manager.ShowManager;
import service.MovieService;
import ui.BaseView;
import vo.MovieVO;
import vo.ShowVO;

import java.util.List;

public class ShowManageView extends BaseView {

	private final MovieService movieService;
	private final ShowManager showManager;

	public ShowManageView() {
		this.movieService = new MovieService();
		this.showManager = ShowManager.getInstance();
	}

	@Override
	public void execute() {
		while (true) {
			try {
				MovieVO selectedMovie = selectMovie();
				if (selectedMovie == null) {
					return;
				}
				manageShowsForMovie(selectedMovie);
			} catch (Exception e) {
				System.out.println("오류: " + e.getMessage());
			}
		}
	}

	private MovieVO selectMovie() throws Exception {
		System.out.println("\n===== 영화 목록 =====");
		List<MovieVO> movies = movieService.getAllMovies();
		if (movies.isEmpty()) {
			System.out.println("등록된 영화가 없습니다. 영화를 먼저 등록해주세요.");
			return null;
		}

		movies.forEach(m -> System.out.printf("%d. %s\n", m.getMovieId(), m.getTitle()));
		System.out.println("===================");
		int movieId = scanInt("상영 일정을 관리할 영화 ID를 입력하세요 (0: 돌아가기) >> ");

		if (movieId == 0) {
			return null;
		}

		MovieVO movie = movieService.getMovieById(movieId);
		if (movie == null) {
			System.out.println("잘못된 영화 ID입니다.");
			return selectMovie();
		}
		return movie;
	}

	private void manageShowsForMovie(MovieVO movie) {
		while (true) {
			try {
				System.out.printf("\n===== [%s] 상영 일정 =====\n", movie.getTitle());
				List<ShowVO> shows = showManager.getShowsByMovie(movie.getMovieId());
				if (shows.isEmpty()) {
					System.out.println("등록된 상영 일정이 없습니다.");
				} else {
					shows.forEach(s -> System.out.printf("ID: %-3d | 날짜: %-12s | 시간: %-7s\n", s.getShowId(),
							s.getShowDate(), s.getShowClock()));
				}
				System.out.println("========================================");
				System.out.println("1. 상영 일정 추가");
				System.out.println("2. 상영 일정 수정");
				System.out.println("3. 상영 일정 삭제");
				System.out.println("4. 다른 영화 선택");

				int choice = scanInt("메뉴 선택 >> ");

				switch (choice) {
				case 1:
					addShow(movie.getMovieId());
					break;
				case 2:
					updateShow();
					break;
				case 3:
					deleteShow();
					break;
				case 4:
					return;
				default:
					System.out.println("잘못된 입력입니다.");
				}
			} catch (Exception e) {
				System.out.println("오류: " + e.getMessage());
			}
		}
	}

	private void addShow(int movieId) {
		System.out.println("\n--- 상영 일정 추가 ---");
		while (true) {
			try {
				String showDate = scanStr("상영 날짜 (YYYY-MM-DD) >> ");
				String showTime = scanStr("상영 시간 (HH:mm) >> ");
				showManager.addShow(movieId, showDate + " " + showTime);
				System.out.println("새로운 상영 일정이 등록되었습니다.");
				break;
			} catch (Exception e) {
				System.out.println("오류: 날짜 또는 시간 형식이 올바르지 않습니다. (예: 2025-01-01 14:30)");
			}
		}
	}

	private void updateShow() {
		System.out.println("\n--- 상영 일정 수정 ---");
		int showId = scanInt("수정할 상영 일정 ID를 입력하세요 >> ");

		while (true) {
			try {
				String newShowDate = scanStr("새 상영 날짜 (YYYY-MM-DD) >> ");
				String newShowTime = scanStr("새 상영 시간 (HH:mm) >> ");
				showManager.updateShow(showId, newShowDate + " " + newShowTime);
				System.out.println("상영 일정이 수정되었습니다.");
				break;
			} catch (Exception e) {
				System.out.println("오류: 날짜 또는 시간 형식이 올바르지 않습니다. (예: 2025-01-01 14:30)");
			}
		}
	}

	private void deleteShow() throws Exception {
		System.out.println("\n--- 상영 일정 삭제 ---");
		int showId = scanInt("삭제할 상영 일정 ID를 입력하세요 >> ");

		showManager.deleteShow(showId);
		System.out.println("상영 일정이 삭제되었습니다.");
	}
}