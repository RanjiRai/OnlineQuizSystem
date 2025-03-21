package FrontEnd.Admin;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        AdminLogin Login = new AdminLogin();
         Login.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}