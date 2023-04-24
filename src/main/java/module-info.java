module sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;


    opens oop.Interface to javafx.fxml;
    exports oop.Interface;
    exports oop.oop;
    opens oop.oop to javafx.fxml;
}