package org.aieesti;
//Entry point of GUI
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 400);
        stage.setTitle("Appraisal Update Tool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();  // JavaFX entry point
    }
}
