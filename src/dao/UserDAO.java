package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.DBConnector;
import vo.UserVO;

public class UserDAO {
	private static UserDAO instance;

	private UserDAO() {
	}

	public static synchronized UserDAO getInstance() {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	public UserVO findByUserId(String userId) {
		String sql = "SELECT * FROM MT_USER WHERE user_id = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

	public UserVO save(UserVO user) {
		String sql = "INSERT INTO MT_USER (user_no, user_id, password, name, role, reg_date) VALUES (MT_USER_SEQ.nextval, ?, STANDARD_HASH(?, 'SHA256'), ?, ?, SYSDATE)";
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "user_no" })) {

			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getName());
			pstmt.setString(4, user.getRole());

			int affectedRows = pstmt.executeUpdate();

			if (affectedRows > 0) {
				try (ResultSet rs = pstmt.getGeneratedKeys()) {
					if (rs.next()) {
						user.setUserNo(rs.getInt(1));
						return user;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean login(String userId, String password) {
		String sql = "SELECT 1 " + "FROM MT_USER " + "WHERE user_id = ? " + "AND password = STANDARD_HASH(?, 'SHA256')";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, userId);
			pstmt.setString(2, password);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getRole(String userId) {
		String sql = "SELECT role FROM MT_USER WHERE user_id = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? rs.getString("role") : null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean deleteByUserId(String userId) {
		String sql = "DELETE FROM MT_USER WHERE user_id = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<UserVO> findAll() {
		String sql = "SELECT * FROM MT_USER ORDER BY user_no DESC";
		List<UserVO> users = new ArrayList<>();
		try (Connection conn = DBConnector.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				UserVO user = new UserVO();
				user.setUserNo(rs.getInt("user_no"));
				user.setUserId(rs.getString("user_id"));
				user.setPassword(rs.getString("password"));
				user.setName(rs.getString("name"));
				user.setRole(rs.getString("role"));
				user.setRegDate(rs.getDate("reg_date"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public boolean updateUser(UserVO user) {
		String sql = "UPDATE MT_USER SET name = ?, role = ? WHERE user_id = ?";
		try (Connection conn = DBConnector.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getRole());
			pstmt.setString(3, user.getUserId());
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}