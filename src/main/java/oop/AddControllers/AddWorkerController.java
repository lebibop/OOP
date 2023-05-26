package oop.AddControllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import oop.Controllers.SceneController;
import oop.Helpers.UpdateStatus;
import oop.Model.Worker;
import oop.Services.WorkerService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
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
        if (validateInputs()) {
            Worker vet = createVetFromInput();
            boolean isSaved = new WorkerService().createWorker(vet);
            if (isSaved) {
                UpdateStatus.setIsWorkerAdded(true);
                delayWindowClose(event);
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            return Integer.parseInt(str) >= 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (name.getText().equals("") || surname.getText().equals("") || position.getValue().equals("Choose a job") || bday.getValue() == null || exp.getText().equals("")) {
            IOAlert.setContentText("You must fill empty field(-s) to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!isNumeric(exp.getText())){
            IOAlert.setContentText("Incorrect toe input for EXPERIENCE - you must input a number (>=0)");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }


        return true;
    }

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
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("saves"));
            fileChooser.setTitle("select file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select csv","*.csv"));
            File file = fileChooser.showOpenDialog(stage);

            BufferedReader reader = new BufferedReader(new FileReader(file.toURI().toString().substring(6)));
            java.util.List<String> positions = Arrays.asList("doorman", "receptionist", "bellboy", "liftman", "concierge", "porter", "waiter", "manager");
            String temp;
            do{
                temp = reader.readLine();
                if(temp!=null) {
                    String[] temp2 = temp.split(";");
                    if (temp2.length == 5) {
                        if (isNumeric(temp2[4]) && positions.contains(temp2[3].toLowerCase())) {
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



