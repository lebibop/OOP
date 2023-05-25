package oop.oop;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import oop.Interface.SceneController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddWorkerController implements Initializable  {
    WorkerService workerService = new WorkerService();
    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private DatePicker bday;

    @FXML
    private ChoiceBox<String> position;
    @FXML
    private TextField exp;

    @FXML
    private void saveNewVetToDb(ActionEvent event){
//        if (true/*validateInputs()*/) {
            Worker vet = createVetFromInput();
            boolean isSaved = new WorkerService().createWorker(vet);
            if (isSaved) {
                UpdateStatus.setIsWorkerAdded(true);
                delayWindowClose(event);
//            }
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
        vet.setPosition(position.getValue());
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

    @FXML
    private void add_file(ActionEvent event){
        try {
            //log.debug("uploading to file");
            BufferedReader reader = new BufferedReader(new FileReader("saves/save.csv"));
            String temp;
            do{
                temp = reader.readLine();
                if(temp!=null){
                    String[] temp2 = temp.split(";");
                    Worker st = new Worker();
                    st.setName(temp2[0]);
                    st.setSurname(temp2[1]);
                    String[] words = temp2[2].split("-");
                    st.setDate_bd(LocalDate.of(Integer.parseInt(words[0]), Integer.parseInt(words[1]), Integer.parseInt(words[2])));
                    st.setPosition(temp2[3]);
                    st.setExperience(Integer.parseInt(temp2[4]));

                    workerService.createWorker(st);
                }
            }
            while(temp!=null);
            reader.close();

            UpdateStatus.setIsWorkerAdded(true);
            delayWindowClose(event);
            //log.info("uploaded to file");
        }
        catch (IOException e)
        {
            //log.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        position.getItems().addAll("Doorman", "Receptionist", "Bellboy", "Liftman", "Concierge", "Porter", "Waiter", "Manager");
        position.setValue("Choose a job");
    }
}



