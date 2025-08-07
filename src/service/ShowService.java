package service;

import dao.SeatDAO;
import dao.ShowDAO;
import dao.ShowSeatDAO;
import vo.SeatVO;
import vo.ShowSeatVO;
import vo.ShowVO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ShowService {
	private final ShowDAO showDAO;
	private final ShowSeatDAO showSeatDAO;
	private final SeatDAO seatDAO;

	private static ShowService instance;

	public static ShowService getInstance() {
		if (instance == null) {
			instance = new ShowService();
		}
		return instance;
	}

	private ShowService() {
		this.showDAO = ShowDAO.getInstance();
		this.showSeatDAO = ShowSeatDAO.getInstance();
		this.seatDAO = new SeatDAO();
	}

	public List<ShowVO> getShowsByMovieId(int movieId) {
		return showDAO.selectShowsByMovieId(movieId);
	}

	public int getRemainingSeats(int showId) {
		List<ShowSeatVO> seats = showSeatDAO.selectShowSeatsByShowId(showId);
		return (int) seats.stream().filter(seat -> !seat.isReserved()).count();
	}

	public boolean[][] getReservedSeats(int showId) {
		boolean[][] reservedSeats = new boolean[7][7];
		List<ShowSeatVO> seats = showSeatDAO.selectShowSeatsByShowId(showId);
		for (ShowSeatVO seat : seats) {
			if (seat.isReserved()) {
				if (seat.getSeatCode() != null && seat.getSeatCode().length() >= 2) {
					char row = seat.getSeatCode().charAt(0);
					int col;
					try {
						col = Integer.parseInt(seat.getSeatCode().substring(1));
						if (row >= 'A' && row <= 'G' && col >= 1 && col <= 7) {
							reservedSeats[row - 'A'][col - 1] = true;
						}
					} catch (NumberFormatException e) {
						System.err.println("좌석 코드 형식 오류: " + seat.getSeatCode());
					}
				}
			}
		}
		return reservedSeats;
	}

	public List<ShowSeatVO> getAllSeats(int showId) {
		return showSeatDAO.selectShowSeatsByShowId(showId);
	}

	public ShowSeatVO getSeatByCode(int showId, String seatCode) {
		return showSeatDAO.findShowSeatByCode(showId, seatCode);
	}

	public ShowVO getShowById(int showId) {
		return showDAO.selectShowById(showId);
	}

	public void addShow(int movieId, String showDateTime) throws Exception {
		Timestamp timestamp = parseTimestamp(showDateTime);
		ShowVO newShow = new ShowVO();
		newShow.setMovieId(movieId);
		newShow.setShowTime(timestamp);

		int newShowId = showDAO.findMaxShowId() + 1;
		newShow.setShowId(newShowId);

		showDAO.insertShow(newShow);
		createShowSeats(newShowId);
	}

	public void updateShow(int showId, String showDateTime) throws Exception {
		ShowVO show = showDAO.selectShowById(showId);
		if (show == null) {
			throw new Exception("존재하지 않는 상영 일정입니다.");
		}
		if (showSeatDAO.hasReservedSeats(showId)) {
			throw new Exception("예매된 좌석이 있어 수정할 수 없습니다.");
		}
		Timestamp timestamp = parseTimestamp(showDateTime);
		show.setShowTime(timestamp);
		showDAO.updateShow(show);
	}

	public void deleteShow(int showId) throws Exception {
		if (showSeatDAO.hasReservedSeats(showId)) {
			throw new Exception("예매된 좌석이 있어 삭제할 수 없습니다.");
		}
		showSeatDAO.deleteShowSeatsByShowId(showId);
		showDAO.deleteShow(showId);
	}

	private void createShowSeats(int showId) throws SQLException {
		List<SeatVO> allSeats = seatDAO.findAll();
		int currentShowSeatId = showSeatDAO.findMaxShowSeatId();
		for (SeatVO seat : allSeats) {
			currentShowSeatId++;
			ShowSeatVO showSeat = new ShowSeatVO();
			showSeat.setShowSeatId(currentShowSeatId);
			showSeat.setShowId(showId);
			showSeat.setSeatId(seat.getSeatId());
			showSeat.setReserved(false);
			showSeatDAO.insertShowSeat(showSeat);
		}
	}

	private Timestamp parseTimestamp(String dateTimeStr) throws DateTimeParseException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStr, formatter);
		return Timestamp.valueOf(localDateTime);
	}
}
