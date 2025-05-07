
package dao;

import util.DBConnector;
import vo.ShowVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowDAO {

	private static final String SELECT_SHOWS_BY_MOVIE = "SELECT * FROM SHOW WHERE MOVIE_ID = ?";

	public static List<ShowVO> selectShowsByMovieId(int movieId) {
		List<ShowVO> shows = new ArrayList<>();
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement ps = conn.prepareStatement(SELECT_SHOWS_BY_MOVIE)) {

			ps.setInt(1, movieId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					ShowVO show = new ShowVO();
					show.setShowId(rs.getInt("SHOW_ID"));
					show.setMovieId(rs.getInt("MOVIE_ID"));
					show.setShowTime(rs.getString("SHOW_TIME"));
					shows.add(show);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return shows;
	}
}