package service;

import java.sql.SQLException;
import dao.MovieDAO;
import dao.ShowDAO;
import dao.ShowSeatDAO;
import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;
import java.util.List;

public class MovieService {
	private final MovieDAO movieDAO;
	private final ShowDAO showDAO;
	private final ShowSeatDAO showSeatDAO;

	public MovieService() {
		this.movieDAO = new MovieDAO();
		this.showDAO = ShowDAO.getInstance();
		this.showSeatDAO = ShowSeatDAO.getInstance();
	}

	public List<MovieVO> getAllMovies() {
		List<MovieVO> movies = movieDAO.selectAllMovies();
		loadMovieDetails(movies);
		return movies;
	}

	public List<MovieVO> searchMoviesByTitle(String keyword) {
		List<MovieVO> movies = movieDAO.findMoviesByTitle("%" + keyword + "%");
		loadMovieDetails(movies);
		return movies;
	}

	private void loadMovieDetails(List<MovieVO> movies) {
		for (MovieVO movie : movies) {
			List<ShowVO> shows = showDAO.selectShowsByMovieId(movie.getMovieId());
			movie.setShowList(shows);

			for (ShowVO show : shows) {
				List<ShowSeatVO> seats = showSeatDAO.selectShowSeatsByShowId(show.getShowId());
				show.setShowSeatList(seats);
			}
		}
	}

	public MovieVO getMovieById(int movieId) {
		MovieVO movie = movieDAO.selectMovieById(movieId);
		if (movie != null) {
			List<ShowVO> shows = showDAO.selectShowsByMovieId(movie.getMovieId());
			movie.setShowList(shows);
			for (ShowVO show : shows) {
				List<ShowSeatVO> seats = showSeatDAO.selectShowSeatsByShowId(show.getShowId());
				show.setShowSeatList(seats);
			}
		}
		return movie;
	}

	public void addMovie(MovieVO movie) throws Exception {

		if (movieDAO.findMovieByTitle(movie.getTitle()) != null) {
			throw new Exception("이미 존재하는 영화입니다.");
		}

		int newMovieId = movieDAO.findMaxMovieId() + 1;
		movie.setMovieId(newMovieId);
		movieDAO.insertMovie(movie);
	}

	public void deleteMovie(int movieId) throws Exception {

		if (movieDAO.selectMovieById(movieId) == null) {
			throw new Exception("존재하지 않는 영화입니다.");
		}
		try {
			movieDAO.deleteMovie(movieId);
		} catch (SQLException e) {
			if (e.getErrorCode() == 2292) {
				throw new Exception("상영 시간이 있어서 삭제 할 수 없습니다.");
			}
			throw e;
		}
	}
}