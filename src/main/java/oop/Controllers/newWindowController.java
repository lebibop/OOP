package oop.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class newWindowController {

    static double x;
    static double y;

    public static void getNewWorkerWindow() throws IOException {
        getPopUpWindow("/addWorker.fxml");
    }

    public static void getNewRoomWindow() throws IOException {
        getPopUpWindow("/addRoom.fxml");
    }

    public static void getNewClientWindow() throws IOException {
        getPopUpWindow("/addClient.fxml");
    }

    public static void getNewReportWindow() throws IOException {
        getPopUpWindow("/addReport.fxml");
    }

//    public static Stage getNewVetWindow() throws IOException {
//        Stage stage = new Stage();
//        Pane main = FXMLLoader.load(Objects.requireNonNull(newWindowController.class.getResource("/addWorker.fxml")));
//        controlDrag(main, stage);
//        stage.setScene(new Scene(main));
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setTitle("Pet Clinic CRM");
//        stage.getScene();
//        stage.showAndWait();
//        return stage;
//    }

//    public static void getNewVisitWindow() throws IOException {
//        getPopUpWindow(ScenePath.ADD_VISIT.getPath());
//    }

    public static void getPopUpWindow(String path) throws IOException {
        Stage stage = new Stage();
        Pane main = FXMLLoader.load(Objects.requireNonNull(newWindowController.class.getResource(path)));
        controlDrag(main, stage);
        stage.setScene(new Scene(main));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Pet Clinic CRM");
        stage.getScene();
        stage.showAndWait();
    }

    public static void controlDrag(Pane main, Stage stage) {
        main.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                x = stage.getX() - event.getScreenX();
                y = stage.getY() - event.getScreenY();
            }
        });
        main.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() + x);
                stage.setY(event.getScreenY() + y);
            }
        });
    }
}
