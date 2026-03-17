package com.interviews;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * This class serves as the secondary controller for the JavaFX application. It contains methods that handle user interactions and events related to the secondary view of the application. The switchToPrimary() method is used to navigate back to the primary view when a specific event occurs, such as a button click. This allows for seamless navigation between different views in the application, enhancing the user experience.
 */
public class SecondaryController {

    /**
     * Switches the view back to the primary view. This method is triggered by a user event, such as clicking a button, and it uses the App.setRoot() method to change the current view to "primary". This allows users to navigate back to the main interface of the application from the secondary view.
     * @throws IOException if there is an error loading the primary view
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}