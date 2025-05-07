package dao;

import util.DBConnector;
import vo.ShowSeatVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowSeatDAO {

    private static final String SELECT_SHOW_SEATS = 
            "SELECT ss.*, s.ROW_LABEL, s.COL_NUM, s.SEAT_CODE " +
            "FROM SHOW_SEAT ss JOIN SEAT s ON ss.SEAT_ID = s.SEAT_ID " +
            "WHERE ss.SHOW_ID = ?";
    
 
    public static List<ShowSeatVO> selectShowSeatsByShowId(int showId) {
        List<ShowSeatVO> seats = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SHOW_SEATS)) {
            
            ps.setInt(1, showId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShowSeatVO seat = new ShowSeatVO();
                    seat.setShowSeatId(rs.getInt("SHOW_SEAT_ID"));
                    seat.setShowId(rs.getInt("SHOW_ID"));
                    seat.setSeatId(rs.getInt("SEAT_ID"));
                    seat.setReserved(rs.getBoolean("IS_RESERVED"));
                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }
}
