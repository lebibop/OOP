package oop.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * Контроллер переключения.
 * @author lebibop
 */
public class newWindowController {
    static double x;
    static double y;

    /**
     * Открывает новое окно для добавления нового работника.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewWorkerWindow() throws IOException {
        getPopUpWindow("/addWorker.fxml");
    }

    /**
     * Открывает новое окно для добавления новой комнаты.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewRoomWindow() throws IOException {
        getPopUpWindow("/addRoom.fxml");
    }

    /**
     * Открывает новое окно для добавления нового клиента.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewClientWindow() throws IOException {
        getPopUpWindow("/addClient.fxml");
    }

    /**
     * Открывает новое всплывающее окно с заданным FXML-файлом.
     * @param path Путь к FXML-файлу.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
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
        main.setOnMousePressed(event -> {
            x = stage.getX() - event.getScreenX();
            y = stage.getY() - event.getScreenY();
        });
        main.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + x);
            stage.setY(event.getScreenY() + y);
        });
    }
}
