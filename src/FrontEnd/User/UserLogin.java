package FrontEnd.User;

import BackEnd.Middleware.AuthService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserLogin {
    public void start(Stage stage) {
        Label label = new Label("User Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (AuthService.validateUser(username, password, "USER")) {
                messageLabel.setText("Login Successful!");
                
                // ✅ Pass the same stage and username to UserDashboard
                UserDashboard userDashboard = new UserDashboard(stage, username);
                userDashboard.start();
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            }
        });

        VBox layout = new VBox(10, label, usernameField, passwordField, loginButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 200);

        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.show();
    }
}
