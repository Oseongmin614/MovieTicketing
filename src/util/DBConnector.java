package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnector {

	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	private static final String USER = "movieticket";
	private static final String PASSWORD = "movieticketpw";

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("[DBConnector] 드라이버 로드 실패: " + e.getMessage());
		}
	}

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static void close(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			System.out.println("[DBConnector] ResultSet 닫기 실패: " + e.getMessage());
		}

		try {
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			System.out.println("[DBConnector] PreparedStatement 닫기 실패: " + e.getMessage());
		}

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("[DBConnector] Connection 닫기 실패: " + e.getMessage());
		}
	}
}