package FrontEnd.User;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserDashboard {
    private Stage primaryStage;
    private String username;

    public UserDashboard(Stage primaryStage, String username) {
        this.primaryStage = primaryStage;
        this.username = username;
    }

    public void start() {
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        Button startQuizButton = new Button("Start Quiz");
        Button viewLeaderboardButton = new Button("View Leaderboard");
        Button logoutButton = new Button("Logout");

        // ✅ Start Quiz
        startQuizButton.setOnAction(e -> {
            QuizView quizView = new QuizView(primaryStage, username);
            quizView.start();  // No need to pass parameters
        });

        // ✅ View Leaderboard
        viewLeaderboardButton.setOnAction(e -> {
            LeaderboardView leaderboardView = new LeaderboardView(primaryStage);
            leaderboardView.start();
        });

        // ✅ Logout (Redirect to Login)
        logoutButton.setOnAction(e -> {
            UserLoginView loginView = new UserLoginView();
            loginView.start(primaryStage);
        });

        VBox layout = new VBox(10, welcomeLabel, startQuizButton, viewLeaderboardButton, logoutButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("User Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
