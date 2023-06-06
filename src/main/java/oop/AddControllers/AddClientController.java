package oop.AddControllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import oop.Controllers.SceneController;
import oop.Helpers.ReportUpdate;
import oop.Helpers.UpdateStatus;
import oop.Model.Room;
import oop.Model.Client;
import oop.Services.ClientService;
import oop.Services.RoomService;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AddClientController implements Initializable  {
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
    private ChoiceBox<Integer> cap;
    @FXML
    private ChoiceBox<Room> room_choose;

    /**
     * Выполняет поиск свободных номеров в базе данных и отображает их в выпадающем списке.
     */
    @FXML
    private void search() {
        try {
            LocalDate arr = date_arr.getValue();
            LocalDate dep = date_dep.getValue();
            Integer capacity = cap.getValue();

            if (date_arr.getValue() == null || date_dep.getValue() == null || !date_arr.getValue().isBefore(date_dep.getValue()))
                throw new Exception();

            ObservableList<Room> roomOb = FXCollections.observableArrayList(roomService.find_rooms(arr, dep, capacity));

            room_choose.getItems().clear();
            room_choose.getItems().addAll(roomOb);
            room_choose.setValue(roomOb.get(0));
        }
        catch (Exception e)
        {
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Incorrect input for Date search");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    /**
     * Сохраняет изменения в базе данных после редактирования клиента.
     * Если данные введены корректно, то создается новый объект клиента и происходит обновление базы данных.
     * Если данные введены некорректно, то выводится сообщение об ошибке.
     */
    @FXML
    private void saveNewVetToDb(ActionEvent event){
        if (validateInputs()) {
            Client vet = createVetFromInput();
            Room room = new RoomService().getRoom_ByNumber(vet.getRoom().getNumber());
            room.addClient(vet);
            new ReportUpdate().update_report_add(vet, room);
            new RoomService().updateRoom(room);
            boolean isSaved = new ClientService().createClient(vet);
            if (isSaved) {
                UpdateStatus.setIsClientAdded(true);
                delayWindowClose(event);
            }
        }
    }

    /**
     * Проверяет корректность введенных данных.
     * Если все поля заполнены и выбрана свободная комната, возвращает true.
     * Если есть незаполненные поля или выбрана занятая комната, выводит сообщение об ошибке и возвращает false.
     */
    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (name.getText().equals("") || surname.getText().equals("") || date_bd.getValue() == null || date_arr.getValue() == null || date_dep.getValue() == null || room_choose.getValue() == null) {
            IOAlert.setContentText("You must fill empty field(-s) to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (!roomService.find_rooms(date_arr.getValue(), date_dep.getValue(), cap.getValue()).contains(room_choose.getValue())) {
            IOAlert.setContentText("You must press SEARCH button");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        return false;
        }

        return true;
    }

    /**
     * Создает новый объект клиента на основе введенных данных.
     * @return новый объект клиента
     */
    private Client createVetFromInput() {
        Client vet = new Client();
        vet.setName(name.getText());
        vet.setSurname(surname.getText());
        vet.setDate_bd(date_bd.getValue());
        vet.setDate_arrival(date_arr.getValue());
        vet.setDate_departure(date_dep.getValue());
        vet.setStay_lenght((int) ChronoUnit.DAYS.between(vet.getDate_arrival(), vet.getDate_departure()));
        vet.setRoom(room_choose.getValue());
        return vet;
    }

    /**
     * Задерживает закрытие окна на 1 секунду.
     * @param event событие, вызвавшее метод
     */
    private void delayWindowClose(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event2 -> closeWindow(event));
        delay.play();
    }

    /**
     * Закрывает текущее окно.
     * @param event событие, вызвавшее метод
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        SceneController.close(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cap.getItems().addAll(1,2,3,4);
        cap.setValue(1);
    }
}



