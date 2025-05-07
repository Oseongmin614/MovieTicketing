package ui.user;

import java.util.List;

import manager.MenuManger;
import service.ReservationService;
import ui.BaseView;
import vo.MovieVO;
import vo.ReservationVO;
import vo.SeatVO;
import vo.ShowSeatVO;
import vo.ShowVO;
import vo.UserVO;

/**
 * 영화 예매 화면 클래스
 * - 영화 선택, 상영 시간 선택, 좌석 선택 등 예매 과정 처리
 */
public class ReservationView extends BaseView implements MenuManger {
    
    private ReservationService reservationService;
    private UserVO currentUser;
    
    public ReservationView(UserVO user) {
        this.reservationService = new ReservationService();
        this.currentUser = user;
    }
    
    @Override
    public void execute() throws Exception {
        
        List<MovieVO> movies = reservationService.getAvailableMovies();
        
        if (movies.isEmpty()) {
            System.out.println("현재 예매 가능한 영화가 없습니다.");
            return;
        }
        
        // 영화 목록 출력
        displayMovieList(movies);
        
        // 영화 선택
        int movieChoice = selectMovie(movies);
        if (movieChoice == -1) return; // 취소 또는 잘못된 입력
        
        MovieVO selectedMovie = movies.get(movieChoice - 1);
        System.out.println("선택한 영화: " + selectedMovie.getTitle());
        
        // 상영 시간 목록 출력
        List<ShowVO> shows = selectedMovie.getShowList();
        if (shows == null || shows.isEmpty()) {
            System.out.println("선택한 영화의 상영 시간이 없습니다.");
            return;
        }
        
        displayShowTimes(shows);
        
        //  상영 시간 선택
        int showChoice = selectShowTime(shows);
        if (showChoice == -1) return; // 취소 또는 잘못된 입력
        
        ShowVO selectedShow = shows.get(showChoice - 1);
        System.out.println("선택한 상영 시간: " + selectedShow.getShowTime());
        
        //  좌석 배치도 출력
        displaySeatMap(selectedShow);
        
        //  좌석 입력 및 유효성 검사
        String seatCode = selectSeat(selectedShow);
        if (seatCode == null) return; // 취소 또는 잘못된 입력
        
        //  예매 정보 저장, 완료 화면 출력, 좌석 갱신
        ReservationVO reservation = reservationService.makeReservation(
            currentUser.getUserNo(), 
            selectedShow.getShowId(), 
            getSeatIdFromCode(selectedShow, seatCode)
        );
        
        if (reservation != null) {
            displayReservationComplete(reservation, selectedMovie, selectedShow, seatCode);
        } else {
            System.out.println("예매 처리 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 영화 목록 출력
     */
    private void displayMovieList(List<MovieVO> movies) {
        System.out.println("\n===== 예매 가능 영화 목록 =====");
        for (int i = 0; i < movies.size(); i++) {
            MovieVO movie = movies.get(i);
            System.out.printf("%d. %s (%d분)\n", 
                i + 1, movie.getTitle(), movie.getDurationMin());
        }
        System.out.println("==============================");
    }
    
    /**
     * 영화 선택
     */
    private int selectMovie(List<MovieVO> movies) {
        try {
            int choice = scanInt("예매할 영화 번호를 선택하세요 (0: 취소): ");
            
            //  예매 중단 기능
            if (choice == 0) {
                System.out.println("예매를 취소하고 메인으로 돌아갑니다.");
                return -1;
            }
            
            //  잘못된 입력 처리
            if (choice < 1 || choice > movies.size()) {
                System.out.println("잘못된 영화 번호입니다. 다시 선택해주세요.");
                return selectMovie(movies);
            }
            
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력 가능합니다. 다시 선택해주세요.");
            return selectMovie(movies);
        }
    }
    
    /**
     * 상영 시간 출력
     */
    private void displayShowTimes(List<ShowVO> shows) {
        System.out.println("\n===== 상영 시간 목록 =====");
        for (int i = 0; i < shows.size(); i++) {
            ShowVO show = shows.get(i);
            int availableSeats = reservationService.getAvailableSeatsCount(show);
            int totalSeats = reservationService.getTotalSeatsCount(show);
            System.out.printf("%d. %s (잔여: %d/%d석)\n", 
                i + 1, show.getShowTime(), availableSeats, totalSeats);
        }
        System.out.println("=========================");
    }
    
    /**
     * 상영 시간 선택
     */
    private int selectShowTime(List<ShowVO> shows) {
        try {
            int choice = scanInt("상영 시간을 선택하세요 (0: 취소): ");
            
            // 예매 중단 기능
            if (choice == 0) {
                System.out.println("예매를 취소하고 메인으로 돌아갑니다.");
                return -1;
            }
            
            // 잘못된 입력 처리
            if (choice < 1 || choice > shows.size()) {
                System.out.println("잘못된 상영 시간 번호입니다. 다시 선택해주세요.");
                return selectShowTime(shows);
            }
            
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력 가능합니다. 다시 선택해주세요.");
            return selectShowTime(shows);
        }
    }
    
    /**
     * 좌석 배치도 출력
     */
    private void displaySeatMap(ShowVO show) {
        System.out.println("\n===== 좌석 배치도 =====");
        System.out.println("  1 2 3 4 5 6 7");
        
        char rowStart = 'A';
        List<ShowSeatVO> seats = show.getShowSeatList();
        
        for (int row = 0; row < 7; row++) {
            System.out.print((char)(rowStart + row) + " ");
            
            for (int col = 1; col <= 7; col++) {
                String seatCode = (char)(rowStart + row) + String.valueOf(col);
                boolean isReserved = isSeatReserved(seats, seatCode);
                
                if (isReserved) {
                    System.out.print("X ");
                } else {
                    System.out.print("O ");
                }
            }
            System.out.println();
        }
        System.out.println("====================");
        System.out.println("O: 예매 가능, X: 예매됨");
    }
    
    /**
     * 좌석 코드로 예약 여부 확인
     */
    private boolean isSeatReserved(List<ShowSeatVO> seats, String seatCode) {
        for (ShowSeatVO seat : seats) {
            SeatVO seatInfo = reservationService.getSeatById(seat.getSeatId());
            if (seatInfo != null && seatCode.equals(seatInfo.getSeatCode())) {
                return seat.isReserved();
            }
        }
        return false;
    }
    
    /**
     * 좌석 선택
     */
    private String selectSeat(ShowVO show) {
        try {
            String seatCode = scanStr("예매할 좌석을 입력하세요 (예: A3, 0: 취소): ").toUpperCase();
            
            // 예매 중단 기능
            if (seatCode.equals("0")) {
                System.out.println("예매를 취소하고 메인으로 돌아갑니다.");
                return null;
            }
            
            // 좌석 유효성 검사
            if (!isValidSeatCode(seatCode)) {
                System.out.println("잘못된 좌석 코드입니다. 다시 입력해주세요.");
                return selectSeat(show);
            }
            
            // 좌석 예약 여부 확인
            if (isSeatReserved(show.getShowSeatList(), seatCode)) {
                System.out.println("이미 예약된 좌석입니다. 다른 좌석을 선택해주세요.");
                return selectSeat(show);
            }
            
            return seatCode;
        } catch (Exception e) {
            System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            return selectSeat(show);
        }
    }
    
    /**
     * 좌석 코드 유효성 검사
     */
    private boolean isValidSeatCode(String seatCode) {
        if (seatCode == null || seatCode.length() != 2) {
            return false;
        }
        
        char row = seatCode.charAt(0);
        char col = seatCode.charAt(1);
        
        return row >= 'A' && row <= 'G' && col >= '1' && col <= '7';
    }
    
    /**
     * 좌석 코드로 좌석 ID 찾기
     */
    private int getSeatIdFromCode(ShowVO show, String seatCode) {
        List<ShowSeatVO> seats = show.getShowSeatList();
        for (ShowSeatVO seat : seats) {
            SeatVO seatInfo = reservationService.getSeatById(seat.getSeatId());
            if (seatInfo != null && seatCode.equals(seatInfo.getSeatCode())) {
                return seat.getShowSeatId();
            }
        }
        return -1;
    }
    
    /**
     * 예매 완료 화면 출력
     */
    private void displayReservationComplete(ReservationVO reservation, MovieVO movie, ShowVO show, String seatCode) {
        System.out.println("\n================= 예매 완료 =================");
        System.out.println("예매 번호: " + reservation.getReservationId());
        System.out.println("영화 제목: " + movie.getTitle());
        System.out.println("상영 시간: " + show.getShowTime());
        System.out.println("좌석 번호: " + seatCode);
        System.out.println("예매자: " + currentUser.getName());
        System.out.println("예매 일시: " + reservation.getReservedDate());
        System.out.println("==================================================");
        System.out.println("\t예매가 완료되었습니다. 감사합니다.");
    }
}
