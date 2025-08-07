package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.MovieDAO;
import dao.ReservationDAO;
import dao.ShowDAO;
import dao.ShowSeatDAO;
import vo.MovieVO;
import vo.ReservationVO;
import vo.ShowVO;
import vo.UserVO;
import vo.ShowSeatVO;

public class ReservationService {

	private MovieDAO movieDAO;
	private ShowDAO showDAO;
	private ShowSeatDAO showSeatDAO;
	private ReservationDAO reservationDAO;

	private static ReservationService instance;

	public static ReservationService getInstance() {
		if (instance == null) {
			instance = new ReservationService();
		}
		return instance;
	}

	public ReservationService() {
		this.movieDAO = MovieDAO.getInstance();
		this.showDAO = ShowDAO.getInstance();
		this.showSeatDAO = ShowSeatDAO.getInstance();
		this.reservationDAO = ReservationDAO.getInstance();
	}

	public List<MovieVO> getAvailableMovies() {
		List<MovieVO> movies = movieDAO.selectAllMovies();

		for (MovieVO movie : movies) {
			List<ShowVO> shows = showDAO.selectShowsByMovieId(movie.getMovieId());
			movie.setShowList(shows);
		}

		return movies;
	}

	public ReservationVO makeReservation(int userNo, int showId, int showSeatId) {
		ShowSeatVO seat = showSeatDAO.selectShowSeatById(showSeatId);
		if (seat == null || seat.isReserved()) {
			System.out.println("이미 예약된 좌석이거나 존재하지 않는 좌석입니다.");
			return null;
		}

		ReservationVO reservation = new ReservationVO();
		reservation.setUserNo(userNo);
		reservation.setShowId(showId);
		reservation.setShowSeatId(showSeatId);
		reservation.setReservationTime(new Date());
		reservation.setStatus("ACTIVE");

		boolean success = false;
		try {
			success = reservationDAO.saveReservation(reservation);

			if (success) {
				success = showSeatDAO.updateSeatReservationStatus(showSeatId, true);
			}

			if (!success) {
				if (reservation.getReservationId() > 0) {
					reservationDAO.cancelReservation(reservation.getReservationId());
				}
				System.out.println("예매 처리 중 오류가 발생했습니다.");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return reservation;
	}

	public List<ReservationVO> getUserReservations(int userNo) {
		return reservationDAO.findByUserNo(userNo);
	}

	public boolean cancelReservation(int reservationId) {
		ReservationVO reservation = reservationDAO.findById(reservationId);
		if (reservation == null) {
			System.out.println("해당 예매 정보를 찾을 수 없습니다.");
			return false;
		}

		if (!"ACTIVE".equals(reservation.getStatus())) {
			System.out.println("이미 취소되었거나 사용 완료된 예매입니다.");
			return false;
		}

		boolean success = false;
		try {
			success = reservationDAO.cancelReservation(reservationId);

			if (success) {
				success = showSeatDAO.updateSeatReservationStatus(reservation.getShowSeatId(), false);
			}

			if (!success) {
				System.out.println("예매 취소 처리 중 오류가 발생했습니다.");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<ShowSeatVO> getAvailableSeats(int showId) {
		List<ShowSeatVO> allSeats = showSeatDAO.selectShowSeatsByShowId(showId);
		List<ShowSeatVO> availableSeats = new ArrayList<>();

		for (ShowSeatVO seat : allSeats) {
			if (!seat.isReserved()) {
				availableSeats.add(seat);
			}
		}

		return availableSeats;
	}

	public ShowSeatVO getSeatByCode(int showId, String seatCode) {
		return showSeatDAO.findShowSeatByCode(showId, seatCode);
	}

	public List<ReservationVO> getReservationsByUserId(String userId) {
		UserVO user = LoginService.getInstance().getUser(userId);
		if (user != null) {
			return reservationDAO.findByUserNo(user.getUserNo());
		}
		return new ArrayList<>();
	}

	public ReservationVO getReservationDetails(int reservationId) {
		return reservationDAO.findById(reservationId);
	}
}