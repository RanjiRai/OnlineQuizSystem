package FrontEnd.Admin;

import BackEnd.Admin.QuizDao;
import Models.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ManageQuestions {
    private ListView<String> questionListView;
    private ObservableList<String> questionList;

    public void start(Stage stage) {
        Label titleLabel = new Label("Manage Quiz Questions");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Input fields for adding questions
        TextField questionField = new TextField();
        questionField.setPromptText("Enter Question");

        TextField optionAField = new TextField();
        optionAField.setPromptText("Option A");

        TextField optionBField = new TextField();
        optionBField.setPromptText("Option B");

        TextField optionCField = new TextField();
        optionCField.setPromptText("Option C");

        TextField optionDField = new TextField();
        optionDField.setPromptText("Option D");

        ComboBox<String> correctOptionBox = new ComboBox<>();
        correctOptionBox.setPromptText("Select Correct Option");
        correctOptionBox.getItems().addAll("A", "B", "C", "D");

        Button addButton = new Button("Add Question");
        Button deleteButton = new Button("Delete Selected");
        Button backButton = new Button("Back to Dashboard");

        questionListView = new ListView<>();
        questionList = FXCollections.observableArrayList();
        loadQuestions();

        // Event Handlers
        addButton.setOnAction(e -> addQuestion(questionField, optionAField, optionBField, optionCField, optionDField, correctOptionBox));
        deleteButton.setOnAction(e -> deleteSelectedQuestion());
        backButton.setOnAction(e -> {
            AdminDashboard dashboard = new AdminDashboard();
            Stage newStage = new Stage();
            dashboard.start(newStage);
            stage.close(); // Close current window
        });

        VBox layout = new VBox(10, titleLabel, questionField, optionAField, optionBField, optionCField, optionDField, correctOptionBox, addButton, questionListView, deleteButton, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));
        Scene scene = new Scene(layout, 500, 550);

        stage.setScene(scene);
        stage.setTitle("Manage Questions");
        stage.show();
    }

    private void loadQuestions() {
        questionList.clear();
        try {
            for (Question q : QuizDao.getAllQuestions()) {
                questionList.add(q.getId() + ": " + q.getQuestionText());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load questions.");
        }
        questionListView.setItems(questionList);
    }

    private void addQuestion(TextField questionField, TextField optionAField, TextField optionBField, TextField optionCField, TextField optionDField, ComboBox<String> correctOptionBox) {
        String questionText = questionField.getText().trim();
        String optionA = optionAField.getText().trim();
        String optionB = optionBField.getText().trim();
        String optionC = optionCField.getText().trim();
        String optionD = optionDField.getText().trim();
        String correctOption = correctOptionBox.getValue();

        if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty() || correctOption == null) {
            showAlert("Error", "Please fill in all fields before adding a question.");
            return;
        }

        if (questionList.contains(questionText)) {
            showAlert("Error", "This question already exists.");
            return;
        }

        boolean success = QuizDao.addQuestion(questionText, optionA, optionB, optionC, optionD, correctOption);
        if (success) {
            loadQuestions(); // Refresh list
            clearFields(questionField, optionAField, optionBField, optionCField, optionDField);
            correctOptionBox.setValue(null);
        } else {
            showAlert("Error", "Failed to add question. Please try again.");
        }
    }

    private void deleteSelectedQuestion() {
        String selectedItem = questionListView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select a question to delete.");
            return;
        }

        try {
            int id = Integer.parseInt(selectedItem.split(":")[0].trim()); // Extracting ID
            boolean success = QuizDao.deleteQuestion(id);
            if (success) {
                loadQuestions(); // Refresh list
            } else {
                showAlert("Error", "Failed to delete question.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Error", "Invalid question ID format.");
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
