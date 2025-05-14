package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

public class GOODCREDIT {

    /**
     * Handles the close button action.
     * Closes the credits window when the button is clicked.
     *
     * @param event The action event
     */
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        // Get the source node of the event (the button)
        Node source = (Node) event.getSource();

        // Get the stage (window) that contains the button
        Stage stage = (Stage) source.getScene().getWindow();

        // Close the window
        stage.close();
    }

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        // You can add any initialization code here
        System.out.println("Credits screen initialized");
    }
}