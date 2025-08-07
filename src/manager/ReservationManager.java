package manager;

import java.util.List;

import service.ReservationService;
import vo.MovieVO;
import vo.ReservationVO;
import vo.UserVO;

public class ReservationManager {
	private ReservationService reservationService;

	public ReservationManager() {
		this.reservationService = new ReservationService();
	}

	public List<MovieVO> getAvailableMovies() {
		return reservationService.getAvailableMovies();
	}

	public ReservationVO processReservation(UserVO user, int showId, int showSeatId) {
		if (user == null) {
			System.out.println("로그인이 필요한 서비스입니다.");
			return null;
		}

		return reservationService.makeReservation(user.getUserNo(), showId, showSeatId);
	}

	public List<ReservationVO> getUserReservations(UserVO user) {
		if (user == null) {
			System.out.println("로그인이 필요한 서비스입니다.");
			return null;
		}

		return reservationService.getUserReservations(user.getUserNo());
	}

	public boolean cancelReservation(int reservationId, UserVO user) {
		if (user == null) {
			System.out.println("로그인이 필요한 서비스입니다.");
			return false;
		}

		ReservationVO reservation = reservationService.getUserReservations(user.getUserNo()).stream()
				.filter(r -> r.getReservationId() == reservationId).findFirst().orElse(null);

		if (reservation == null) {
			System.out.println("해당 예매 내역을 찾을 수 없거나 권한이 없습니다.");
			return false;
		}

		return reservationService.cancelReservation(reservationId);
	}
}