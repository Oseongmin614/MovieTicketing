package ui.user;

import java.util.List;

import service.ReservationService;
import ui.BaseView;
import vo.ReservationVO;
import vo.UserVO;

public class MyReservationView extends BaseView {

	private UserVO currentUser;
	private ReservationService reservationService;

	public MyReservationView(UserVO user) {
		this.currentUser = user;
		this.reservationService = ReservationService.getInstance();
	}

	@Override
	public void execute() throws Exception {
		System.out.println("==================================================");
		System.out.println("		|| 내 예매 내역 ||");
		System.out.println("==================================================");

		List<ReservationVO> reservations = reservationService.getReservationsByUserId(currentUser.getUserId());

		reservations.removeIf(r -> "CANCELED".equals(r.getStatus()));

		if (reservations.isEmpty()) {
			System.out.println("예매 내역이  없습니다.");
		} else {
			System.out.println("---------------------------------------------------------------------");
			System.out.printf("%-5s %-15s %-10s %-10s %-10s %-10s\n", "번호", "영화 제목", "상영 시간", "좌석", "예매일", "상태");
			System.out.println("---------------------------------------------------------------------");
			for (int i = 0; i < reservations.size(); i++) {
				ReservationVO reservation = reservations.get(i);
				String status = "CANCELED".equals(reservation.getStatus()) ? "취소됨" : "예매완료";
				System.out.printf("%-5d %-15s %-10s %-10s %-10s %-10s\n", (i + 1), reservation.getMovieTitle(),
						reservation.getShowTime(), reservation.getSeatCode(), reservation.getReservedDate(), status);
			}
			System.out.println("---------------------------------------------------------------------");

			while (true) {
				System.out.print("상세 조회 및 취소를 원하는 예매 번호를 입력하세요 (0번: 이전 메뉴): ");
				int choice = scanInt("");

				if (choice == 0) {
					break;
				}

				if (choice > 0 && choice <= reservations.size()) {
					ReservationVO reservation = reservations.get(choice - 1);
					printReservationDetails(reservation);

					if ("ACTIVE".equals(reservation.getStatus())) {
						System.out.print("정말로 예매를 취소하시겠습니까? (Y/N): ");
						String confirm = scanStr("");
						if (confirm.equalsIgnoreCase("Y")) {
							boolean success = reservationService.cancelReservation(reservation.getReservationId());
							if (success) {
								System.out.println("예매가 성공적으로 취소되었습니다.");
								reservations = reservationService.getReservationsByUserId(currentUser.getUserId());
							} else {
								System.out.println("예매 취소에 실패했습니다.");
							}
						}
					} else {
						System.out.println("이미 취소되었거나 완료된 예매입니다.");
					}
					break;
				} else {
					System.out.println("잘못된 번호입니다. 다시 입력해주세요.");
				}
			}
		}
	}

	private void printReservationDetails(ReservationVO reservation) {
		System.out.println("========== 예매 상세 정보 ==========");
		System.out.println("예매 번호: " + reservation.getReservationId());
		System.out.println("영화 제목: " + reservation.getMovieTitle());
		System.out.println("상영 시간: " + reservation.getShowTime());
		System.out.println("좌석 번호: " + reservation.getSeatCode());
		System.out.println("예매 일시: " + reservation.getReservedDate());
		System.out.println("예매 상태: " + reservation.getStatus());
		System.out.println("====================================");
	}
}
