package utilityAndServices;
import java.util.HashMap;
import java.util.Map;
import learning.Course;
import java.util.ArrayList;
import java.util.List;
import user.User;
import java.util.Scanner;

public class ApplicationSite {
    // clasa serviciu de tip singletone
    private static ApplicationSite applicationSite; // a user can add buy a course/ post a course only if she is connected
    private List<User> userList = new ArrayList<>();
    protected static User connectedUser;
    Map <Course, User> publishedCourses = new HashMap<>();

    public void connectUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String userName = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        boolean userFound = false;
        for (User user : userList) {
            if (user.getUserName().equals(userName) && user.getPassword().equals(password)) {
                connectedUser = user;
                userFound = true;
                System.out.println("User " + userName + " connected.");
                return;
            }
        }
        if (!userFound) {
            System.out.println("User does not exist!");
        }
    }

    public void disconnectUser() {
        System.out.println("User " + connectedUser.getUserName() + " disconnected.");
        connectedUser = null;
    }

    public void signupUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter username:");
        String userName = scanner.nextLine();

        for (User user : userList) { // we ensure there isn't already a user with the same user name
            if (user.getUserName().equals(userName)) {
                System.out.println("Username must be unique");
                return;
            }
        }
        // to do: implement regex for password
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter last name:");
        String lastName = scanner.nextLine();
        System.out.println("Enter first name:");
        String firstName = scanner.nextLine();

        User user = new User(lastName,firstName, password, userName);
        userList.add(user);
        System.out.println("User " + userName + " assigned.");
    }
//    private ApplicationSite() {
//
//    }
    static {
        connectedUser = null; // initially there is no user connected
    }

    public static ApplicationSite getApplicationSite() {
        if (applicationSite == null)
            applicationSite = new ApplicationSite();
        return applicationSite;
    }

}
