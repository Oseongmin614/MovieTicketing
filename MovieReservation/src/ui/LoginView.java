package ui;

import service.LoginService;
import ui.admin.AdminMenuView;
import ui.user.UserMenuView;

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
        UserMenuView UserMenu = new UserMenuView();
        
        if (loginService.login(userId, password)) {
            String role = loginService.getRole(userId);
            System.out.println("로그인 성공! (" + role + ")");

            // LOGIN-04: 역할에 따라 분기 (관리자/유저 메뉴로 이동)
            if ("admin".equals(role)) {
                new AdminMenuView().execute();
            } else {
                new UserMenuView().execute();
            }
        } else {
            System.out.println("아이디 또는 비밀번호가 일치하지 않 습니다.");
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