import java.util.List;

public class Question {
    String question;
    String correctAnswer;
    List<String> allAnswers;

    public Question(String question, String correctAnswer, List<String> allAnswers) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.allAnswers = allAnswers;

    }
}
