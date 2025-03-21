package FrontEnd.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import BackEnd.Database.DatabaseConnection;
import Models.Score;

public class ViewResults {
    public void start(Stage stage) {
        // Table to display results
        TableView<Score> tableView = new TableView<>();

        // Define columns
        TableColumn<Score, String> userColumn = new TableColumn<>("Username");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        tableView.getColumns().addAll(userColumn, scoreColumn);

        // Fetch data from the database
        ObservableList<Score> resultsList = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT u.username, s.score FROM scores s JOIN users u ON s.user_id = u.id ORDER BY s.score DESC";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                resultsList.add(new Score(rs.getString("username"), rs.getInt("score")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set data to TableView
        tableView.setItems(resultsList);

        // Layout
        VBox layout = new VBox(10, new Label("Quiz Results"), tableView);
        layout.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        // Scene setup
        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("View Results");
        stage.show();
    }
}
