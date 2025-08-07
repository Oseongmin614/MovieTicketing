package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnector;
import vo.ShowSeatVO;

public class ShowSeatDAO {

	private static ShowSeatDAO instance;

	private ShowSeatDAO() {

	}

	public static ShowSeatDAO getInstance() {
		if (instance == null) {
			instance = new ShowSeatDAO();
		}
		return instance;
	}

	public List<ShowSeatVO> selectShowSeatsByShowId(int showId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ShowSeatVO> seatList = new ArrayList<>();

		try {
			conn = DBConnector.getConnection();
			String sql = "SELECT ss.*, s.ROW_LABEL, s.COL_NUM, s.SEAT_CODE " + "FROM SHOW_SEAT ss "
					+ "JOIN SEAT s ON ss.SEAT_ID = s.SEAT_ID " + "WHERE ss.SHOW_ID = ? "
					+ "ORDER BY s.ROW_LABEL, s.COL_NUM";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, showId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ShowSeatVO showSeat = new ShowSeatVO();
				showSeat.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
				showSeat.setShowId(rs.getInt("SHOW_ID"));
				showSeat.setSeatId(rs.getInt("SEAT_ID"));
				showSeat.setReserved(rs.getString("IS_RESERVED").equals("Y"));
				showSeat.setRowLabel(rs.getString("ROW_LABEL"));
				showSeat.setColNum(rs.getInt("COL_NUM"));
				showSeat.setSeatCode(rs.getString("SEAT_CODE"));
				seatList.add(showSeat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}

		return seatList;
	}

	public boolean updateSeatReservationStatus(int showSeatId, boolean isReserved) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;

		try {
			conn = DBConnector.getConnection();
			String sql = "UPDATE SHOW_SEAT SET IS_RESERVED = ? WHERE SHOW_SEAT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, isReserved ? "Y" : "N");
			pstmt.setInt(2, showSeatId);

			int count = pstmt.executeUpdate();
			if (count > 0) {
				result = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(null, pstmt, conn);
		}

		return result;
	}

	public ShowSeatVO findShowSeatByCode(int showId, String seatCode) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShowSeatVO showSeat = null;

		try {
			conn = DBConnector.getConnection();
			String sql = "SELECT ss.*, s.ROW_LABEL, s.COL_NUM, s.SEAT_CODE " + "FROM SHOW_SEAT ss "
					+ "JOIN SEAT s ON ss.SEAT_ID = s.SEAT_ID " + "WHERE ss.SHOW_ID = ? AND s.SEAT_CODE = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, showId);
			pstmt.setString(2, seatCode);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				showSeat = new ShowSeatVO();
				showSeat.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
				showSeat.setShowId(rs.getInt("SHOW_ID"));
				showSeat.setSeatId(rs.getInt("SEAT_ID"));
				showSeat.setReserved(rs.getString("IS_RESERVED").equals("Y"));
				showSeat.setRowLabel(rs.getString("ROW_LABEL"));
				showSeat.setColNum(rs.getInt("COL_NUM"));
				showSeat.setSeatCode(rs.getString("SEAT_CODE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}

		return showSeat;
	}

	public ShowSeatVO selectShowSeatById(int showSeatId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ShowSeatVO showSeat = null;

		try {
			conn = DBConnector.getConnection();
			String sql = "SELECT ss.*, s.ROW_LABEL, s.COL_NUM, s.SEAT_CODE " + "FROM SHOW_SEAT ss "
					+ "JOIN SEAT s ON ss.SEAT_ID = s.SEAT_ID " + "WHERE ss.SHOW_SEAT_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, showSeatId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				showSeat = new ShowSeatVO();
				showSeat.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
				showSeat.setShowId(rs.getInt("SHOW_ID"));
				showSeat.setSeatId(rs.getInt("SEAT_ID"));
				showSeat.setReserved(rs.getString("IS_RESERVED").equals("Y"));
				showSeat.setRowLabel(rs.getString("ROW_LABEL"));
				showSeat.setColNum(rs.getInt("COL_NUM"));
				showSeat.setSeatCode(rs.getString("SEAT_CODE"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnector.close(rs, pstmt, conn);
		}

		return showSeat;
	}

	public int findMaxShowSeatId() throws SQLException {
		String sql = "SELECT NVL(MAX(SHOW_SEAT_ID), 0) FROM SHOW_SEAT";
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public void insertShowSeat(ShowSeatVO showSeat) throws SQLException {
		String sql = "INSERT INTO SHOW_SEAT (SHOW_SEAT_ID, SHOW_ID, SEAT_ID, IS_RESERVED) VALUES (?, ?, ?, ?)";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showSeat.getShowSeatId());
			ps.setInt(2, showSeat.getShowId());
			ps.setInt(3, showSeat.getSeatId());
			ps.setString(4, showSeat.isReserved() ? "Y" : "N");
			ps.executeUpdate();
		}
	}

	public boolean hasReservedSeats(int showId) throws SQLException {
		String sql = "SELECT 1 FROM SHOW_SEAT WHERE SHOW_ID = ? AND IS_RESERVED = 'Y' AND ROWNUM = 1";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public void deleteShowSeatsByShowId(int showId) throws SQLException {
		String sql = "DELETE FROM SHOW_SEAT WHERE SHOW_ID = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showId);
			ps.executeUpdate();
		}
	}
}