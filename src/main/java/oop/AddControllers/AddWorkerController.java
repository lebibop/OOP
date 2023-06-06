package oop.AddControllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import oop.Controllers.SceneController;
import oop.Helpers.UpdateStatus;
import oop.Model.Worker;
import oop.Services.WorkerService;
import java.net.URL;
import java.util.ResourceBundle;

public class AddWorkerController implements Initializable  {
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

    /**
     * Сохраняет нового клиента в базу данных.
     * Если данные прошли валидацию и клиент успешно сохранен, закрывает окно.
     * @param event событие, вызвавшее метод
     */
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

    /**
     * Проверяет, является ли строка числом.
     * @param str строка для проверки
     * @return true, если строка является числом, иначе false
     */
    public static boolean isNumeric(String str) {
        try {
            return Integer.parseInt(str) >= 0;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * Проверяет корректность введенных данных.
     * @return true, если данные корректны, иначе false
     */
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

    /**
     * Создает объект работника на основе введенных данных.
     * @return объект работника
     */
    private Worker createVetFromInput() {
        Worker vet = new Worker();
        vet.setName(name.getText());
        vet.setSurname(surname.getText());
        vet.setDate_bd(bday.getValue());
        vet.setPosition(position.getValue());
        vet.setExperience(Integer.parseInt(exp.getText()));
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
    public void initialize(URL url, ResourceBundle rb)
    {
        position.getItems().addAll("Doorman", "Receptionist", "Bellboy", "Liftman", "Concierge", "Porter", "Waiter", "Manager");
        position.setValue("Choose a job");
    }
}



