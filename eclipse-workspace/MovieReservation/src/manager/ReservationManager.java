package manager;

import java.util.List;

import service.ReservationService;
import vo.MovieVO;
import vo.ReservationVO;
import vo.UserVO;

/**
 * 예매 관련 기능 관리 클래스
 * - View와 Service 사이의 통신 관리
 * - 복합적인 작업 처리
 */
public class ReservationManager {
    private ReservationService reservationService;
    
    public ReservationManager() {
        this.reservationService = new ReservationService();
    }
    
    /**
     * 현재 예매 가능한 영화 목록 조회
     */
    public List<MovieVO> getAvailableMovies() {
        return reservationService.getAvailableMovies();
    }
    
    /**
     * 예매 처리
     */
    public ReservationVO processReservation(UserVO user, int showId, int showSeatId) {
        if (user == null) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return null;
        }
        
        return reservationService.makeReservation(user.getUserNo(), showId, showSeatId);
    }
    
    /**
     * 사용자 예매 내역 조회
     */
    public List<ReservationVO> getUserReservations(UserVO user) {
        if (user == null) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return null;
        }
        
        return reservationService.getUserReservations(user.getUserNo());
    }
    
    /**
     * 예매 취소 처리
     */
    public boolean cancelReservation(int reservationId, UserVO user) {
        if (user == null) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return false;
        }
        
        // 해당 예매가 현재 사용자의 것인지 확인 (보안)
        ReservationVO reservation = reservationService.getUserReservations(user.getUserNo())
                .stream()
                .filter(r -> r.getReservationId() == reservationId)
                .findFirst()
                .orElse(null);
        
        if (reservation == null) {
            System.out.println("해당 예매 내역을 찾을 수 없거나 권한이 없습니다.");
            return false;
        }
        
        return reservationService.cancelReservation(reservationId);
    }
}
