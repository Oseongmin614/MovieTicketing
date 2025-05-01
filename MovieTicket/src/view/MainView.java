package view;


import model.impl.UserManager;
import view.menu.BaseView;
import view.Login;
import view.Singup;

/* 회원가입,로그인 */
public class MainView extends BaseView {
    private String menu(){
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
        while (true){
            try{
                String choice = menu();
                UserManager View =null;
                switch (choice){
                    case "1":
                        View = new Login();
                        break;
                    case "2":
                        View = new Singup();
                }
            }
            }catch (ChoiceOutOfBoundException e) {
                System.out.println(e.getMessage());
        }
    }
}
