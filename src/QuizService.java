import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class QuizService {
    public static List<Question> fetchQuestions(int amount, String difficulty, String categoryId) {
        List<Question> questions = new ArrayList<>();
        try {
            String urlStr = String.format("https://opentdb.com/api.php?amount=%d&difficulty=%s&type=multiple&category=%s", amount, difficulty, categoryId);
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine, response = "";
            while ((inputLine = reader.readLine()) != null) {
                response += inputLine;
            }
            reader.close();

            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(response);
            JSONArray results = (JSONArray) json.get("results");

            for (Object obj : results) {
                JSONObject q = (JSONObject) obj;
                String questionText = htmlDecode((String) q.get("question"));
                String correct = htmlDecode((String) q.get("correct_answer"));
                JSONArray incorrect = (JSONArray) q.get("incorrect_answers");

                List<String> allAnswers = new ArrayList<>();
                allAnswers.add(correct);
                for (Object i : incorrect) {
                    allAnswers.add(htmlDecode((String) i));
                }
                Collections.shuffle(allAnswers);

                questions.add(new Question(questionText, correct, allAnswers));
            }
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }

    private static String htmlDecode(String text) {
        return text.replaceAll("&quot;", "\"")
                .replaceAll("&#039;", "'")
                .replaceAll("&amp;", "&");
    }
}