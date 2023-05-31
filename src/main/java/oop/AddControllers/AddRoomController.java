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
import oop.Model.Room;
import oop.Services.RoomService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddRoomController implements Initializable  {
    RoomService roomService = new RoomService();
    @FXML
    private TextField number;
    @FXML
    private ChoiceBox<String> status;
    @FXML
    private TextField capacity;
    @FXML
    private TextField price;

    @FXML
    private void saveNewVetToDb(ActionEvent event){
        if (validateInputs()) {
            Room vet = createVetFromInput();
            boolean isSaved = new RoomService().createRoom(vet);
            if (isSaved) {
                UpdateStatus.setIsRoomAdded(true);
                delayWindowClose(event);
            }
        }
    }

    public static boolean isNumeric(String str) {
        try {
            return Integer.parseInt(str) > 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (number.getText().equals("") || capacity.getText().equals("") || status.getValue().equals("Choose a status") || price.getText().equals("")) {
            IOAlert.setContentText("You must fill empty field(-s) to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!isNumeric(number.getText())){
            IOAlert.setContentText("Incorrect toe input for ROOM NUMBER - you must input a number (>0)");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!correct_number(Integer.valueOf(number.getText()))){
            IOAlert.setContentText("Incorrect toe input for ROOM NUMBER - A кщщь with this number already exists");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!isNumeric(capacity.getText())){
            IOAlert.setContentText("Incorrect toe input for CAPACITY - you must input a number (>0)");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!isNumeric(price.getText())){
            IOAlert.setContentText("Incorrect toe input for PRICE - you must input a number (>0)");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        return true;
    }


    private boolean correct_number(Integer number){
        for (Room rooms : roomService.getRooms()){
            if (Objects.equals(rooms.getNumber(), number))
                return false;
        }
        return true;
    }

    private Room createVetFromInput() {
        Room vet = new Room();
        vet.setNumber(Integer.valueOf(number.getText()));
        vet.setStatus(status.getValue());
        vet.setCapacity(Integer.valueOf(capacity.getText()));
        vet.setPrice(Integer.valueOf(price.getText()));
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
        status.getItems().addAll("Free", "Booked");
        status.setValue("Choose a status");
    }
}



