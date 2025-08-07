package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.DBConnector;
import vo.SeatVO;

public class SeatDAO {

	private static final String SELECT_ALL_SEATS = "SELECT * FROM SEAT";
	private static final String SELECT_SEAT_BY_ID = "SELECT * FROM SEAT WHERE SEAT_ID = ?";
	private static final String SELECT_SEAT_BY_CODE = "SELECT * FROM SEAT WHERE SEAT_CODE = ?";

	public SeatVO findById(int seatId) {
		SeatVO seat = null;

		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_SEAT_BY_ID)) {

			ps.setInt(1, seatId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					seat = extractSeatFromResultSet(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return seat;
	}

	public List<SeatVO> findAll() {
		List<SeatVO> seats = new ArrayList<>();

		try (Connection conn = DBConnector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_SEATS)) {

			while (rs.next()) {
				seats.add(extractSeatFromResultSet(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return seats;
	}

	public SeatVO findByCode(String seatCode) {
		SeatVO seat = null;

		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_SEAT_BY_CODE)) {

			ps.setString(1, seatCode);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					seat = extractSeatFromResultSet(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return seat;
	}

	private SeatVO extractSeatFromResultSet(ResultSet rs) throws SQLException {
		SeatVO seat = new SeatVO();
		seat.setSeatId(rs.getInt("SEAT_ID"));
		seat.setRowLabel(rs.getString("ROW_LABEL"));
		seat.setColNum(rs.getInt("COL_NUM"));
		seat.setSeatCode(rs.getString("SEAT_CODE"));
		return seat;
	}
}
