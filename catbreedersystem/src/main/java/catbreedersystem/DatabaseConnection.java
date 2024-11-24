package catbreedersystem;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final  String URL = "jdbc:mysql://localhost:3306/cat_breeder_system?useSSL=false";
    private static final String USER = "root";  // 替换为你的 MySQL 用户名
    private static final String PASSWORD = "0401LuckyDog!";  // 替换为你的 MySQL 密码

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
        }

        try (Connection connection = getConnection()) {
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database");
            e.printStackTrace();
        }
    }
}
