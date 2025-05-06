package ui.user;

import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;
import impl.MenuManger;
import ui.BaseView;
import service.MovieService;

import java.util.List;

public class MovieListView extends BaseView implements MenuManger {
    
    @Override
    public void execute() throws Exception {
        // MVL-01: 영화 목록 데이터 불러오기
        MovieService movieService = new MovieService();
        List<MovieVO> movies = movieService.getAllMovies();
        
        displayMovieList(movies);
        
        // 메뉴로 돌아가기
        String choice = scanStr("\n메뉴로 돌아가려면 아무 키나 입력하세요: ");
    }
    
    private void displayMovieList(List<MovieVO> movies) {
        System.out.println("\n===== 현재 상영 영화 목록 =====");
        
        if (movies == null || movies.isEmpty()) {
            System.out.println("상영 중인 영화가 없습니다.");
            return;
        }
        
        int idx = 1;
        for (MovieVO movie : movies) {
            System.out.printf("%d. 제목: %s | 러닝타임: %d분\n", 
                             idx++, movie.getTitle(), movie.getDurationMin());
            
            List<ShowVO> shows = movie.getShowList();
            if (shows == null || shows.isEmpty()) {
                System.out.println("   상영 시간 정보 없음");
            } else {
                // MVL-03: 상영 시간대 정보 출력
                for (ShowVO show : shows) {
                    // MVL-04: 남은 좌석 수 출력
                    int remainingSeats = calculateRemainingSeats(show);
                    System.out.printf("   - 상영 시간: %s | 남은 좌석: %d\n", 
                                     show.getShowTime(), remainingSeats);
                }
            }
            System.out.println(); // 영화 간 구분선
        }
        
        System.out.println("============================");
    }
    
    private int calculateRemainingSeats(ShowVO show) {
        List<ShowSeatVO> seats = show.getShowSeatList();
        if (seats == null) return 0;
        
        int remainingSeats = 0;
        for (ShowSeatVO seat : seats) {
            if (!seat.isReserved()) {
                remainingSeats++;
            }
        }
        return remainingSeats;
    }
}