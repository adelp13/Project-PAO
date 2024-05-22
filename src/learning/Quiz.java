package learning;

import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Quiz {
    protected String name;
    protected Difficulty difficulty;
    protected List<Question> questionList = new ArrayList<>();

    public Quiz(String name, Difficulty difficulty, List<Question> questionList) {
        this.difficulty = difficulty;
        this.questionList = new ArrayList<>(questionList); // composition
        this.name = name;
    }

    public double runQuiz() {
        int scor = 0;
        Scanner scanner = new Scanner(System.in);
        int i = 1;
        for (Question question : questionList) {
            System.out.println(i + " " + question.getQuestion() + " ?");
            System.out.print("Write answer: ");
            String answer = scanner.nextLine();
            if (answer.equals(question.getAnswer())) {
                System.out.println("Correct answer!");
                scor += question.getPoints();
            }
            else {
                System.out.println("Correct answer is actually " + question.getQuestion());
            }
            i += 1;
        }
        return calculateScore(scor);
    }
    public int calculateTotalQuizPoints() { // calculate the possible
        int points = 0;
        for (Question question : questionList) { // we add the points from every question
            points += question.getPoints();
        }
        return points;
    }

    public double calculateScore(int score) {
        return ((double) score / calculateTotalQuizPoints()) * 100;
    }

    public String getName() {
        return this.name;
    }
    @Override
    public String toString() {
        return "Quiz named " + name + " with " + questionList.size() + " questions. ";
    }
}
