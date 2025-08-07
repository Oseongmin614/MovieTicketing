package ui.admin;

import java.util.List;

import service.MovieService;
import ui.BaseView;
import vo.MovieVO;

public class MovieManageView extends BaseView {

	private MovieService movieService;

	public MovieManageView() {
		this.movieService = new MovieService();
	}

	@Override
	public void execute() throws Exception {
		while (true) {
			System.out.println("==================================================");
			System.out.println("		|| 영화 관리 ||");
			System.out.println("==================================================");
			System.out.println("1. 영화 목록");
			System.out.println("2. 영화 추가");
			System.out.println("3. 영화 삭제");
			System.out.println("4. 돌아가기");
			System.out.println("==================================================");
			String choice = scanStr("	항목을 선택하세요 : ");

			switch (choice) {
			case "1":
				showMovieList();
				break;
			case "2":
				addMovie();
				break;
			case "3":
				deleteMovie();
				break;
			case "4":
				return;
			default:
				System.out.println("잘못된 입력입니다. 다시 선택하세요.");
			}
		}
	}

	private void showMovieList() {
		System.out.println("==================================================");
		System.out.println("		|| 영화 목록 ||");
		System.out.println("==================================================");
		List<MovieVO> movies = movieService.getAllMovies();
		if (movies.isEmpty()) {
			System.out.println("등록된 영화가 없습니다.");
		} else {
			for (MovieVO movie : movies) {
				System.out.println(movie.getMovieId() + ". " + movie.getTitle() + " (" + movie.getDurationMin() + "분)");
				System.out.println("   - " + movie.getDescription());
			}
		}
	}

	private void addMovie() {
		System.out.println("==================================================");
		System.out.println("		|| 영화 추가 ||");
		System.out.println("==================================================");
		String title = scanStr("	영화 제목: ");
		int durationMin = scanInt("	상영 시간(분): ");
		String description = scanStr("	설명: ");

		MovieVO movie = new MovieVO();
		movie.setTitle(title);
		movie.setDurationMin(durationMin);
		movie.setDescription(description);

		try {
			movieService.addMovie(movie);
			System.out.println("영화가 성공적으로 추가되었습니다.");
		} catch (Exception e) {
			System.out.println("영화 추가에 실패했습니다. 다시 시도해주세요.");
			System.out.println("에러: " + e.getMessage());
		}
	}

	private void deleteMovie() {
		showMovieList();
		System.out.println("    0. 취소");
		int movieId = scanInt("    삭제할 영화의 ID를 입력하세요: ");

		if (movieId == 0) {
			System.out.println("영화 삭제가 취소되었습니다.");
			return;
		}

		try {
			movieService.deleteMovie(movieId);
			System.out.println("영화가 성공적으로 삭제되었습니다.");
		} catch (Exception e) {
			System.out.println("영화 삭제에 실패했습니다. 다시 시도해주세요.");
			System.out.println("에러: " + e.getMessage());
		}
	}
}
