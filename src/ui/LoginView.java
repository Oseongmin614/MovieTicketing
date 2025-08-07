package ui;

import service.LoginService;
import ui.admin.AdminMenuView;
import ui.user.UserMenuView;
import vo.UserVO;

public class LoginView extends BaseView {
	private final LoginService loginService;

	public LoginView() {
		this.loginService = LoginService.getInstance();
	}

	@Override
	public void execute() throws Exception {
		System.out.println("\n [ 로그인 ] ");
		String userId = scanStr("아이디 :");
		String password = scanStrMasked("비밀번호 :");

		if (loginService.login(userId, password)) {
			String role = loginService.getRole(userId);
			System.out.println("로그인 성공! (" + role + ")");
			UserVO loginUser = loginService.getUser(userId);
			if ("ADMIN".equals(role)) {
				new AdminMenuView().execute();
			} else {
				new UserMenuView(loginUser).execute();
			}
		} else {
			System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
			new MainView().execute();
		}
	}

	private String scanStrMasked(String prompt) {
		System.out.print(prompt);
		if (System.console() != null) {
			return new String(System.console().readPassword());
		}
		return scanStr("");
	}
}