package oop.Interface;

import oop.Interface.Interface;
import oop.Interface.InterfaceController;
import oop.oop.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class SceneController {

    private static Parent main;

    private static final Logger logger = LoggerFactory.getLogger("Scene Logger");


    public static void getWorkersScene(ActionEvent event) throws IOException {

        logger.error("Transition to WORKERS list scene");

        changeScreen(event, "/Workers.fxml");
    }

    public static void getClientsScene(ActionEvent event) throws IOException {

        logger.debug("Transition to CLIENTS list scene");

        changeScreen(event, "/Clients.fxml");
    }

    public static void getAddWorkerScene(ActionEvent event) throws IOException {

        logger.debug("Transition to CLIENTS list scene");

        changeScreen(event, "/addWorker.fxml");
    }



    private static void changeScreen(ActionEvent event, String path) throws IOException
    {

        logger.debug("Changing scene");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(SceneController.class.getResource(path)));

        main = loader.load();
        Scene visitScene = new Scene(main);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(visitScene);
        window.show();
    }


    public static void close(ActionEvent actionEvent) {

        logger.debug("Closing window");

        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }


}
