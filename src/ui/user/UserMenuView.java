package ui.user;

import exception.ChoiceOutOfBoundException;
import manager.MenuManger;
import service.MovieService;
import ui.BaseView;
import ui.MainView;
import vo.UserVO;

public class UserMenuView extends BaseView {

	private UserVO currentUser;

	public UserMenuView(UserVO user) {
		this.currentUser = user;
	}

	private String menu() {
		System.out.println("==================================================");
		System.out.println("		|| 사용자 메뉴 ||		" + currentUser.getUserId());
		System.out.println("==================================================");
		System.out.println("1. 영화 목록 보기");
		System.out.println("2. 영화 검색");
		System.out.println("3. 영화 예매");
		System.out.println("4. 내 예매 내역");
		System.out.println("5. 로그아웃");
		System.out.println("0. 종료");
		System.out.println("==================================================");
		String choice = scanStr("	항목을 선택하세요 :");
		return choice;
	}

	@Override
	public void execute() throws Exception {
		while (true) {
			try {
				String choice = menu();
				MenuManger view = null;

				switch (choice) {
				case "1":
					view = new MovieListView();
					break;
				case "2":
					System.out.println("영화 검색 화면으로 이동합니다...");
					view = new MovieSearchView(new MovieService());
					break;
				case "3":
					System.out.println("영화 예매 화면으로 이동합니다...");
					view = new ReservationView(currentUser);
					break;
				case "4":
					System.out.println("예매 내역을 확인합니다...");
					view = new MyReservationView(currentUser);
					break;
				case "5":
					System.out.println("로그아웃 되었습니다.");
					new MainView().execute();
					return;
				case "0":
					System.out.println("프로그램을 종료합니다.");
					System.exit(0);
				default:
					System.out.println("잘못된 입력입니다. 다시 선택하세요.");
					continue;
				}

				if (view != null) {
					view.execute();
				}
			} catch (ChoiceOutOfBoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}