package com.interviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the JavaFX client application.
 */
public class App extends Application {

    private static Scene scene;
    public static User currentUser;
    public static User viewingProfileUser;
    public static Question currentQuestion;
    public static String currentCategory;
    public static Question editingQuestion;

    /**
     * Starts the JavaFX application and loads the primary scene.
     *
     * @param stage the primary application stage
     * @throws IOException if the FXML view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Replaces the current scene root with the requested FXML view.
     *
     * @param fxml the base FXML file name without the extension
     * @throws IOException if the view cannot be loaded
     */
    public static void setRoot(String fxml) throws IOException {
        if (!"profile".equals(fxml)) {
            viewingProfileUser = null;
        }
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Opens a profile for viewing without changing the logged-in user.
     *
     * @param user the profile owner to display
     * @throws IOException if the profile FXML cannot be loaded
     */
    public static void viewProfile(User user) throws IOException {
        viewingProfileUser = user;
        setRoot("profile");
    }

    /**
     * Opens the signed-in user's own profile.
     *
     * @throws IOException if the profile FXML cannot be loaded
     */
    public static void viewCurrentUserProfile() throws IOException {
        viewingProfileUser = null;
        setRoot(currentUser != null ? "profile" : "login");
    }

    /**
     * Returns the user whose profile should currently be shown.
     *
     * @return the viewed profile user, or the signed-in user when no viewed user is selected
     */
    public static User getProfileUser() {
        return viewingProfileUser != null ? viewingProfileUser : currentUser;
    }

    /**
     * Checks whether the visible profile belongs to the signed-in user.
     *
     * @return true when the profile can be edited by the signed-in user
     */
    public static boolean isViewingCurrentUserProfile() {
        User profileUser = getProfileUser();
        if (currentUser == null || profileUser == null) {
            return false;
        }
        if (currentUser.getId() != null && profileUser.getId() != null) {
            return currentUser.getId().equals(profileUser.getId());
        }
        return currentUser == profileUser;
    }

    /**
     * Returns whether the signed-in user can access admin-only screens.
     *
     * @return true when the current user is an admin
     */
    public static boolean isCurrentUserAdmin() {
        return currentUser != null && currentUser.getStatus() == Status.ADMIN;
    }

    /**
     * Returns whether the signed-in user can apply for contributor access.
     *
     * @return true when the current user is a standard user
     */
    public static boolean isCurrentUserStandardUser() {
        return currentUser != null && currentUser.getStatus() == Status.USER;
    }

    /**
     * Shows the admin dashboard navigation option only for admin users.
     *
     * @param adminDashboardButton the admin navigation button on a controller
     */
    public static void configureAdminDashboardButton(Button adminDashboardButton) {
        if (adminDashboardButton == null) {
            return;
        }

        boolean showAdminOption = isCurrentUserAdmin();
        adminDashboardButton.setVisible(showAdminOption);
        adminDashboardButton.setManaged(showAdminOption);
    }

    /**
     * Shows the contributor application option only for standard users.
     *
     * @param contributorApplicationButton the contributor application nav button
     */
    public static void configureContributorApplicationButton(Button contributorApplicationButton) {
        if (contributorApplicationButton == null) {
            return;
        }

        boolean showApplicationOption = isCurrentUserStandardUser();
        contributorApplicationButton.setVisible(showApplicationOption);
        contributorApplicationButton.setManaged(showApplicationOption);
    }

    /**
     * Navigates to the admin dashboard when the current user is allowed.
     *
     * @return true when navigation occurred
     * @throws IOException if the dashboard FXML cannot be loaded
     */
    public static boolean goToAdminDashboardIfAllowed() throws IOException {
        if (!isCurrentUserAdmin()) {
            return false;
        }

        setRoot("admindashboard");
        return true;
    }

    /**
     * Navigates to the contributor application page when the user can apply.
     *
     * @return true when navigation occurred
     * @throws IOException if the application FXML cannot be loaded
     */
    public static boolean goToContributorApplicationIfAllowed() throws IOException {
        if (currentUser == null) {
            setRoot("login");
            return false;
        }
        if (!isCurrentUserStandardUser()) {
            return false;
        }

        setRoot("contributorapplication");
        return true;
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
