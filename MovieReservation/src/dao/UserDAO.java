package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnector;
import util.PasswordHasher;
import vo.UserVO;

/**
 * 사용자 데이터베이스 접근 클래스
 */
public class UserDAO extends BaseDAO {
    // 싱글톤 패턴 구현
    private static UserDAO instance;

    private UserDAO() {}

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    /**
     * 로그인 검증
     * @param userId 아이디
     * @param password 비밀번호(평문)
     * @return 로그인 성공 시 true, 실패 시 false
     */
    public boolean login(String userId, String password) {
        String sql = "SELECT 1 FROM MT_USER WHERE user_id = ? AND password = STANDARD_HASH(?, 'SHA256')";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 결과 있으면 true
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserVO getUserByUserId(String userId) {
        String sql = "SELECT * FROM MT_USER WHERE user_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserVO user = new UserVO();
                    user.setUserNo(rs.getInt("user_no"));
                    user.setUserId(rs.getString("user_id"));
                    user.setPassword(rs.getString("password"));
                    user.setName(rs.getString("name"));
                    user.setRole(rs.getString("role"));
                    user.setRegDate(rs.getDate("reg_date"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 역할 조회
     */
    public String getRole(String userId) {
        String sql = "SELECT role FROM MT_USER WHERE user_id = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getString("role") : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

	
}
