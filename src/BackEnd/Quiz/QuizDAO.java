package BackEnd.Quiz;

import BackEnd.Database.DatabaseConnection;
import Models.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    // ✅ Fetch Questions from Database
    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT id, question_text, option_a, option_b, option_c, option_d, correct_option FROM questions ORDER BY RAND() LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                questions.add(new Question(
                    rs.getInt("id"),
                    rs.getString("question_text").trim(),
                    rs.getString("option_a").trim(),
                    rs.getString("option_b").trim(),
                    rs.getString("option_c").trim(),
                    rs.getString("option_d").trim(),
                    rs.getString("correct_option").trim()  // ✅ Trim correct option to avoid mismatches
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching questions: " + e.getMessage());
        }
        return questions;
    }

    // ✅ Insert a New Question
    public static boolean addQuestion(Question question) {
        String sql = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, question.getQuestionText().trim());
            pstmt.setString(2, question.getOptionA().trim());
            pstmt.setString(3, question.getOptionB().trim());
            pstmt.setString(4, question.getOptionC().trim());
            pstmt.setString(5, question.getOptionD().trim());
            pstmt.setString(6, question.getCorrectOption().trim());  // ✅ Trim input before storing

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding question: " + e.getMessage());
            return false;
        }
    }
}
