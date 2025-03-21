package BackEnd.Admin;

import BackEnd.Database.DatabaseConnection;
import Models.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDao {
    
    // Add a new question to the database
    public static boolean addQuestion(String question, String optionA, String optionB, String optionC, String optionD, String correctOption) {
        String query = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_option) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, question);
            stmt.setString(2, optionA);
            stmt.setString(3, optionB);
            stmt.setString(4, optionC);
            stmt.setString(5, optionD);
            stmt.setString(6, correctOption);
            
            return stmt.executeUpdate() > 0; // Returns true if insertion is successful
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve all questions
    public static List<Question> getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM questions";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getString("question_text"), // ✅ Corrected column name
                        rs.getString("option_a"), // ✅ Corrected column name
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_option")
                );
                questionList.add(q);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return questionList;
    }

    // Delete a question by ID
    public static boolean deleteQuestion(int id) {
        String query = "DELETE FROM questions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
