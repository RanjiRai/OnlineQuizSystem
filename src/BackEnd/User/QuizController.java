package BackEnd.User;

import BackEnd.Quiz.QuizDAO;
import Models.Question;
import java.util.List;
import java.util.Arrays;

public class QuizController {
    
    public static List<Question> getQuizQuestions() {
        return QuizDAO.getQuestions(); // Fetch randomized questions
    }

    public static int calculateScore(List<Question> questions, String[] userAnswers) {
        int score = 0;

        System.out.println("\n🔍 Checking Answers...");
        System.out.println("DEBUG: Total Questions Fetched = " + questions.size());
        System.out.println("DEBUG: User Answers = " + Arrays.toString(userAnswers));
        
        if (userAnswers == null || userAnswers.length != questions.size()) {
            System.out.println("⚠ ERROR: User answers array is null or does not match questions count!");
            return 0; // Prevent scoring errors if userAnswers is incorrect
        }

        for (int i = 0; i < questions.size(); i++) {
            String correctAnswer = questions.get(i).getCorrectOption();
            String userAnswer = userAnswers[i];

            if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
                System.out.println("⚠ Q" + (i + 1) + " has no correct answer in DB!");
                continue;
            }

            correctAnswer = correctAnswer.trim().toUpperCase(); // ✅ Normalize DB answer (A, B, C, D)
            userAnswer = (userAnswer != null) ? userAnswer.trim().toUpperCase() : ""; // ✅ Normalize user answer (A, B, C, D)

            System.out.println("DEBUG: Q" + (i + 1) + " | User: [" + userAnswer + "] | Correct: [" + correctAnswer + "]");

            if (userAnswer.isEmpty()) {
                System.out.println("⚠ Q" + (i + 1) + " was skipped.");
            } else if (correctAnswer.equals(userAnswer)) { 
                score += 10; 
                System.out.println("✅ Q" + (i + 1) + " Correct! +10 points");
            } else {
                System.out.println("❌ Q" + (i + 1) + " Incorrect | Selected: [" + userAnswer + "] | Correct: [" + correctAnswer + "]");
            }
        }

        System.out.println("🎯 FINAL SCORE: " + score + " / " + (questions.size() * 10));
        return score;
    }

    public static boolean saveScore(String username, int score) {
        return ScoreDAO.saveUserScore(username, score);
    }
}
