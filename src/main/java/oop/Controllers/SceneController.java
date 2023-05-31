package oop.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

        logger.error("Transition to CLIENTS list scene");

        changeScreen(event, "/Clients.fxml");
    }
    public static void getRoomsScene(ActionEvent event) throws IOException {

        logger.error("Transition to ROOMS list scene");

        changeScreen(event, "/Rooms.fxml");
    }
    public static void getReportsScene(ActionEvent event) throws IOException {

        logger.error("Transition to REPORTS list scene");

        changeScreen(event, "/Reports.fxml");
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
