package oop.oop;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import oop.Interface.SceneController;

import java.io.IOException;
import java.time.LocalDate;

public class AddWorkerController {
    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private DatePicker bday;

    @FXML
    private TextField position;
    @FXML
    private TextField exp;

    @FXML
    private Label alertText;

    @FXML
    private void saveNewVetToDb(ActionEvent event) throws IOException {
        if (true/*validateInputs()*/) {
            Worker vet = createVetFromInput();
            boolean isSaved = new WorkerService().createWorker(vet);
            if (isSaved) {
                UpdateStatus.setIsWorkerAdded(true);
                alertText.setText("Vet is added!");
                delayWindowClose(event);
            }
        }
    }

//    private boolean validateInputs() {
//        if (name.getText().equals("")) {
//            alertText.setText("*You must add first name!");
//            return false;
//        }
//
//        if (surname.getText().equals("")) {
//            alertText.setText("*You must add last name!");
//            return false;
//        }
//
//        if (bday.getText().equals("")) {
//            alertText.setText("*You must add speciality!");
//            return false;
//        }
//
//        if (vetAddress.getText().equals("")) {
//            alertText.setText("*You must add address!");
//            return false;
//        }
//        return true;
//    }

    private Worker createVetFromInput() {
        Worker vet = new Worker();
        vet.setName(name.getText());
        vet.setSurname(surname.getText());
        vet.setDate_bd(bday.getValue());
        vet.setPosition(position.getText());
        vet.setExperience(Integer.parseInt(exp.getText()));
        return vet;
    }

    private void delayWindowClose(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event2 -> closeWindow(event));
        delay.play();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        SceneController.close(event);
    }
}
