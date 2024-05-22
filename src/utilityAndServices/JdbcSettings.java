package utilityAndServices;
import java.sql.*;
import payment.Card;
import java.util.Scanner;
import java.util.UUID;

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
        }
    }

    private static void createConnection() {
        try {
            connection = DriverManager.getConnection(jdbcURL, "Adela", "83383");
        }
        catch (Exception e) {
            System.out.println("Connection creation failed\n");
        }
    }

    static {
        registerDriver();
        createConnection();
    }
    public Connection getConnection() {
        return connection;
    }

    public void updateSubjectJDBC(String name, String description) {
        String queryText = "update Subject set description=? where name=?";
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, description);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("Subject update failed" + se.toString());
        }
    }

    public void deleteSubjectJDBC(String name) {
        String queryText = "delete from Subject where name=?";
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("Subject deletion failed" + se.toString());
        }
    }

    public void deleteUserJDBC(User user) {
        String queryText = "delete from User where userName=?";
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, user.getUserName());
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("User deletion failed" + se.toString());
        }
    }

    public void showAllSubjectsJDBC() throws SQLException {
        String queryText = "select * from Subject";
        try(Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryText);
            while (rs.next())
                System.out.println(rs.getString("name") + " description: " + rs.getString("description"));
        }
        catch (SQLException se) {
            System.err.println("Subjects display failed" + se.toString());
        }
    }
    public void crudSubjects(User connectedUser) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Add a subject 2.Update description 3.Delete a subject (it will also dissapear from the map(course, subject)\n");
        int command = scanner.nextInt();
        ApplicationSite A = ApplicationSite.getApplicationSite();

        switch (command) {
            case 1:
                Subject obj = new Subject("", "");
                obj.read();
                A.addSubject(obj);
                obj.createJDBC(connection);
                break;
            case 2:
                System.out.println("Those are the subjects: \n");
                showAllSubjectsJDBC();
                System.out.println("Write the name of the subject you want to edit: ");
                String name;
                name = scanner.next();
                System.out.println("\nWrite the new description: ");
                String description = scanner.next();
                A.updateSubjectDescription(name, description);
                updateSubjectJDBC(name, description);
                break;
            case 3:
                System.out.println("Those are the subjects: \n");
                A.displaySubjects();
                String nume;
                System.out.println("Write the name of the subject you want to delete: ");
                nume = scanner.next();
                A.deleteSubject(nume);
                deleteSubjectJDBC(nume);
                break;
            default:
        }
    }

    public void updateUserJDBC(User connectedUser, String newPassword) {
        String queryText = "update User set password=? where userName=?";
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, connectedUser.getUserName());
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("Password update failed" + se.toString());
        }
    }
    public void crudUsers(User connectedUser) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1.Change password 2.Delete account\n");
        int command = scanner.nextInt();
        ApplicationSite A = ApplicationSite.getApplicationSite();

        switch (command) {
            case 1:
                System.out.println("\nWrite the curret password: ");
                String password = scanner.next();
                if (!password.equalsIgnoreCase(connectedUser.getPassword())) {
                    System.out.println("\nWrong password! ");
                    return;
                }
                else {
                    System.out.println("\nWrite the new password: ");
                    String newPassword = scanner.next();
                    connectedUser.setPassword(newPassword);
                    updateUserJDBC(connectedUser, newPassword);
                }

                break;
            case 2:
                System.out.println("Deleting your account is permanent and you will loose the learning paths completed.");
                System.out.println("If you're also a teacher, you will give up the earnings from course selling.\n Are you sure you want to continue? yes/no");
                String choice;
                choice = scanner.next();
                if (choice.equalsIgnoreCase("yes")) {
                    A.deleteUser(connectedUser);
                    deleteUserJDBC(connectedUser);
                }
                break;
            default:
        }


    }

    public void deleteCardJDBC(Card cardToDelete, User connectedUser) {
        String queryText = "delete from UserHasCard where userId=? and cardId=?";
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, (connectedUser.getIdUser()).toString());
            pstmt.setString(2, (cardToDelete.getIdCard()).toString());
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("Card deletion failed" + se.toString());
        }

        //now we delete the row from Card table
        queryText = "delete from Card where id=?";
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, (cardToDelete.getIdCard()).toString());
            pstmt.executeUpdate();
        }
        catch (SQLException se) {
            System.err.println("Card deletion failed" + se.toString());
        }
    }
    public void findUsers(User connectedUser) throws SQLException {
        String queryText = "select * from User where userName=?";
        System.out.println("Enter the user name: ");
        Scanner scanner = new Scanner(System.in);
        String user = scanner.next();
        try(PreparedStatement pstmt = connection.prepareStatement(queryText)) {
            pstmt.setString(1, user);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String lastName = rs.getString("lastName");
                boolean administrator = rs.getBoolean("administrator");
                String firstName = rs.getString("firstName");
                String password = rs.getString("password");
                String userName = rs.getString("userName");

                User u = new User(id, lastName, administrator, firstName, password, userName);
                System.out.println("This is the user: \n" + u);
            } else {
                System.out.println("User doesn't exist.");
            }
        }
        catch (SQLException se) {
            System.err.println("User search failed" + se.toString());
        }
    }
}