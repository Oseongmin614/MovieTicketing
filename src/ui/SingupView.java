package ui;

import service.UserService;
import vo.UserVO;

public class SingupView extends BaseView {

	private UserService userService;

	public SingupView() {
		this.userService = new UserService();
	}

	@Override
	public void execute() throws Exception {
		System.out.println("==================================================");
		System.out.println("		|| 회원가입 ||");
		System.out.println("==================================================");

		System.out.print("아이디: ");
		String userId = scanStr("");

		System.out.print("비밀번호: ");
		String password = scanStr("");

		System.out.print("이름: ");
		String name = scanStr("");

		UserVO user = new UserVO();
		user.setUserId(userId);
		user.setPassword(password);
		user.setName(name);
		user.setRole("USER");

		boolean success = userService.registerUser(user);

		if (success) {
			System.out.println("회원가입이 완료되었습니다.");
		} else {
			System.out.println("회원가입에 실패했습니다. 다시 시도해주세요.");
		}
	}
}