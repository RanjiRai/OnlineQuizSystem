package FrontEnd.User;

import BackEnd.Middleware.AuthService; // Ensure correct package name
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLoginView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // UI Components
        Label titleLabel = new Label("User Login");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        Label messageLabel = new Label(); // For displaying success/failure messages

        // Styling
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        // Login Button Action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("⚠ Username and password cannot be empty.");
                return;
            }

            // Authenticate the user using plain text comparison
            if (AuthService.validateUser(username, password, "USER")) {
                messageLabel.setText("✔ Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");

                // Navigate to the User Dashboard
                new UserDashboard(primaryStage, username).start();
            } else {
                messageLabel.setText("❌ Invalid username or password.");
            }
        });

        // Register Button Action
        registerButton.setOnAction(e -> new UserRegisterView().start(primaryStage));

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
            titleLabel, usernameLabel, usernameField, passwordLabel, passwordField,
            loginButton, registerButton, messageLabel
        );

        // Scene
        Scene scene = new Scene(layout, 350, 300);
        primaryStage.setTitle("User Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
