package learning;

import user.User;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    protected Difficulty difficulty;
    protected List<Question> questionList = new ArrayList<>();

    public Quiz(Difficulty difficulty, List<Question> questionList) {
        this.difficulty = difficulty;
        this.questionList = new ArrayList<>(questionList); // composition
    }
    public int calculateTotalQuizPoints() {
        int points = 0;
        for (Question question : questionList) { // we add the points from every question
            points += question.getPoints();
        }
        return points;
    }

    public double calculateScore(int score) {
        return ((double) score / calculateTotalQuizPoints()) * 100;
    }
}
