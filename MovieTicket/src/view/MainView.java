package view;
/* 회원가입,로그인 */
public class MainView extends BaseView {
    private String menu(){
        System.out.println("==================================================");
        System.out.println("\t\t|| 성민 영화관 ||");
        System.out.println("==================================================");

        String type = scanStr("\t항목을 선택하세요 :");
        return type;
    }

}
