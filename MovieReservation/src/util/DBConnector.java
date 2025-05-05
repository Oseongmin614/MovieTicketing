package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 오라클 DB 연결 유틸리티 클래스
 */
public class DBConnector {
    // 오라클 드라이버 및 접속 정보 (환경에 맞게 수정)
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "movieticket"; 
    private static final String PASSWORD = "movieticketpw"; 

    // 드라이버 로드 (최초 1회)
    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("[DBConnector] 드라이버 로드 실패: " + e.getMessage());
        }
    }

    /**
     * DB 연결 객체 반환
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}