import java.io.*;
import java.util.*;

public class QuizGame {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("🎮 Welcome to Java Quiz Game!");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.print("How many questions? ");
        int numQuestions = sc.nextInt();

        System.out.print("Choose difficulty (easy, medium, hard): ");
        String difficulty = sc.next();

        System.out.println("Choose category:");
        System.out.println("9 - General Knowledge");
        System.out.println("11 - Movies");
        System.out.println("18 - Computers");
        System.out.println("21 - Sports");
        System.out.print("Enter category ID: ");
        String categoryId = sc.next();

        List<Question> questions = QuizService.fetchQuestions(numQuestions, difficulty, categoryId);
        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            System.out.println("\nQ" + (i + 1) + ": " + q.question);
            for (int j = 0; j < q.allAnswers.size(); j++) {
                System.out.println((j + 1) + ". " + q.allAnswers.get(j));
            }

            long startTime = System.currentTimeMillis();
            int answer = getAnswerWithinTime(10);
            long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;

            if (answer != -1 && q.allAnswers.get(answer - 1).equals(q.correctAnswer)) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong or Timeout! Correct: " + q.correctAnswer);
            }

            System.out.println("Time taken: " + elapsedTime + "s");
        }

        System.out.println("\n🎉 Quiz Over! " + name + ", your score: " + score + "/" + numQuestions);
        saveScore(name, score);
        displayHighScores();
    }

    private static int getAnswerWithinTime(int seconds) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("\n⏰ Time's up!");
                System.exit(0);
            }
        };
        timer.schedule(task, seconds * 1000L);
        try {
            int answer = sc.nextInt();
            timer.cancel();
            return answer;
        } catch (Exception e) {
            timer.cancel();
            return -1;
        }
    }

    private static void saveScore(String name, int score) throws IOException {
        FileWriter writer = new FileWriter("highscores.txt", true);
        writer.write(name + " - " + score + "\n");
        writer.close();
    }

    private static void displayHighScores() throws IOException {
        System.out.println("\n🏆 High Scores:");
        BufferedReader reader = new BufferedReader(new FileReader("highscores.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
    }
}