package service;

import dao.MovieDAO;
import dao.ShowDAO;
import dao.ShowSeatDAO;
import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;
import java.util.List;

public class MovieService {
    private final MovieDAO movieDAO;

    public MovieService() {
        this.movieDAO = new MovieDAO(); 
    }

    // 기존 전체 영화 조회
    public List<MovieVO> getAllMovies() {
        List<MovieVO> movies = movieDAO.selectAllMovies();
        loadMovieDetails(movies);
        return movies;
    }

    // 신규 추가: 제목 기반 영화 검색
    public List<MovieVO> searchMoviesByTitle(String keyword) {
        List<MovieVO> movies = movieDAO.findMoviesByTitle("%" + keyword + "%");
        loadMovieDetails(movies);
        return movies;
    }

    // 중복 코드 추출 (상영정보 & 좌석정보 로드)
    private void loadMovieDetails(List<MovieVO> movies) {
        for (MovieVO movie : movies) {
            List<ShowVO> shows = ShowDAO.selectShowsByMovieId(movie.getMovieId());
            movie.setShowList(shows);
            for (ShowVO show : shows) {
                List<ShowSeatVO> seats = ShowSeatDAO.selectShowSeatsByShowId(show.getShowId());
                show.setShowSeatList(seats);
            }
        }
    }
}
