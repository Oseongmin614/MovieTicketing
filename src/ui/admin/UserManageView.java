package ui.admin;

import java.util.List;

import manager.UserManager;
import ui.BaseView;
import vo.UserVO;
import ui.MainView;

public class UserManageView extends BaseView {

	private final UserManager userManager;

	public UserManageView() {
		this.userManager = UserManager.getInstance();
	}

	@Override
	public void execute() {
		while (true) {
			try {
				System.out.println("\n===== 사용자 관리 =====");
				List<UserVO> users = userManager.getAllUsers();
				if (users.isEmpty()) {
					System.out.println("등록된 사용자가 없습니다.");
				} else {
					System.out.printf("%-10s | %-15s | %-10s | %-10s | %-20s\n", "사용자 번호", "아이디", "이름", "역할", "가입일");
					System.out.println("--------------------------------------------------------------------------");
					for (UserVO user : users) {
						System.out.printf("%-12d | %-15s | %-10s | %-10s | %-20s\n", user.getUserNo(), user.getUserId(),
								user.getName(), user.getRole(), user.getRegDate());
					}
				}
				System.out.println("======================");
				System.out.println("1. 사용자 정보 수정");
				System.out.println("2. 사용자 삭제");
				System.out.println("3. 돌아가기");

				int choice = scanInt("메뉴 선택 >> ");

				switch (choice) {
				case 1:
					updateUser();
					break;
				case 2:
					deleteUser();
					break;
				case 3:
					return;
				default:
					System.out.println("잘못된 입력입니다.");
				}
			} catch (Exception e) {
				System.out.println("오류: " + e.getMessage());
			}
		}
	}

	private void updateUser() {
		System.out.println("\n--- 사용자 정보 수정 ---");
		String userId = scanStr("수정할 사용자의 아이디를 입력하세요 >> ");
		UserVO user = userManager.getUserById(userId);

		if (user == null) {
			System.out.println("존재하지 않는 사용자입니다.");
			return;
		}

		System.out.printf("이름 (%s) >> ", user.getName());
		String name = scanStr("", user.getName());
		System.out.printf("역할 (%s) >> ", user.getRole());
		String role = scanStr("", user.getRole());

		user.setName(name);
		user.setRole(role);

		if (userManager.updateUser(user)) {
			System.out.println("사용자 정보가 수정되었습니다.");
		} else {
			System.out.println("사용자 정보 수정에 실패했습니다.");
		}
	}

	private void deleteUser() {
		System.out.println("\n--- 사용자 삭제 ---");
		String userId = scanStr("삭제할 사용자의 아이디를 입력하세요 >> ");

		if (userManager.deleteUser(userId)) {
			System.out.println("사용자가 삭제되었습니다.");
		} else {
			System.out.println("사용자 삭제에 실패했습니다.");
		}
	}
}