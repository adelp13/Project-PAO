package learning;

import user.User;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private String answer;
    private int points;

    public Question(String question, String answer, int points) {
        this.question = question;
        this.answer = answer;
        this.points = points;
    }
    public int getPoints(){
        return points;
    }
}
