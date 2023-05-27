package oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oop.Helpers.HibernateUtil;
import org.slf4j.*;

import java.util.Objects;

public class Main extends Application {

    private static final Logger log = LoggerFactory.getLogger("Main logger");
    @Override
    public void start(Stage MainStage) throws Exception
    {
        log.debug("Start program");
        Parent LoginParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/login.fxml")));
        Scene LoginScene = new Scene(LoginParent);
        MainStage.setScene(LoginScene);
//        MainStage.initStyle(StageStyle.UTILITY);
        MainStage.setTitle("Login");
        MainStage.show();
    }


    @Override
    public void stop() throws Exception {
        log.debug("End of a program");
        super.stop();
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

}