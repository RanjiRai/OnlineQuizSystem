package FrontEnd.Admin;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminDashboard {
    public void start(Stage stage) {
        Button manageQuestionsButton = new Button("Manage Questions");
        Button viewResultsButton = new Button("View Results");

        manageQuestionsButton.setOnAction(e -> new ManageQuestions().start(stage));
        viewResultsButton.setOnAction(e -> new ViewResults().start(stage));

        VBox layout = new VBox(10, manageQuestionsButton, viewResultsButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400, 300);

        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}
