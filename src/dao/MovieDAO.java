package dao;

import vo.MovieVO;
import util.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
	private static final String SELECT_ALL_MOVIES = "SELECT * FROM MOVIE";
	private static final String FIND_MOVIES_BY_TITLE = "SELECT * FROM MOVIE WHERE TITLE LIKE ?";
	private static final String SELECT_MOVIE_BY_ID = "SELECT * FROM MOVIE WHERE MOVIE_ID = ?";
	private static MovieDAO instance;

	public MovieDAO() {
	}

	public static MovieDAO getInstance() {
		if (instance == null) {
			instance = new MovieDAO();
		}
		return instance;
	}

	public List<MovieVO> selectAllMovies() {
		List<MovieVO> movies = new ArrayList<>();
		try (Connection conn = DBConnector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SELECT_ALL_MOVIES)) {

			while (rs.next()) {
				movies.add(extractMovieFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}

	public List<MovieVO> findMoviesByTitle(String titleKeyword) {
		List<MovieVO> movies = new ArrayList<>();
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(FIND_MOVIES_BY_TITLE)) {

			ps.setString(1, "%" + titleKeyword + "%");
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					movies.add(extractMovieFromResultSet(rs));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movies;
	}

	private MovieVO extractMovieFromResultSet(ResultSet rs) throws SQLException {
		MovieVO movie = new MovieVO();
		movie.setMovieId(rs.getInt("MOVIE_ID"));
		movie.setTitle(rs.getString("TITLE"));
		movie.setDurationMin(rs.getInt("DURATION_MIN"));
		movie.setDescription(rs.getString("DESCRIPTION"));
		movie.setRegDate(rs.getDate("REG_DATE"));
		return movie;
	}

	public MovieVO selectMovieById(int movieId) {
		MovieVO movie = null;
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_MOVIE_BY_ID)) {

			ps.setInt(1, movieId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					movie = extractMovieFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movie;
	}

	public void insertMovie(MovieVO movie) throws SQLException {
		String sql = "INSERT INTO MOVIE (MOVIE_ID, TITLE, DURATION_MIN, DESCRIPTION, REG_DATE) VALUES (?, ?, ?, ?, SYSDATE)";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, movie.getMovieId());
			ps.setString(2, movie.getTitle());
			ps.setInt(3, movie.getDurationMin());
			ps.setString(4, movie.getDescription());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public int findMaxMovieId() {
		int maxId = 0;
		String sql = "SELECT MAX(MOVIE_ID) FROM MOVIE";
		try (Connection conn = DBConnector.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			if (rs.next()) {
				maxId = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxId;
	}

	public void deleteMovie(int movieId) throws SQLException {
		String sql = "DELETE FROM MOVIE WHERE MOVIE_ID = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, movieId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
	}

	public MovieVO findMovieByTitle(String title) {
		MovieVO movie = null;
		String sql = "SELECT * FROM MOVIE WHERE TITLE = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, title);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					movie = extractMovieFromResultSet(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return movie;
	}
}
