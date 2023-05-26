module sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;
    requires org.apache.logging.log4j.slf4j;
    requires slf4j.api;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.annotation;


    opens oop to javafx.fxml;
    exports oop;
    exports oop.Controllers;
    exports oop.AddControllers;
    opens oop.AddControllers;
    opens oop.AppClasses;
    exports oop.AppClasses;
    exports oop.Model;
    opens oop.Model;
    exports oop.Helpers;
    opens oop.Helpers;
    exports oop.Services;
    opens oop.Services;
    opens oop.Controllers;
}