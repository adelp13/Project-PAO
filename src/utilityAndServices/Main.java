package utilityAndServices;
import java.sql.SQLException;
import java.util.Scanner;
import user.User;
import learning.Subject;
import learning.Course;
import learning.AccreditedCourse;
import learning.Difficulty;
import learning.Level;
import java.util.Set;
import java.util.List;
import learning.Quiz;
import learning.Question;


public class Main {
    public static void main(String[] args) throws SQLException {
        ApplicationSite S = ApplicationSite.getApplicationSite();
        JdbcSettings J = JdbcSettings.getJdbcSettings();
        AuditMeniu A = AuditMeniu.getAuditMeniu();
        S.loadFromDatabase(J.getConnection()); // we load the info from the mysql database into sets, maps etc of the Application Site
        // we add some users
        User u1 = new User("Popescu", true, "Maria", "1234", "mariap");
        User u2 = new User("Paraschiv", false, "Andrei", "1234", "andreip");
        User u3  = new User("Iliescu", false, "Andreea", "1234", "andreeai");
//        S.userList.add(u1);
//        S.userList.add(u2);
//        S.userList.add(u3);

        // and some subjects
        Subject s1 = new Subject("c++", "Basic C++");
        Subject s2 = new Subject("java", "Introduction to java");
        Subject s3 = new Subject("biology", "advanced biology");
//        S.subjectSet.add(s1);
//        S.subjectSet.add(s2);
//        S.subjectSet.add(s3);

        // we add courses with their teacher (an user already added)
        Course c1 = new Course(10.0, "Course 1", Difficulty.EASY, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2))),
                new Quiz("Quiz 2", Difficulty.MEDIUM,List.of(new Question("2+1", "3", 2), new Question("1+1", "2", 3)))), 10.0, S.userList.getFirst());
        Course c2 = new Course(20.0, "Course 2", Difficulty.MEDIUM, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 15.0, S.userList.get(1));
        Course c3 = new Course(30.0, "Course 3", Difficulty.HARD, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 30.5, S.userList.get(2));
        Course c4 = new AccreditedCourse(15.0, "Course 4", Difficulty.HARD, List.of(
                new Quiz("Quiz 1",Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 42.5, S.userList.get(2), 10, Level.ADVANCED);
        Course c5 = new AccreditedCourse(18.0, "Course 5", Difficulty.EASY, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 13.5, S.userList.getFirst(), 5, Level.BASIC);
        S.courseList.add(c2);
        S.courseList.add(c1);
        S.courseList.add(c3);
        S.courseList.add(c4);
        S.courseList.add(c5);

        // we add in the map for each course its subjects:
        Set<Subject> subjects = Set.of(s1); //immutable set
        S.subjectsForCourses.put(c1, subjects);
        S.subjectsForCourses.put(c2, subjects);
        Set<Subject> subjects2 = Set.of(s1, s2);
        S.subjectsForCourses.put(c3, subjects2);
        S.subjectsForCourses.put(c4, subjects2);
        Set<Subject> subjects3 = Set.of(s1, s3);
        S.subjectsForCourses.put(c5, subjects3);

        Scanner scanner = new Scanner(System.in);
        while (true) { // the menu
            if (ApplicationSite.connectedUser == null) {
                System.out.println("1.Login\n2.Sign up");
                int command = scanner.nextInt();
                scanner.nextLine();
                if (command == 1) {
                    S.connectUser();
                    try{
                        A.insertAction("Login user");
                    } catch (Exception e) {
                        System.err.println( e.getMessage());
                    }

            } else {
                    S.signupUser();
                    A.insertAction("Sign up user" + S.getUserList().getLast().getUserName());
                }
            }
            else{
                System.out.println("1.Add a course 2.Disconnect 3.Show courses started 4.Show all courses (sorted by name) 5.Buy a course 6.Show all subjects sorted by name\n");
                System.out.println("7.ModifySubjects 8.Manage Account 9.Find User by userName\n");
                int command = scanner.nextInt();
                switch (command) {
                    case 1:
                        S.addCourse();
                        try{
                            A.insertAction("Add course");
                        } catch (Exception e) {
                            System.err.println( e.getMessage());
                        }
                        break;
                    case 2:
                        S.disconnectUser();
                        try{
                            A.insertAction("User disconnected");
                        } catch (Exception e) {
                            System.err.println( e.getMessage());
                        }
                        break;
                    case 3:
                        S.showCoursesStarted();
                        try{
                            A.insertAction("User " + S.getConnectedUser().getUserName() + "wanted to see the course started");
                        } catch (Exception e) {
                            System.err.println( e.getMessage());
                        }
                        break;
                    case 4:
                        S.showAllCourses();
                        try{
                            A.insertAction("User " + S.getConnectedUser().getUserName() + "wanted to see all courses");
                        } catch (Exception e) {
                            System.err.println( e.getMessage());
                        }
                        break;
                    case 5:
                        S.buyCourse();
                        try{
                            A.insertAction("User " + S.getConnectedUser().getUserName() + "bought a course");
                        } catch (Exception e) {
                            System.err.println( e.getMessage());
                        }
                        break;
                    case 6:
                        S.showSubjectsSorted();
                        break;
                    case 7:
                        if (S.getConnectedUser().getAdministrator())
                            J.crudSubjects(S.getConnectedUser());
                        else {
                            System.out.println("Only administrators can modify the subjects!");
                        }
                        break;
                    case 8:
                        J.crudUsers(S.getConnectedUser());
                        break;
                    case 9:
                        J.findUsers(S.getConnectedUser());
                        break;
                    default:
                }
            }
        }
    }
}

