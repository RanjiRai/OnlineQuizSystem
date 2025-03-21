package FrontEnd.User;

import BackEnd.Quiz.QuizDAO;
import BackEnd.User.QuizController;
import Models.Question;
import Utils.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class QuizView {
    private Stage stage;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private String[] userAnswers;
    private Label questionLabel, timerLabel;
    private RadioButton optionA, optionB, optionC, optionD;
    private ToggleGroup optionsGroup;
    private Button nextButton, prevButton;
    private Timeline timeline;
    private int timeLeft = 60; // ⏳ Quiz Timer (seconds)
    private String username; // Store logged-in user

    public QuizView(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
    }

    public void start() {
        this.questions = QuizDAO.getQuestions();

        if (questions.isEmpty()) {
            Label message = new Label("No questions available.");
            VBox layout = new VBox(10, message);
            layout.setAlignment(Pos.CENTER);
            Scene scene = new Scene(layout, 400, 300);
            stage.setScene(scene);
            stage.show();
            return;
        }

        userAnswers = new String[questions.size()];
        setupUI();
        startTimer();
        loadQuestion();
    }

    private void setupUI() {
        questionLabel = new Label();
        timerLabel = new Label("Time Left: " + timeLeft + "s");

        optionA = new RadioButton();
        optionB = new RadioButton();
        optionC = new RadioButton();
        optionD = new RadioButton();

        optionsGroup = new ToggleGroup();
        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);

        prevButton = new Button("Previous");
        prevButton.setDisable(true);
        prevButton.setOnAction(e -> handlePrevious());

        nextButton = new Button("Next");
        nextButton.setDisable(true);
        nextButton.setOnAction(e -> handleNext());

        optionsGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            nextButton.setDisable(newVal == null);
        });

        VBox layout = new VBox(15, timerLabel, questionLabel, optionA, optionB, optionC, optionD, prevButton, nextButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Quiz");
        stage.show();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            questionLabel.setText("Q" + (currentQuestionIndex + 1) + ": " + q.getQuestionText());

            optionA.setText(q.getOptionA());
            optionB.setText(q.getOptionB());
            optionC.setText(q.getOptionC());
            optionD.setText(q.getOptionD());

            optionsGroup.selectToggle(null);

            if (userAnswers[currentQuestionIndex] != null) {
                switch (userAnswers[currentQuestionIndex]) {
                    case "A": optionA.setSelected(true); break;
                    case "B": optionB.setSelected(true); break;
                    case "C": optionC.setSelected(true); break;
                    case "D": optionD.setSelected(true); break;
                }
            }

            prevButton.setDisable(currentQuestionIndex == 0);
            nextButton.setText((currentQuestionIndex == questions.size() - 1) ? "Submit" : "Next");
        } else {
            showResults();
        }
    }

    private void handleNext() {
        if (optionsGroup.getSelectedToggle() != null) {
            RadioButton selected = (RadioButton) optionsGroup.getSelectedToggle();
            String selectedText = selected.getText().trim();

            if (selectedText.equals(optionA.getText())) userAnswers[currentQuestionIndex] = "A";
            else if (selectedText.equals(optionB.getText())) userAnswers[currentQuestionIndex] = "B";
            else if (selectedText.equals(optionC.getText())) userAnswers[currentQuestionIndex] = "C";
            else if (selectedText.equals(optionD.getText())) userAnswers[currentQuestionIndex] = "D";

            System.out.println("Stored Answer for Q" + (currentQuestionIndex + 1) + ": " + userAnswers[currentQuestionIndex]);
        } else {
            userAnswers[currentQuestionIndex] = "";
            System.out.println("Q" + (currentQuestionIndex + 1) + " was skipped.");
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            loadQuestion();
        } else {
            showResults();
        }
    }

    private void handlePrevious() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion();
        }
    }

    private void showResults() {
        timeline.stop(); // ⏳ Stop Timer

        System.out.println("\n🔍 User Answers:");
        for (int i = 0; i < userAnswers.length; i++) {
            System.out.println("Q" + (i + 1) + ": " + userAnswers[i]);
        }

        int score = QuizController.calculateScore(questions, userAnswers);

        if (username != null) {
            boolean saved = QuizController.saveScore(username, score);
            if (!saved) {
                System.err.println("⚠ Failed to save score in DB!");
            }
        }

        Label resultLabel = new Label("🎯 Quiz Completed!\nYour Score: " + score + "/" + (questions.size() * 10));

        Button dashboardButton = new Button("Back to Dashboard");
        dashboardButton.setOnAction(e -> {
            UserDashboard dashboard = new UserDashboard(stage, username);
            dashboard.start();
        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            SessionManager.logoutUser();
            new UserLoginView().start(stage);
        });

        VBox layout = new VBox(15, resultLabel, dashboardButton, logoutButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 350, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeLeft--;
            timerLabel.setText("Time Left: " + timeLeft + "s");

            if (timeLeft <= 0) {
                timeline.stop();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "⏳ Time's up! Submitting your quiz...");
                alert.showAndWait();
                showResults();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
