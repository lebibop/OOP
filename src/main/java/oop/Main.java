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
        Parent MainParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/workers.fxml")));
        Scene MainScene = new Scene(MainParent);
        MainStage.setScene(MainScene);
//        MainStage.initStyle(StageStyle.UTILITY);
        MainStage.setTitle("Hotel");
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