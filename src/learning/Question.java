package learning;

public class Question implements utilityAndServices.InterfaceCompare<Question>{
    private String question;
    private String answer;
    private int points;

    @Override
    public boolean compareTo(Question question) {
        return this.points > question.getPoints(); // int is primitive, can t use compareTo
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
}
