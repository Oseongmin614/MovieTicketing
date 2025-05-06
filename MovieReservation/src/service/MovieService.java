package service;

import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;


import java.util.List;

public class MovieService {
    private MovieDAO movieDAO;
    
    public MovieService() {
        this.movieDAO = new MovieDAO();
    }
    
    public List<MovieVO> getAllMovies() {
        // 영화 목록 조회
        List<MovieVO> movies = movieDAO.selectAllMovies();
        
        // 각 영화마다 상영시간 목록 설정
        for (MovieVO movie : movies) {
            List<ShowVO> shows = movieDAO.selectShowsByMovieId(movie.getMovieId());
            movie.setShowList(shows);
            
            // 각 상영시간마다 좌석 목록 설정
            for (ShowVO show : shows) {
                List<ShowSeatVO> showSeats = movieDAO.selectShowSeatsByShowId(show.getShowId());
                show.setShowSeatList(showSeats);
            }
        }
        
        return movies;
    }
}
