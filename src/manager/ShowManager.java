package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.ShowService;
import vo.MovieVO;
import vo.SeatVO;
import vo.ShowSeatVO;
import vo.ShowVO;

public class ShowManager {

	private ShowService showService;

	private static ShowManager instance;

	public static ShowManager getInstance() {
		if (instance == null) {
			instance = new ShowManager();
		}
		return instance;
	}

	private ShowManager() {
		this.showService = ShowService.getInstance();
	}

	public List<ShowVO> getShowsByMovie(int movieId) {
		return showService.getShowsByMovieId(movieId);
	}

	public int getRemainingSeats(int showId) {
		return showService.getRemainingSeats(showId);
	}

	public boolean[][] getSeatMap(int showId) {
		return showService.getReservedSeats(showId);
	}

	public boolean isValidSeatCode(String seatCode) {
		if (seatCode == null || seatCode.length() < 2) {
			return false;
		}

		char row = seatCode.charAt(0);
		String colStr = seatCode.substring(1);

		if (row < 'A' || row > 'G') {
			return false;
		}

		try {
			int col = Integer.parseInt(colStr);
			return col >= 1 && col <= 7;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean isSeatAvailable(int showId, String seatCode) {
		if (!isValidSeatCode(seatCode)) {
			return false;
		}

		boolean[][] seatMap = getSeatMap(showId);

		char row = seatCode.charAt(0);
		int col = Integer.parseInt(seatCode.substring(1));

		int rowIndex = row - 'A';
		int colIndex = col - 1;

		if (rowIndex < 0 || rowIndex >= seatMap.length || colIndex < 0 || colIndex >= seatMap[0].length) {
			return false;
		}

		return !seatMap[rowIndex][colIndex];
	}

	public List<ShowSeatVO> getAllSeats(int showId) {
		return showService.getAllSeats(showId);
	}

	public ShowSeatVO getSeatByCode(int showId, String seatCode) {
		return showService.getSeatByCode(showId, seatCode);
	}

	public ShowVO getShowById(int showId) {
		return showService.getShowById(showId);
	}

	public void addShow(int movieId, String showDateTime) throws Exception {
		showService.addShow(movieId, showDateTime);
	}

	public void updateShow(int showId, String showDateTime) throws Exception {
		showService.updateShow(showId, showDateTime);
	}

	public void deleteShow(int showId) throws Exception {
		showService.deleteShow(showId);
	}
}