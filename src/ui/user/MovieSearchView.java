package ui.user;

import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;
import service.MovieService;
import ui.BaseView;

import java.util.List;

import manager.MenuManger;

public class MovieSearchView extends BaseView implements MenuManger {

	private MovieService movieService;

	public MovieSearchView(MovieService movieService) {
		this.movieService = movieService;
	}

	@Override
	public void execute() throws Exception {
		System.out.println("===== 영화 검색 =====");
		String keyword = scanStr("검색할 영화 제목(키워드)을 입력하세요: ").trim();

		if (keyword.isEmpty() || keyword.length() < 2) {
			System.out.println("검색어를 두 글자 이상 입력하세요.");
			return;
		}

		List<MovieVO> movies = movieService.searchMoviesByTitle(keyword);

		if (movies == null || movies.isEmpty()) {
			System.out.println("해당 영화가 존재하지 않습니다.");
			return;
		}

		System.out.println("\n=================== 검색 결과 ======================");
		int idx = 1;
		for (MovieVO movie : movies) {
			System.out.printf("[%d] %s | 러닝타임: %d분\n", idx++, movie.getTitle(), movie.getDurationMin());
			List<ShowVO> shows = movie.getShowList();
			if (shows != null && !shows.isEmpty()) {
				for (ShowVO show : shows) {
					int remainSeats = 0;
					List<ShowSeatVO> seats = show.getShowSeatList();
					if (seats != null) {
						for (ShowSeatVO seat : seats) {
							if (!seat.isReserved())
								remainSeats++;
						}
					}
					System.out.printf("   - 상영 시간: %s | 남은 좌석: %d\n", show.getShowTime(), 49 - remainSeats);
				}
			} else {
				System.out.println("   상영 정보 없음");
			}
		}
		System.out.println("==================================================");
	}
}
