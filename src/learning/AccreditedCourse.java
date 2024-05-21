package learning;

import user.User;

import java.util.List;

public final class AccreditedCourse extends Course {
    protected int accreditationPeriod; // in years
    protected Level accreditationLevel;

    public AccreditedCourse(double price, String name, Difficulty difficulty, List<Quiz> quizList, double length, User teacher, int accreditationPeriod, Level accreditationLevel) {
        super(price, name, difficulty, quizList, length, teacher);
        this.accreditationLevel = accreditationLevel;
        this.accreditationPeriod = accreditationPeriod;
    }
    public AccreditedCourse() {
        super();
    }

    @Override
    public String toString() {
        return "Accredited " + super.toString();
    }
}
