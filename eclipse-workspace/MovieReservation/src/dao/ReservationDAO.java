package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import util.DBConnector;
import vo.ReservationVO;
import vo.ShowSeatVO;

/**
 * 예매 관련 데이터 액세스 객체
 */
public class ReservationDAO implements BaseDAO<ReservationVO, Integer> {
    // 싱글톤 패턴 구현
    private static ReservationDAO instance;
    
    public ReservationDAO() {}
    
    public static synchronized ReservationDAO getInstance() {
        if (instance == null) {
            instance = new ReservationDAO();
        }
        return instance;
    }
    
    // SQL 쿼리 상수
    private static final String SELECT_ALL_RESERVATIONS = "SELECT * FROM RESERVATION";
    private static final String SELECT_RESERVATION_BY_ID = "SELECT * FROM RESERVATION WHERE RESERVATION_ID = ?";
    private static final String SELECT_RESERVATIONS_BY_USER = "SELECT * FROM RESERVATION WHERE USER_NO = ? ORDER BY RESERVED_DATE DESC";
    private static final String INSERT_RESERVATION = 
            "INSERT INTO RESERVATION (RESERVATION_ID, USER_NO, SHOW_ID, SHOW_SEAT_ID, RESERVED_DATE, STATUS) " +
            "VALUES (RESERVATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";
    private static final String UPDATE_RESERVATION_STATUS = 
            "UPDATE RESERVATION SET STATUS = ? WHERE RESERVATION_ID = ?";
    private static final String SELECT_SHOW_SEAT_BY_ID = "SELECT * FROM SHOW_SEAT WHERE SHOW_SEAT_ID = ?";
    private static final String UPDATE_SHOW_SEAT = "UPDATE SHOW_SEAT SET IS_RESERVED = ? WHERE SHOW_SEAT_ID = ?";
    
    @Override
    public List<ReservationVO> findAll() {
        List<ReservationVO> reservations = new ArrayList<>();
        
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_RESERVATIONS)) {
            
            while (rs.next()) {
                reservations.add(extractReservationFromResultSet(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    @Override
    public ReservationVO findById(Integer id) {
        ReservationVO reservation = null;
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_RESERVATION_BY_ID)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    reservation = extractReservationFromResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservation;
    }
    
    @Override
    public ReservationVO save(ReservationVO entity) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_RESERVATION, new String[]{"RESERVATION_ID"})) {
            
            ps.setInt(1, entity.getUserNo());
            ps.setInt(2, entity.getShowId());
            ps.setInt(3, entity.getShowSeatId());
            ps.setTimestamp(4, new Timestamp(entity.getReservedDate().getTime()));
            ps.setString(5, entity.getStatus() != null ? entity.getStatus() : "ACTIVE");
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setReservationId(rs.getInt(1));
                        updateSeatStatus(conn, entity.getShowSeatId(), "Y");
                        return entity;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public ReservationVO update(ReservationVO entity) {
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE RESERVATION SET USER_NO = ?, SHOW_ID = ?, SHOW_SEAT_ID = ?, STATUS = ? WHERE RESERVATION_ID = ?")) {
            
            ps.setInt(1, entity.getUserNo());
            ps.setInt(2, entity.getShowId());
            ps.setInt(3, entity.getShowSeatId());
            ps.setString(4, entity.getStatus());
            ps.setInt(5, entity.getReservationId());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                return entity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public void delete(Integer id) {
        cancelReservation(id);
    }
    
    /**
     * 예약 취소
     */
    public boolean cancelReservation(int reservationId) {
        String sql = "UPDATE RESERVATION SET STATUS = 'CANCELED' WHERE RESERVATION_ID = ?";
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, reservationId);
            ReservationVO reservation = findById(reservationId);
            
            if (reservation != null) {
                int result = ps.executeUpdate();
                
                if (result > 0) {
                    return updateSeatStatus(conn, reservation.getShowSeatId(), "N");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    private boolean updateSeatStatus(Connection conn, int showSeatId, String isReserved) {
        String sql = "UPDATE SHOW_SEAT SET IS_RESERVED = ? WHERE SHOW_SEAT_ID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isReserved);
            ps.setInt(2, showSeatId);
            
            int result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 기타 필요한 메소드들은 그대로 유지합니다...
    
    public List<ReservationVO> findByUserNo(int userNo) {
        // 기존 구현 그대로 유지
        List<ReservationVO> reservations = new ArrayList<>();
        
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_RESERVATIONS_BY_USER)) {
            
            ps.setInt(1, userNo);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reservations.add(extractReservationFromResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    private ReservationVO extractReservationFromResultSet(ResultSet rs) throws SQLException {
        ReservationVO reservation = new ReservationVO();
        reservation.setReservationId(rs.getInt("RESERVATION_ID"));
        reservation.setUserNo(rs.getInt("USER_NO"));
        reservation.setShowId(rs.getInt("SHOW_ID"));
        reservation.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
        reservation.setReservedDate(rs.getTimestamp("RESERVED_DATE"));
        reservation.setStatus(rs.getString("STATUS"));
        return reservation;
    }
}
