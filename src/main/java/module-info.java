module com.interviews {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;

    opens com.interviews to javafx.fxml;
    exports com.interviews;
}
