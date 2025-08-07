package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.DBConnector;
import vo.ShowVO;

public class ShowDAO {

	private static ShowDAO instance;

	private ShowDAO() {
	}

	public static ShowDAO getInstance() {
		if (instance == null) {
			instance = new ShowDAO();
		}
		return instance;
	}

	private ShowVO extractShowFromResultSet(ResultSet rs) throws SQLException {
		ShowVO show = new ShowVO();
		show.setShowId(rs.getInt("SHOW_ID"));
		show.setMovieId(rs.getInt("MOVIE_ID"));
		show.setShowTime(rs.getTimestamp("SHOW_TIME"));
		return show;
	}

	public List<ShowVO> selectShowsByMovieId(int movieId) {
		String sql = "SELECT * FROM SHOW WHERE MOVIE_ID = ? ORDER BY SHOW_TIME";
		List<ShowVO> showList = new ArrayList<>();
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, movieId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					showList.add(extractShowFromResultSet(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return showList;
	}

	public ShowVO selectShowById(int showId) {
		String sql = "SELECT * FROM SHOW WHERE SHOW_ID = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, showId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return extractShowFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int findMaxShowId() throws SQLException {
		String sql = "SELECT NVL(MAX(SHOW_ID), 0) FROM SHOW";
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		return 0;
	}

	public void insertShow(ShowVO show) throws SQLException {
		String sql = "INSERT INTO SHOW (SHOW_ID, MOVIE_ID, SHOW_TIME) VALUES (?, ?, ?)";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, show.getShowId());
			ps.setInt(2, show.getMovieId());
			ps.setTimestamp(3, show.getShowTime());
			ps.executeUpdate();
		}
	}

	public void updateShow(ShowVO show) throws SQLException {
		String sql = "UPDATE SHOW SET SHOW_TIME = ? WHERE SHOW_ID = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setTimestamp(1, show.getShowTime());
			ps.setInt(2, show.getShowId());
			ps.executeUpdate();
		}
	}

	public void deleteShow(int showId) throws SQLException {
		String sql = "DELETE FROM SHOW WHERE SHOW_ID = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showId);
			ps.executeUpdate();
		}
	}
}