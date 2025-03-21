package BackEnd.User;

import BackEnd.Database.DatabaseConnection;
import Models.Score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    // ✅ Save or update user score using user_id instead of username
    public static boolean saveUserScore(String username, int score) {
        String getUserIdQuery = "SELECT id FROM users WHERE username = ?";
        String checkScoreQuery = "SELECT score FROM scores WHERE user_id = ?";
        String insertQuery = "INSERT INTO scores (user_id, score) VALUES (?, ?)";
        String updateQuery = "UPDATE scores SET score = ? WHERE user_id = ?";

        Connection conn = null;
        PreparedStatement getUserStmt = null;
        PreparedStatement checkScoreStmt = null;
        PreparedStatement insertOrUpdateStmt = null;
        ResultSet rs = null;
        ResultSet scoreRs = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 🔹 Fetch user_id for given username
            getUserStmt = conn.prepareStatement(getUserIdQuery);
            getUserStmt.setString(1, username);
            rs = getUserStmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                System.out.println("✅ User found! ID: " + userId);

                // 🔹 Check if user already has a score
                checkScoreStmt = conn.prepareStatement(checkScoreQuery);
                checkScoreStmt.setInt(1, userId);
                scoreRs = checkScoreStmt.executeQuery();

                if (scoreRs.next()) {
                    int existingScore = scoreRs.getInt("score");

                    // ✅ Update only if the new score is higher
                    if (score > existingScore) {
                        insertOrUpdateStmt = conn.prepareStatement(updateQuery);
                        insertOrUpdateStmt.setInt(1, score);
                        insertOrUpdateStmt.setInt(2, userId);
                        insertOrUpdateStmt.executeUpdate();
                        System.out.println("✅ Score updated for user " + username + ": " + score);
                    } else {
                        System.out.println("⚠ User " + username + " already has a higher or equal score. No update needed.");
                    }
                } else {
                    // ✅ Insert new score if no existing score found
                    insertOrUpdateStmt = conn.prepareStatement(insertQuery);
                    insertOrUpdateStmt.setInt(1, userId);
                    insertOrUpdateStmt.setInt(2, score);
                    insertOrUpdateStmt.executeUpdate();
                    System.out.println("✅ New score inserted for user " + username + ": " + score);
                }

                conn.commit(); // Commit transaction
                return true;
            } else {
                System.out.println("❌ ERROR: User " + username + " not found in database.");
            }
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction in case of failure
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            System.out.println("❌ ERROR: Could not save score for " + username);
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (scoreRs != null) scoreRs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (getUserStmt != null) getUserStmt.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (checkScoreStmt != null) checkScoreStmt.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (insertOrUpdateStmt != null) insertOrUpdateStmt.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        return false;
    }

    // ✅ Get top 10 scores in descending order
    public static List<Score> getTopScores() {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT u.username, s.score " +
                       "FROM scores s " +
                       "JOIN users u ON s.user_id = u.id " +
                       "ORDER BY s.score DESC " +
                       "LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                scores.add(new Score(rs.getString("username"), rs.getInt("score")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scores;
    }
}
