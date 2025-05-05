import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.PasswordHasher;

public class Test {

	public static void main(String[] args) {
		String url = "jdbc:oracle:thin:@localhost:1521:xe"; // DB 주소
        String user = "movieticket"; // DB 사용자명
        String password = "movieticketpw"; // DB 비밀번호
        String hash = PasswordHasher.hash("user123!");
        System.out.println(hash); 

        try {
            // 오라클 드라이버 로딩 (필요시)
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // DB 연결 시도
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("DB 연결 성공!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException JDBC 드라이버 로딩 실패: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
        }

	}

}
