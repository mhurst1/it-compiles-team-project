package com.interviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the JavaFX client application.
 */
public class App extends Application {

    private static Scene scene;

    /**
     * Starts the JavaFX application and loads the primary scene.
     *
     * @param stage the primary application stage
     * @throws IOException if the FXML view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Replaces the current scene root with the requested FXML view.
     *
     * @param fxml the base FXML file name without the extension
     * @throws IOException if the view cannot be loaded
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads an FXML view from the application's resources.
     *
     * @param fxml the base FXML file name without the extension
     * @return the loaded view root
     * @throws IOException if the view cannot be loaded
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch();
    }

}
