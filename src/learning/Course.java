package learning;
import user.User;

import java.util.ArrayList;
import java.util.List;

public class Course implements Comparable<Course> {
    protected String name;
    protected Difficulty difficulty;
    protected double length; // in hours

    protected User teacher;
    protected double price; // euro

    protected List<Quiz> quizList = new ArrayList<>();

    @Override
    public String toString() {
        return "Course " + name + " with " + length + " hours of video content, taught by " + teacher.getLastName() + " " + teacher.getFirstName() + "\n";
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
}
