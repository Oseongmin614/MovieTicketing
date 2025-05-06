package ui.user;

import exception.ChoiceOutOfBoundException;
import impl.MenuManger;
import ui.BaseView;
import ui.MainView;

public class UserMenuView extends BaseView {
    
    private String menu() {
        System.out.println("==================================================");
        System.out.println("\t\t|| 사용자 메뉴 ||");
        System.out.println("==================================================");
        System.out.println("1. 영화 목록 보기");
        System.out.println("2. 영화 검색");
        System.out.println("3. 영화 예매");
        System.out.println("4. 내 예매 내역");
        System.out.println("5. 로그아웃");
        System.out.println("==================================================");
        String choice = scanStr("\t항목을 선택하세요 :");
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
                        // 영화 목록 보기
                        System.out.println("영화 목록을 불러옵니다...");
                        MovieListView movieListView = new MovieListView();
                        // 여기서 MovieListView가 MenuManger 인터페이스를 구현하면 execute() 호출 가능
                        break;
                    case "2":
                        // 영화 검색
                        System.out.println("영화 검색 화면으로 이동합니다...");
                        MovieSearchView movieSearchView = new MovieSearchView();
                        break;
                    case "3":
                        // 영화 예매
                        System.out.println("영화 예매 화면으로 이동합니다...");
                        ReservationView reservationView = new ReservationView();
                        break;
                    case "4":
                        // 내 예매 내역
                        System.out.println("예매 내역을 확인합니다...");
                        MyReservationView myReservationView = new MyReservationView();
                        break;
                    case "5":
                        // 로그아웃하고 메인 화면으로 돌아가기
                        System.out.println("로그아웃 되었습니다.");
                        new MainView().execute();
                        return;
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