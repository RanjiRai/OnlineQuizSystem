package FrontEnd.User;

import BackEnd.User.AuthController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserRegisterView {
    public void start(Stage stage) {
        Label title = new Label("User Registration");
        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        
        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        
        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login"); // 🔹 Added back button
        Label messageLabel = new Label();

        // 🔹 Handle "Back to Login" button action
        backButton.setOnAction(e -> {
            UserLoginView loginView = new UserLoginView();
            loginView.start(stage); // Switch to login view
        });

        registerButton.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("❌ ERROR: Fields cannot be empty!");
                return;
            }

            if (AuthController.registerUser(username, password)) {
                messageLabel.setText("✅ Registration successful!");
                
                // ✅ Automatically redirect to login after successful registration
                UserLoginView loginView = new UserLoginView();
                loginView.start(stage);
            } else {
                messageLabel.setText("❌ Username already exists!");
            }
        });

        VBox layout = new VBox(10, title, userLabel, userField, passLabel, passField, registerButton, backButton, messageLabel);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);
        stage.setScene(scene);
        stage.setTitle("Register");
        stage.show();
    }
}
