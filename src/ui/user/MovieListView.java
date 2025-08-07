package ui.user;

import vo.MovieVO;
import vo.ShowVO;
import vo.ShowSeatVO;
import ui.BaseView;
import service.MovieService;

import java.util.List;

import manager.MenuManger;

public class MovieListView extends BaseView implements MenuManger {

	@Override
	public void execute() throws Exception {

		MovieService movieService = new MovieService();
		List<MovieVO> movies = movieService.getAllMovies();

		displayMovieList(movies);

		String choice = scanStr("\n메뉴로 돌아가려면 아무 키나 입력하세요: ");
	}

	private void displayMovieList(List<MovieVO> movies) {
		System.out.println("\n================= 현재 상영 영화 목록 ================");

		if (movies == null || movies.isEmpty()) {
			System.out.println("상영 중인 영화가 없습니다.");
			return;
		}

		int idx = 1;
		for (MovieVO movie : movies) {
			System.out.printf("%d. 제목: %s\t| 러닝타임: %d분\n", idx++, movie.getTitle(), movie.getDurationMin());

			List<ShowVO> shows = movie.getShowList();
			if (shows == null || shows.isEmpty()) {
				System.out.println("   상영 시간 정보 없음");
			} else {
				for (ShowVO show : shows) {

					System.out.printf("   - 상영 시간: %s | 남은 좌석: %d\n", show.getShowTime(),
							49 - calculateRemainingSeats(show));
				}
			}
			System.out.println();
		}

		System.out.println("==================================================");
	}

	private int calculateRemainingSeats(ShowVO show) {
		List<ShowSeatVO> seats = show.getShowSeatList();
		if (seats == null)
			return 0;

		int remainingSeats = 0;
		for (ShowSeatVO seat : seats) {
			if (!seat.isReserved()) {
				remainingSeats++;
			}
		}
		return remainingSeats;
	}
}