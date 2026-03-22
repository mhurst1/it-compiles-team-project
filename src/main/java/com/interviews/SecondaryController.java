package com.interviews;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * Controller for the secondary JavaFX view.
 */
public class SecondaryController {

    /**
     * Switches the view back to the primary screen.
     *
     * @throws IOException if there is an error loading the primary view
     */
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
