package FrontEnd.Admin;

import BackEnd.Middleware.AuthService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminLogin {
    public void start(Stage stage) {
        Label label = new Label("Admin Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Admin Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Admin Password");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (AuthService.validateUser(username, password, "ADMIN")) {
                messageLabel.setText("Login Successful!");
                new AdminDashboard().start(stage);
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            }
        });

        VBox layout = new VBox(10, label, usernameField, passwordField, loginButton, messageLabel);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 300, 200);

        stage.setScene(scene);
        stage.setTitle("Admin Login");
        stage.show();
    }
}
