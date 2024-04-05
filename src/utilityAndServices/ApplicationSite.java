package utilityAndServices;
import java.util.HashMap;
import java.util.Map;
import learning.Course;
import learning.AccreditedCourse;
import java.util.ArrayList;
import java.util.List;
import user.User;
import learning.Subject;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import learning.Difficulty;
import learning.Level;
import learning.Quiz;
import payment.Card;

public class ApplicationSite {
    // clasa serviciu de tip singletone
    private static ApplicationSite applicationSite; // a user can add buy a course/ post a course only if she is connected
    protected List<User> userList = new ArrayList<>();
    protected  List<Course> courseList = new ArrayList<>();
    protected static User connectedUser; // other operations can t be made only if this variable is not null; a user has to be connected
    protected Set<Subject> subjectSet = new HashSet<>();
    Map<Course, Set<Subject>> subjectsForCourses = new HashMap<>(); // we have polimorphism because there are 2 types of courses

    private ApplicationSite() {
    }
    static {
        connectedUser = null; // initially there is no user connected
    }

    public static ApplicationSite getApplicationSite() {
        if (applicationSite == null)
            applicationSite = new ApplicationSite();
        return applicationSite;
    }

    public void showCoursesStarted() {
        Map<Course, Map<Quiz, String>> map = connectedUser.getCourseProgress();
        System.out.println("Your courses started are: ");
        map.forEach((course, map2) -> {
            int quizesTried = map2.size();
            System.out.println(course + "You have tried " + quizesTried + " quizes out of " + course.getQuizList().size());
        });

        System.out.println("If you want to do quizes in a course, write course name. Else write exit ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (!command.equalsIgnoreCase("exit")) {
            map.forEach((course, map2) -> {
                if (course.getName().equalsIgnoreCase(command)) {
                    for (Quiz quiz : course.getQuizList()) { // we got through the quizes of the course and if a quiz is not in map2 it means the user hasn't tried the quiz
                        if (map2.containsKey(quiz)) {
                            System.out.println(quiz + "You scored " + map2.get(quiz) + " points.");
                        }
                        else {
                            System.out.println(quiz + "You haven't tried this quiz yet.");
                        }
                    }
                }
                boolean userTriesQuizes = true;

                while(userTriesQuizes) {
                    System.out.println("Enter the name of the quiz you want to try, or exit.");
                    String c = scanner.nextLine();
                    if (c.equals("exit")){
                        userTriesQuizes = false;
                        showCoursesStarted();
                        return;
                    }
                    else {
                        for (Quiz quiz : course.getQuizList()) {
                            if (quiz.getName().equals(c)) {
                                double score = quiz.runQuiz();// function that displays the questions and calculate your score
                                map2.put(quiz, String.valueOf(score)); // we add in the quizes result collection of the course key
                                System.out.println("Your score for quiz " + quiz.getName() + " has been recorded.");
                            }
                        }
                    }
                }
            });
        }
    }
    public void buyCourse() { // the connected user can buy a course
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter course name:");
        String name = scanner.nextLine();

        for (Course course : courseList) {
            if (course.getName().equals(name)) {
                if (courseAlreadyEnrolled(course)) {
                    System.out.println("You already enrolled in this course!");
                    return;
                }
                System.out.println("This course costs " + course.getPrice() + " euro.");
                manageCardsToBuy(course); // we display the cards and choose one to pay
                break;
            }
        }
    }

    public boolean courseAlreadyEnrolled(Course course) {
        for (Course enrolledCourse : (connectedUser.getCourseProgress()).keySet()) { // we go through the courses in which the user is enrolled
            if (enrolledCourse.equals(course)) {
                return true;
            }
        }

        return false;
    }
    public void manageCardsToBuy(Course course) { // we display the cards and choose one to pay
        displayCardsOfConnectedUser();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to add a card? (y/n)");
        String command = scanner.nextLine();
        if (command.equalsIgnoreCase("y")) {
            connectedUser.addCard();
            Card card = (connectedUser.getCardList()).getLast(); // we get the new card and display it
            System.out.println( (connectedUser.getCardList()).size() + ". " + card);
        }
        else {
            if ( (connectedUser.getCardList()).isEmpty()) {
                System.out.println("You have no cards. 1.add a card 2. exit");
                int c = scanner.nextInt();
                scanner.nextLine();
                if (c == 1) {
                    connectedUser.addCard();
                    Card card = (connectedUser.getCardList()).getLast(); // we get the new card and display it
                    System.out.println( (connectedUser.getCardList()).size() + ". " + card);
                }
                else {
                    System.out.println("Payment can t be made.");
                    return;
                }
            }
        }
        System.out.println("Select a card to pay. (enter index)");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (connectedUser.payWithCard( (connectedUser.getCardList()).get(index - 1) )) {
            System.out.println("Payment succsefull!");
            // we enroll the user to the course:
            connectedUser.enrollToCourse(course);
        }
        else {
            System.out.println("Payment not succsefull");
            manageCardsToBuy(course); // we display again the cards with the possibility of adding more
        };
    }
    public void displayCardsOfConnectedUser() {
        int i = 0;
        if (connectedUser.getCardList() == null) {
            System.out.println("You don t have any cards.");
        }
        System.out.println("Those are your cards:");
        for (Card card : connectedUser.getCardList()){
            System.out.println(i + ". " + card);
            i += 1;
        }
    }
    public void displaySubjects() {
        for (Subject subject : subjectSet) {
            System.out.println(subject);
        }
    }

    public void showAllCourses() {
        System.out.println("These are the courses:");
        for (Course course : courseList) {
            System.out.println(course);
        }
    }
    public void addCourse() { // a registered user can add a course as a teacher
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter course name:");
        String name = scanner.nextLine();

        // to do: request to enter quizes and question

        System.out.println("This is the list of subjects:");
        displaySubjects();
        System.out.println("Choose at least one subject (write their names):");
        String[] chosenSubjects = scanner.nextLine().split("[,\\s]+");
        Set<Subject> courseSubjects = new HashSet<>();

        System.out.println("Enter course difficulty:");
        Difficulty difficulty = Difficulty.valueOf(scanner.nextLine());
        for (Subject subject : subjectSet) {
            for (String subjectName : chosenSubjects)  {
                if (subject.getName().equalsIgnoreCase(subjectName)) {
                    courseSubjects.add(subject);
                    break;
                }
            }
        }
        System.out.println("You have added " + courseSubjects.size() + " subjects.");

        System.out.println("Enter course length (in hours):");
        double length = scanner.nextDouble();
        scanner.nextLine();

        Course newCourse;
        System.out.println("Is the course accredited? (y/n)");
        String isAccredited = scanner.nextLine();

        System.out.println("Enter course price:");
        double price = scanner.nextDouble();
        scanner.nextLine();

        if (isAccredited.equals("y")) {
            System.out.println("Enter accreditation period (in years):");
            int accreditationPeriod = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter accreditation level:");
            Level accreditationLevel = Level.valueOf(scanner.nextLine());

            newCourse = new AccreditedCourse(price, name, difficulty, null, length, connectedUser, accreditationPeriod, accreditationLevel);
        }
        else {
            newCourse = new Course(price, name, difficulty, null, length, connectedUser);
        }

        courseList.add(newCourse);
        subjectsForCourses.put(newCourse, courseSubjects); // we add in the map the course with its subjects

        System.out.println("Course has been added successfully");
    }

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
        System.out.println("User " + userName + " assigned on " + user.getRegistrationDate());
    }

}
