import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the JavaFX application.
 * Loads the initial UI (HomePage.fxml) and sets up the primary stage.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the home page layout and displaying the main window.
     *
     * @param primaryStage The main stage (window) for this application.
     * @throws Exception If the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/HomePage.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("I AM BUDGET");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * The main method. Launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
