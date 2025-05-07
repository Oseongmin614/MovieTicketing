package dao;

import vo.MovieVO;
import util.DBConnector;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    private static final String SELECT_ALL_MOVIES = "SELECT * FROM MOVIE";
    private static final String FIND_MOVIES_BY_TITLE = "SELECT * FROM MOVIE WHERE TITLE LIKE ?";

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
}