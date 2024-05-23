package learning;
import payment.Card;
import user.User;
import utilityAndServices.ApplicationSite;
import utilityAndServices.crudInterface;

import java.util.*;

public class Course extends crudInterface<learning.Course> implements Comparable<Course> {
    private String name;
    private Difficulty difficulty;
    private double length; // in hours

    private User teacher; // if this is null it means the instructor left the platform and lost all the earnings
    private double price; // euro

    private List<Quiz> quizList = new ArrayList<>();

    public User getTeacher() {
        return this.teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }
    public Course(double price, String name, Difficulty difficulty, List<Quiz> quizList,  double length, User teacher) {
        this.name = name;
        this.price = price;
        this.difficulty = difficulty;
        this.length = length;
        this.quizList = new ArrayList<>(quizList); // composition
        this.teacher = teacher; //this is agregation, not composition; the user exists outside the course
    }
    public Course() {

    }

    @Override
    public String getObjectType() {
        return "Course";
    }

    @Override
    public List<Object> getParametersValues() { // se respecta ordinea atributelor ordinea aributelor
        List<Object> lista = new ArrayList<>(List.of(name, price, difficulty, length, (teacher.getIdUser()).toString() ));
        return lista;
    }

    @Override
    public Course read() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter course name:");
        this.name = scanner.nextLine();
        // to do: request to enter quizes and question
        System.out.println("Enter course difficulty:");
        this.difficulty = Difficulty.valueOf(scanner.nextLine());

        System.out.println("Enter course length (in hours):");
        this.length = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter course price:");
        this.price = scanner.nextDouble();
        scanner.nextLine();
        ApplicationSite S = ApplicationSite.getApplicationSite();
        this.teacher = S.getConnectedUser();
        return this;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getName() {
        return name;
    }
    public double getLength() {
        return length;
    }
    public double getPrice() {
        return length;
    }
    public List<Quiz> getQuizList() {
        return quizList;
    }

    @Override
    public int compareTo(Course course) {
        return this.name.compareTo(course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Course " + name + " with " + length + " hours of video content, "  + ((teacher == null) ? ("the teacher left the platform \n") : ("taught by " + teacher.getLastName() + " " + teacher.getFirstName() + "\n"));
    }

}
