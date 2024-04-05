package utilityAndServices;
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
import java.util.HashSet;
public class Main {
    public static void main(String[] args) {
        ApplicationSite S = ApplicationSite.getApplicationSite();
        // we add some users
        User u1 = new User("Popescu", "Maria", "1234", "mariap");
        User u2 = new User("Paraschiv", "Andrei", "1234", "andreip");
        User u3  = new User("Iliescu", "Andreea", "1234", "andreeai");
        S.userList.add(u1);
        S.userList.add(u2);
        S.userList.add(u3);

        // and some subjects
        Subject s1 = new Subject("c++", "Basic C++");
        Subject s2 = new Subject("java", "Introduction to java");
        Subject s3 = new Subject("biology", "advanced biology");
        S.subjectSet.add(s1);
        S.subjectSet.add(s2);
        S.subjectSet.add(s3);

        // we add courses with their teacher (an user already added)
        Course c1 = new Course(10.0, "Course 1", Difficulty.EASY, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2))),
                new Quiz("Quiz 2", Difficulty.MEDIUM,List.of(new Question("2+1", "3", 2), new Question("1+1", "2", 3)))), 10.0, u1);
        Course c2 = new Course(20.0, "Course 2", Difficulty.MEDIUM, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 15.0, u2);
        Course c3 = new Course(30.0, "Course 3", Difficulty.HARD, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 30.5, u3);
        Course c4 = new AccreditedCourse(15.0, "Course 4", Difficulty.HARD, List.of(
                new Quiz("Quiz 1",Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 42.5, u3, 10, Level.ADVANCED);
        Course c5 = new AccreditedCourse(18.0, "Course 5", Difficulty.EASY, List.of(
                new Quiz("Quiz 1", Difficulty.EASY, List.of(new Question("1+1", "2", 2), new Question("1+1", "2", 2)))), 13.5, u1, 5, Level.BASIC);
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
                } else S.signupUser();
            }
            else{
                System.out.println("1.Add a course 2.Disconnect 3.Show all subjects sorted by name 4.Show all courses (sorted by name) 5.Buy a course 6.Show courses started\n");
                int command = scanner.nextInt();
                switch (command) {
                    case 1:
                        S.addCourse();
                        break;
                    case 2:
                        S.disconnectUser();
                        break;
                    case 3:
                        S.showSubjectsSorted();
                        break;
                    case 4:
                        S.showAllCourses();
                        break;
                    case 5:
                        S.buyCourse();
                        break;
                    case 6:
                        S.showCoursesStarted();
                    default:
                }
            }
        }
    }
}
