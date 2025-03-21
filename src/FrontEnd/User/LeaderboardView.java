package FrontEnd.User;

import BackEnd.User.LeaderBoardController;
import Models.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardView {
    private Stage stage;

    public LeaderboardView(Stage stage) {
        this.stage = stage;
    }

    public void start() {
        TableView<Score> table = new TableView<>();

        TableColumn<Score, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        table.getColumns().addAll(usernameColumn, scoreColumn);

        // ✅ Fetch scores with null check
        List<Score> scores = LeaderBoardController.getTopScores();
        ObservableList<Score> data = FXCollections.observableArrayList(scores != null ? scores : List.of());
        table.setItems(data);

        Label title = new Label("🏆 Leaderboard");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // ✅ Navigate back to UserDashboard
            new UserDashboard(stage, "User").start();
        });

        VBox layout = new VBox(10, title, table, backButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 350);
        stage.setScene(scene);
        stage.setTitle("Leaderboard");
        stage.show();
    }
}
