module com.interviews {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    
    opens com.controllers to javafx.fxml;
    opens com.interviews to javafx.fml;
    exports com.interviews;

    requires transitive java.desktop; 
}
