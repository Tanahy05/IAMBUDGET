import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the HomePage.fxml as the initial view
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/HomePage.fxml"));

        // Create a new scene with the loaded FXML
        Scene scene = new Scene(root);

        // Configure and show the primary stage
        primaryStage.setTitle("Budget Manager");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}