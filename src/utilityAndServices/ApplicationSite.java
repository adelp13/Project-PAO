package utilityAndServices;
import java.sql.*;
import java.util.*;

import learning.Course;
import learning.AccreditedCourse;
import user.User;
import learning.Subject;
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

    public List<User> getUserList() {
        return userList;
    }
    public static ApplicationSite getApplicationSite() {
        if (applicationSite == null)
            applicationSite = new ApplicationSite();
        return applicationSite;
    }

    public User getConnectedUser() {
        return connectedUser;
    }
    public void showSubjectsSorted() {
        List<Subject> subjectsList = new ArrayList<>(subjectSet);// a set cannot be directly sorted; it does not retain the order of elements
        Collections.sort(subjectsList); // using comparable implementation of interface
        this.subjectSet = new LinkedHashSet<>(subjectsList); //linked set maintains the order
        for (Subject subject : subjectSet) {
            System.out.println(subject);
        }
    }

    public void loadFromDatabase(Connection connection) {
        //we load the subjects in the set
        String queryText = "select * from Subject";
        try(Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryText);
            while (rs.next()) {
                Subject s = new Subject(UUID.fromString(rs.getString("id")), rs.getString("name"), rs.getString("description"));
                subjectSet.add(s);
            }
        }
        catch (SQLException se) {
            System.err.println("Subjects loading in RAM failed" + se.toString());
        }
        //we load the users in the list
        queryText = "select * from User";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryText);
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String lastName = rs.getString("lastName");
                boolean administrator = rs.getBoolean("administrator");
                String firstName = rs.getString("firstName");
                String password = rs.getString("password");
                String userName = rs.getString("userName");
                User user = new User(id, lastName, administrator, firstName, password, userName);
                userList.add(user);
            }
        } catch (SQLException se) {
            System.err.println("Users loading in RAM failed: " + se.toString());
        }

        //we load in the User.cardsList infos from userHasCard andCard
        queryText = "select * from UserHasCard";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(queryText);
            while (rs.next()) {
                String userId = rs.getString("userId");
                String cardId = rs.getString("cardId");
                String queryText2 = "select * from Card where id=?";
                try (PreparedStatement pstmt = connection.prepareStatement(queryText2)) {
                    pstmt.setString(1, cardId);
                    ResultSet rs2 = pstmt.executeQuery();
                    if (rs2.next()) {
                        String cardNo = rs2.getString("cardNo");
                        String nameOnCard = rs2.getString("nameOnCard");
                        String expirationDate = rs2.getString("expirationDate");
                        int CVV = rs2.getInt("CVV");
                        Card cardFound = new Card(UUID.fromString(cardId), cardNo, nameOnCard, expirationDate, CVV);
                        for (User user : userList) {
                            if (((user.getIdUser()).toString()).equals(userId)) {
                                (user.getCardList()).add(cardFound); // we add to the user the card pair from table UserHasCard
                                break;
                            }
                        }
                    }
                }
                catch (SQLException se) {
                    System.err.println("Card not loaded: " + se.toString());
                }
            }
        } catch (SQLException se) {
            System.err.println("Cards not loaded: " + se.toString());
        }


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
    public void buyCourse() throws SQLException { // the connected user can buy a course
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
    public void manageCardsToBuy(Course course) throws SQLException { // we display the cards and choose one to pay
        displayCardsOfConnectedUser();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Do you want to delete a card? yes/no");
        String command = scanner.nextLine();
        if (command.equalsIgnoreCase("yes")) {
            System.out.println("Select the card you want to delete. (enter index)");
            int index = scanner.nextInt();
            index -= 1;
            JdbcSettings J = JdbcSettings.getJdbcSettings();
            J.deleteCardJDBC((connectedUser.getCardList()).get(index), connectedUser);
            (connectedUser.getCardList()).remove((connectedUser.getCardList()).get(index)); // delete from user.cardList
        }

        displayCardsOfConnectedUser();
        System.out.println("Do you want to add a card? (y/n)");
        command = scanner.nextLine();
        if (command.equalsIgnoreCase("y")) {
            connectedUser.addCard();
            Card card = (connectedUser.getCardList()).getLast(); // we get the new card and display it
            System.out.println( (connectedUser.getCardList()).size() + ". " + card);
        }
        else {
            if ( (connectedUser.getCardList()).isEmpty())  {
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
            i += 1;
            System.out.println(i + ". " + card);
        }
    }
    public void displaySubjects() {
        for (Subject subject : subjectSet) {
            System.out.println(subject);
        }
    }
    public void deleteSubject(String name){
        for (Subject subject : subjectSet) {
            if (subject.getName().equalsIgnoreCase(name)) {
                subjectSet.remove(subject);
                break;
            }
        }
    }

    public void deleteUser(User user){
        connectedUser = null;
        //before deleting it, we have to mark as null the references in the course object to this user
        for (Course course : courseList) {
            if (course.getTeacher().equals(user)) {
                course.setTeacher(null);
            }
        }
        userList.remove(user);
    }
    public void addSubject(Subject subject) {
        subjectSet.add(subject);
    }
    public void updateSubjectDescription(String subjectName, String newDescription) {
        for (Subject subject : subjectSet) {
            if (subject.getName().equalsIgnoreCase(subjectName)) {
                subject.setDescription(newDescription);
                break;
            }
        }
    }
    public void showAllCourses() {
        Collections.sort(courseList); // we use the comparable interface override
        System.out.println("These are the courses:");
        for (Course course : courseList) {
            //we will also find the subjects for the course in the map where the Course is the Key and the value a set of its subjects
            StringBuilder courseSubjectsNames = new StringBuilder("Subjects of the course: "); // we use string builder because it is more efficient than to make all the concatenations
            for (Subject subject : this.subjectsForCourses.get(course)) {
                courseSubjectsNames.append(subject.getName()).append(", ");
            };
            if (!courseSubjectsNames.isEmpty()) {
                courseSubjectsNames.setLength(courseSubjectsNames.length() - 2); // we get rid of the last comma
            }
            System.out.println(course + courseSubjectsNames.toString());
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
        System.out.println("Enter username (ex: mariap):");
        String userName = scanner.nextLine();
        System.out.println("Enter password (1234 for mariap):");
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
        if (userFound == false) {
            System.out.println("User does not exist!");
        }
    }

    public void disconnectUser() {
        System.out.println("User " + connectedUser.getUserName() + " disconnected.");
        connectedUser = null;
    }

    public void signupUser() throws SQLException {
        User obj = new User();
        obj.read();
        userList.add(obj);
        System.out.println("User " + obj.getUserName() + " assigned on " + obj.getRegistrationDate());

        //JDBC part
        JdbcSettings J = JdbcSettings.getJdbcSettings();
        obj.createJDBC(J.getConnection());
    }

}
