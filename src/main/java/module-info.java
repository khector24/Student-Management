module com.example.studentproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.studentproject to javafx.fxml;
    exports com.example.studentproject;
}