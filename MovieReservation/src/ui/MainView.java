package ui;


import exception.ChoiceOutOfBoundException;
import impl.MenuManger;

/* 회원가입,로그인 */
public class MainView extends BaseView {
    private String menu() {
        System.out.println("==================================================");
        System.out.println("\t\t|| 하이테크 영화관 ||");
        System.out.println("==================================================");
        System.out.println("1.로그인");
        System.out.println("2.회원가입");
        System.out.println("==================================================");
        String type = scanStr("\t항목을 선택하세요 :");
        return type;
    }

    @Override
    public void execute() throws Exception {
        while (true) {
            try {
                String choice = menu();
                MenuManger View = null;
                switch (choice) {
                case "1":
                    View = new LoginView();
                    break;
                case "2":
                    View = new SingupView();
                    break;
                default:
                    System.out.println("잘못된 입력입니다. 다시 선택하세요.");
                    continue;
	            }
	            if (View != null) {
	                View.execute();
	            }
            } catch (ChoiceOutOfBoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}