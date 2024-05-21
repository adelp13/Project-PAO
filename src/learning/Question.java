package learning;

public class Question implements Comparable<Question>{
    private String question;
    private String answer;
    private int points;

    @Override
    public int compareTo(Question question) {
        return Integer.compare(this.points, question.getPoints()); // it returns a negative integer int if first arg < second arg
    }
    public Question(String question, String answer, int points) {
        this.question = question;
        this.answer = answer;
        this.points = points;
    }
    public int getPoints(){
        return points;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return "Question is " + question + " and the answer is " + answer + " ( " + points + " points\n";
    }
}
