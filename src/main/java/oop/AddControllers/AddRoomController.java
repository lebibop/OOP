package oop.AddControllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import oop.Controllers.SceneController;
import oop.Helpers.UpdateStatus;
import oop.Model.Report;
import oop.Model.Room;
import oop.Services.ReportService;
import oop.Services.RoomService;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddRoomController implements Initializable  {
    RoomService roomService = new RoomService();
    @FXML
    private TextField number;
    @FXML
    private TextField capacity;
    @FXML
    private TextField price;

    @FXML
    private void saveNewVetToDb(ActionEvent event){
        if (validateInputs()) {
            Report january = new Report();
            january.setMonth(1);
            january.setClients_per_month(0);
            january.setFree_per_month(31);
            january.setBooked_per_month(0);

            Report february = new Report();
            february.setMonth(2);
            february.setClients_per_month(0);
            february.setFree_per_month(28);
            february.setBooked_per_month(0);

            Report march = new Report();
            march.setMonth(3);
            march.setClients_per_month(0);
            march.setFree_per_month(31);
            march.setBooked_per_month(0);

            Report april = new Report();
            april.setMonth(4);
            april.setClients_per_month(0);
            april.setFree_per_month(30);
            april.setBooked_per_month(0);

            Report may = new Report();
            may.setMonth(5);
            may.setClients_per_month(0);
            may.setFree_per_month(31);
            may.setBooked_per_month(0);

            Report june = new Report();
            june.setMonth(6);
            june.setClients_per_month(0);
            june.setFree_per_month(30);
            june.setBooked_per_month(0);

            Report july = new Report();
            july.setMonth(7);
            july.setClients_per_month(0);
            july.setFree_per_month(31);
            july.setBooked_per_month(0);

            Report august = new Report();
            august.setMonth(8);
            august.setClients_per_month(0);
            august.setFree_per_month(31);
            august.setBooked_per_month(0);

            Report september = new Report();
            september.setMonth(9);
            september.setClients_per_month(0);
            september.setFree_per_month(30);
            september.setBooked_per_month(0);

            Report october = new Report();
            october.setMonth(10);
            october.setClients_per_month(0);
            october.setFree_per_month(31);
            october.setBooked_per_month(0);

            Report november = new Report();
            november.setMonth(11);
            november.setClients_per_month(0);
            november.setFree_per_month(30);
            november.setBooked_per_month(0);

            Report december = new Report();
            december.setMonth(12);
            december.setClients_per_month(0);
            december.setFree_per_month(31);
            december.setBooked_per_month(0);


            Room room = createVetFromInput();

            january.setRoom(room);
            february.setRoom(room);
            march.setRoom(room);
            april.setRoom(room);
            may.setRoom(room);
            june.setRoom(room);
            july.setRoom(room);
            august.setRoom(room);
            september.setRoom(room);
            october.setRoom(room);
            november.setRoom(room);
            december.setRoom(room);

            room.addReport(january);
            room.addReport(february);
            room.addReport(march);
            room.addReport(april);
            room.addReport(may);
            room.addReport(june);
            room.addReport(july);
            room.addReport(august);
            room.addReport(september);
            room.addReport(october);
            room.addReport(november);
            room.addReport(december);
            boolean isSaved = new RoomService().createRoom(room);

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
        if (number.getText().equals("") || capacity.getText().equals("") || price.getText().equals("")) {
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
            IOAlert.setContentText("Incorrect input for ROOM NUMBER - A ROOM with this number already exists");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!isNumeric(capacity.getText())){
            IOAlert.setContentText("Incorrect input for CAPACITY - you must input a number (>0)");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (!isNumeric(price.getText())){
            IOAlert.setContentText("Incorrect input for PRICE - you must input a number (>0)");
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
    public void initialize(URL url, ResourceBundle rb) {

    }
}



