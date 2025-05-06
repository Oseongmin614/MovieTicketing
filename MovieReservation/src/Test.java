import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.Console;

public class Test {
    public static void main(String[] args) {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "movieticket";
        String password = "movieticketpw";

        try {
        	
        	
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT user_no, user_id, name FROM MT_USER")) {

                while (rs.next()) {
                    System.out.println("번호: " + rs.getInt("user_no")
                        + ", 아이디: " + rs.getString("user_id")
                        + ", 이름: " + rs.getString("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}