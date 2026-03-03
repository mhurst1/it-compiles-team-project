module com.interviews {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    
    /**
     * Added for functionallity for the filereader (BufferImage and ImageIO)
     * transitive - Just means that any module now has access to java.desktop features 
     */
    requires transitive java.desktop; 

    opens com.interviews to javafx.fxml;
    exports com.interviews;
}
