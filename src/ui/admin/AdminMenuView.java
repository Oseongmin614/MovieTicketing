package ui.admin;

import exception.ChoiceOutOfBoundException;
import ui.BaseView;
import ui.MainView;

public class AdminMenuView extends BaseView {

	private String menu() {
		System.out.println("==================================================");
		System.out.println("		|| 관리자 메뉴 ||");
		System.out.println("==================================================");
		System.out.println("1. 영화 관리");
		System.out.println("2. 상영 일정 관리");
		System.out.println("3. 사용자 관리");
		System.out.println("4. 로그아웃");
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

				switch (choice) {
				case "1":
					System.out.println("영화 관리 화면으로 이동합니다...");
					MovieManageView movieManageView = new MovieManageView();
					movieManageView.execute();
					break;
				case "2":
					System.out.println("상영 일정 관리 화면으로 이동합니다...");
					new ShowManageView().execute();
					break;
				case "3":
					System.out.println("사용자 관리 화면으로 이동합니다...");
					new UserManageView().execute();
					break;
				case "4":
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
			} catch (ChoiceOutOfBoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}