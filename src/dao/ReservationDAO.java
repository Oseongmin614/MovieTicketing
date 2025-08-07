package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import util.DBConnector;
import vo.ReservationVO;
import vo.ShowSeatVO;

public class ReservationDAO implements BaseDAO<ReservationVO, Integer> {
	private static ReservationDAO instance;

	public ReservationDAO() {
	}

	public static synchronized ReservationDAO getInstance() {
		if (instance == null) {
			instance = new ReservationDAO();
		}
		return instance;
	}

	private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM RESERVATION";

	private static final String SELECT_RESERVATION_BY_ID = "SELECT r.RESERVATION_ID, r.USER_NO, r.SHOW_ID, r.SHOW_SEAT_ID, r.RESERVED_DATE, r.STATUS, "
			+ "m.TITLE AS MOVIE_TITLE, s.SHOW_TIME, se.SEAT_CODE " + "FROM RESERVATION r "
			+ "JOIN SHOW s ON r.SHOW_ID = s.SHOW_ID " + "JOIN MOVIE m ON s.MOVIE_ID = m.MOVIE_ID "
			+ "JOIN SHOW_SEAT ss ON r.SHOW_SEAT_ID = ss.SHOW_SEAT_ID " + "JOIN SEAT se ON ss.SEAT_ID = se.SEAT_ID "
			+ "WHERE r.RESERVATION_ID = ?";

	private static final String SELECT_RESERVATIONS_BY_USER = "SELECT r.RESERVATION_ID, r.USER_NO, r.SHOW_ID, r.SHOW_SEAT_ID, r.RESERVED_DATE, r.STATUS, "
			+ "m.TITLE AS MOVIE_TITLE, s.SHOW_TIME, se.SEAT_CODE " + "FROM RESERVATION r "
			+ "JOIN SHOW s ON r.SHOW_ID = s.SHOW_ID " + "JOIN MOVIE m ON s.MOVIE_ID = m.MOVIE_ID "
			+ "JOIN SHOW_SEAT ss ON r.SHOW_SEAT_ID = ss.SHOW_SEAT_ID " + "JOIN SEAT se ON ss.SEAT_ID = se.SEAT_ID "
			+ "WHERE r.USER_NO = ? ORDER BY r.RESERVED_DATE DESC";

	private static final String INSERT_RESERVATION = "INSERT INTO RESERVATION (RESERVATION_ID, USER_NO, SHOW_ID, SHOW_SEAT_ID, RESERVED_DATE, STATUS) "
			+ "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";

	private static final String UPDATE_RESERVATION_STATUS = "UPDATE RESERVATION SET STATUS = ? WHERE RESERVATION_ID = ?";

	private static final String SELECT_SHOW_SEAT_BY_ID = "SELECT * FROM SHOW_SEAT WHERE SHOW_SEAT_ID = ?";

	private static final String UPDATE_SHOW_SEAT = "UPDATE SHOW_SEAT SET IS_RESERVED = ? WHERE SHOW_SEAT_ID = ?";

	@Override
	public List<ReservationVO> findAll() {
		List<ReservationVO> reservations = new ArrayList<>();

		try (Connection conn = DBConnector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_RESERVATIONS)) {

			while (rs.next()) {
				reservations.add(extractReservationFromResultSet(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reservations;
	}

	@Override
	public ReservationVO findById(Integer id) {
		ReservationVO reservation = null;

		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_RESERVATION_BY_ID)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					reservation = extractReservationFromResultSet(rs);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reservation;
	}

	@Override
	public ReservationVO save(ReservationVO entity) {
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(INSERT_RESERVATION, new String[] { "RESERVATION_ID" })) {

			ps.setInt(1, entity.getUserNo());
			ps.setInt(2, entity.getShowId());
			ps.setInt(3, entity.getShowSeatId());
			ps.setTimestamp(4, new Timestamp(entity.getReservedDate().getTime()));
			ps.setString(5, entity.getStatus() != null ? entity.getStatus() : "ACTIVE");

			int affectedRows = ps.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						entity.setReservationId(rs.getInt(1));
						updateSeatStatus(conn, entity.getShowSeatId(), "Y");
						return entity;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public ReservationVO update(ReservationVO entity) {
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(
						"UPDATE RESERVATION SET USER_NO = ?, SHOW_ID = ?, SHOW_SEAT_ID = ?, STATUS = ? WHERE RESERVATION_ID = ?")) {

			ps.setInt(1, entity.getUserNo());
			ps.setInt(2, entity.getShowId());
			ps.setInt(3, entity.getShowSeatId());
			ps.setString(4, entity.getStatus());
			ps.setInt(5, entity.getReservationId());

			int affectedRows = ps.executeUpdate();

			if (affectedRows > 0) {
				return entity;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void delete(Integer id) {
		cancelReservation(id);
	}

	public boolean cancelReservation(int reservationId) {
		String sql = "UPDATE RESERVATION SET STATUS = 'CANCELED' WHERE RESERVATION_ID = ?";

		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, reservationId);
			ReservationVO reservation = findById(reservationId);

			if (reservation != null) {
				int result = ps.executeUpdate();

				if (result > 0) {
					return updateSeatStatus(conn, reservation.getShowSeatId(), "N");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	private boolean updateSeatStatus(Connection conn, int showSeatId, String isReserved) {
		String sql = "UPDATE SHOW_SEAT SET IS_RESERVED = ? WHERE SHOW_SEAT_ID = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, isReserved);
			ps.setInt(2, showSeatId);

			int result = ps.executeUpdate();
			return result > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<ReservationVO> findByUserNo(int userNo) {
		List<ReservationVO> reservations = new ArrayList<>();

		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_RESERVATIONS_BY_USER)) {

			ps.setInt(1, userNo);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					reservations.add(extractReservationFromResultSet(rs));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reservations;
	}

	private ReservationVO extractReservationFromResultSet(ResultSet rs) throws SQLException {
		ReservationVO reservation = new ReservationVO();
		reservation.setReservationId(rs.getInt("RESERVATION_ID"));
		reservation.setUserNo(rs.getInt("USER_NO"));
		reservation.setShowId(rs.getInt("SHOW_ID"));
		reservation.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
		reservation.setReservedDate(rs.getTimestamp("RESERVED_DATE"));
		reservation.setStatus(rs.getString("STATUS"));

		reservation.setMovieTitle(rs.getString("MOVIE_TITLE"));
		reservation.setShowTime(rs.getTimestamp("SHOW_TIME"));
		reservation.setSeatCode(rs.getString("SEAT_CODE"));

		return reservation;
	}

	public boolean saveReservation(ReservationVO reservation) {
		return save(reservation) != null;
	}

}