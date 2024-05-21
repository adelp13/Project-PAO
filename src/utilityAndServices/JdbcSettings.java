package utilityAndServices;
import java.sql.*;
import utilityAndServices.crudInterface;
import java.util.Scanner;
import learning.Subject;
import user.User;

public class JdbcSettings {
    private static Connection connection = null;
    private final static String jdbcURL = "jdbc:mysql://localhost:3306/Proiect_PAO";
    private static JdbcSettings jdbcSettings;

    public static JdbcSettings getJdbcSettings() {
        if (jdbcSettings == null) {
            jdbcSettings = new JdbcSettings();
        }
        return jdbcSettings;
    }

    private static void registerDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (Exception e) {
            System.out.println("driver registration failed\n");
            e.printStackTrace();
        }
    }

    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(jdbcURL, "Adela", "83383");
        }
        catch (Exception e) {
            System.out.println("ERROR :: Setup createConnection :: could not create connection\n");
            e.printStackTrace();
        }
    }

    static {
        registerDriver();
        createConnection();
    }
    public static Connection getConnection() {
        return connection;
    }

    public void crudSubjects(User connectedUser) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Add a subject 2.Update a subject 3.Delete a subject (it will also dissapear from the map(course, subject)\n");
        int command = scanner.nextInt();

        switch (command) {
            case 1:
                Subject obj = new Subject("", "");
                obj.read();
                obj.createJDBC(connection);
                break;
            default:
        }
    }
}