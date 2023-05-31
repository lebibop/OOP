package oop.AddControllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import oop.Controllers.SceneController;
import oop.Helpers.UpdateStatus;
import oop.Model.Room;
import oop.Model.Client;
import oop.Services.ClientService;
import oop.Services.RoomService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.ResourceBundle;

public class AddClientController implements Initializable  {
    ClientService clientService = new ClientService();
    RoomService roomService = new RoomService();
    @FXML
    private TextField name;

    @FXML
    private TextField surname;
    @FXML
    private DatePicker date_bd;
    @FXML
    private DatePicker date_arr;
    @FXML
    private DatePicker date_dep;

    @FXML
    private ChoiceBox<Room> room;

    @FXML
    private void saveNewVetToDb(ActionEvent event){
        if (validateInputs()) {
            Client vet = createVetFromInput();
            boolean isSaved = new ClientService().createClient(vet);
            if (isSaved) {
                UpdateStatus.setIsClientAdded(true);
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
        if (name.getText().equals("") || surname.getText().equals("") || date_bd.getValue() == null || date_arr.getValue() == null || date_dep.getValue() == null || room.getValue() == null) {
            IOAlert.setContentText("You must fill empty field(-s) to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        return true;
    }

    private Client createVetFromInput() {
        Client vet = new Client();
        vet.setName(name.getText());
        vet.setSurname(surname.getText());
        vet.setDate_bd(date_bd.getValue());
        vet.setDate_arrival(date_arr.getValue());
        vet.setDate_departure(date_dep.getValue());
        vet.setStay_lenght((int) ChronoUnit.DAYS.between(vet.getDate_arrival(), vet.getDate_departure()));
        vet.setRoom(room.getValue());
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
//        try {
//            //log.debug("uploading to file");
//            Stage stage = new Stage();
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setInitialDirectory(new File("saves"));
//            fileChooser.setTitle("select file");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select csv","*.csv"));
//            File file = fileChooser.showOpenDialog(stage);
//
//            BufferedReader reader = new BufferedReader(new FileReader(file.toURI().toString().substring(6)));
//            java.util.List<String> positions = Arrays.asList("doorman", "receptionist", "bellboy", "liftman", "concierge", "porter", "waiter", "manager");
//            String temp;
//            do{
//                temp = reader.readLine();
//                if(temp!=null) {
//                    String[] temp2 = temp.split(";");
//                    if (temp2.length == 5) {
//                        if (isNumeric(temp2[4]) && positions.contains(temp2[3].toLowerCase())) {
//                            Worker st = new Worker();
//                            st.setName(temp2[0]);
//                            st.setSurname(temp2[1]);
//                            String[] words = temp2[2].split("-");
//                            st.setDate_bd(LocalDate.of(Integer.parseInt(words[0]), Integer.parseInt(words[1]), Integer.parseInt(words[2])));
//                            st.setPosition(temp2[3]);
//                            st.setExperience(Integer.parseInt(temp2[4]));
//
//                            workerService.createWorker(st);
//                        }
//                    }
//                }
//            }
//            while(temp!=null);
//            reader.close();
//
//            UpdateStatus.setIsWorkerAdded(true);
//            delayWindowClose(event);
//            //log.info("uploaded to file");
//        }
//        catch (IOException e)
//        {
//            //log.warn("Exception " + e);
//            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
//            IOAlert.setContentText("Error");
//            IOAlert.showAndWait();
//            if(IOAlert.getResult() == ButtonType.OK)
//            {
//                IOAlert.close();
//            }
//        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ObservableList<Room> roomObservableList = FXCollections.observableArrayList(roomService.getRooms());
        room.getItems().addAll(roomObservableList);
//        room.setValue("Choose a room");
    }
}



