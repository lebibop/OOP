package oop.Controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oop.Helpers.CustomIntegerStringConverter;
import oop.Helpers.LocalDateCellFactory;
import oop.Helpers.UpdateStatus;
import oop.Model.Worker;
import oop.Services.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

import static oop.AddControllers.AddWorkerController.isNumeric;

/**
 * Контроллер для таблицы работников.
 * @author lebibop
 */
public class WorkerController implements Initializable
{
    @FXML
    private TableColumn<Worker, String> name_column;
    @FXML
    private TableColumn<Worker, String> surname_column;
    @FXML
    public TableColumn<Worker, LocalDate> date_column;
    @FXML
    public TableColumn<Worker, String> position_column;
    @FXML
    public TableColumn<Worker, Integer> exp_column;
    private static final Logger log = LoggerFactory.getLogger("Worker logger");

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TextField search;

    @FXML
    private TableView<Worker> table = new TableView<>();

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    WorkerService workerService = new WorkerService();
    ObservableList<Worker> List = FXCollections.observableArrayList();

    /**
     * Обработчик события выбора значения в выпадающем списке.
     * Получает выбранное значение и вызывает соответствующий метод в классе SceneController для отображения соответствующей сцены.
     * @param event событие выбора значения в выпадающем списке
     * @throws IOException если возникает ошибка ввода-вывода при отображении сцены
     */
    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Clients"))
            SceneController.getClientsScene(event);
        if (Objects.equals(choice, "Rooms"))
            SceneController.getRoomsScene(event);
        if (Objects.equals(choice, "Reports"))
            SceneController.getReportsScene(event);
    }

    /**
     * Обновление экрана.
     * Вызывает метод в классе SceneController для отображения сцены с работниками.
     * @param event событие нажатия на кнопку обновления экрана
     * @throws IOException если возникает ошибка ввода-вывода при отображении сцены
     */
    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getWorkersScene(event);
    }

    /**
     * Обработчик события нажатия на кнопку добавления нового работника.
     * Открывает окно для добавления нового работника и обновляет экран, если работник был успешно добавлен.
     * @param event событие нажатия на кнопку добавления нового работника
     * @throws IOException если возникает ошибка ввода-вывода при отображении окна добавления нового работника или при обновлении экрана
     */
    @FXML
    private void add(ActionEvent event) throws IOException {
        log.debug("adding a worker");

        newWindowController.getNewWorkerWindow();
        if(UpdateStatus.isIsWorkerAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsWorkerAdded(false);
        }
        log.info("worker added");
    }


    /**
     * Метод для заполнения таблицы работников данными из базы данных.
     * Очищает список работников, затем добавляет в него всех работников из базы данных.
     */
    private void setObList() {
        log.debug("adding a worker table");
        List.clear();
        List.addAll(workerService.getWorkers());
        log.info("worker table is added");
    }


    /**
     * Метод для удаления выбранных строк из таблицы работников.
     * Получает индекс выбранной строки, выбранные строки и удаляет их из базы данных.
     * Если ни одна строка не выбрана, выбрасывает исключение MyException.
     * После удаления строк обновляет экран.
     * @param event событие нажатия на кнопку удаления строк
     * @throws MyException если ни одна строка не выбрана
     * @throws IOException если возникает ошибка ввода-вывода при обновлении экрана
     */
    private void remove_row(ActionEvent event) throws MyException, IOException {
        log.debug("removing a worker");
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else {
            ObservableList<Worker> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Worker worker : selectedRows) {
                workerService.deleteWorker(worker);
            }
            log.info("remove is done");
            refreshScreen(event);
        }
    }

    static class MyException extends Exception
    {
        public MyException()
        {
            super("Choose a row to delete");
        }
    }

    /**
     * Обработчик события нажатия на кнопку удаления работника.
     * Вызывает метод remove_row() для удаления выбранных строк из таблицы работников.
     * Если ни одна строка не выбрана, выводит сообщение об ошибке.
     * @param event событие нажатия на кнопку удаления работника
     */
    @FXML
    private void delete(ActionEvent event)
    {
        try {
            log.debug("deleting a worker");
            remove_row(event);
            log.info("worker deleted");
        }

        catch (MyException | IOException myEx){
            log.error("Exception " + myEx);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText(myEx.getMessage());
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    /**
     * Обработчик события нажатия на кнопку сохранения таблицы работников в файл.
     * Сохраняет данные из таблицы в файл "saves/save.csv".
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     * После сохранения файла открывает папку "saves".
     */
    @FXML
    private void save() {
        try {
            log.debug("saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_worker.csv"));
            for(Worker workers : List)
            {
                writer.write(workers.getName() + ";" + workers.getSurname() + ";"
                        + workers.getDate_bd() + ";" + workers.getPosition() + ";" + workers.getExperience());
                writer.newLine();
            }
            writer.close();
            Desktop.getDesktop().open(new File("saves"));
            log.info("saved to file");
        }
        catch (IOException e)
        {
            log.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }
    @FXML
    private void upload(ActionEvent event)
    {
        try {
            log.debug("uploading to file");

            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("saves"));
            fileChooser.setTitle("select file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Select csv","*.csv"));
            File file = fileChooser.showOpenDialog(stage);

            BufferedReader reader = new BufferedReader(new FileReader(file.toURI().toString().substring(6)));
            ObservableList<Worker> selectedRows = table.getItems();
            for (Worker worker : selectedRows) {
                workerService.deleteWorker(worker);
            }
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
            refreshScreen(event);
            log.info("uploaded to file");
        }
        catch (IOException e)
        {
            log.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
            IOAlert.setContentText("Can't find file to upload");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    /**
     * Обработчик события нажатия на кнопку сохранения таблицы работников в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_worker.pdf".
     * Если список работников пуст, выбрасывает исключение MyException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    @FXML
    public void toPDF() {
        try {
            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("pdf/report_worker.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(5);
            my_report_table.setWidthPercentage(100f);
            my_report_table.setTotalWidth(PageSize.A4.getWidth() - my_pdf_report.leftMargin() - my_pdf_report.rightMargin()); // устанавливаем фиксированную ширину таблицы
            my_report_table.setLockedWidth(true);

            PdfPCell headerCell = new PdfPCell(new Phrase("Workers REPORT"));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setColspan(5);
            headerCell.setPaddingBottom(10);
            my_report_table.addCell(headerCell);

            PdfPCell table_cell;

            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Surname", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Date", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Position", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Experience", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));

            if (List.isEmpty()) throw new MyException();

            for(Worker workers : List)
            {
                table_cell=new PdfPCell(new Phrase(workers.getName()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(workers.getSurname()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(workers.getDate_bd())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(workers.getPosition()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(workers.getExperience())));
                my_report_table.addCell(table_cell);
            }
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();
            log.info("Saved to PDF");
        }
        catch (FileNotFoundException | DocumentException | MyException e)
        {
            log.warn("Exception " + e);
            e.printStackTrace();
        }
    }

    /**
     * Обработчик события изменения имени работника в таблице.
     * Получает выделенного работника из таблицы и обновляет его имя в соответствии с новым значением.
     * Затем вызывает метод updateWorker() из workerService для сохранения изменений в базе данных.
     * @param editEvent Событие изменения ячейки таблицы, содержащее новое значение имени работника.
     */
    @FXML
    private void change_name(TableColumn.CellEditEvent<Worker, String> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setName(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }

    /**
     * Обработчик события изменения фамилии работника в таблице.
     * Получает выделенного работника из таблицы и обновляет его фамилию в соответствии с новым значением.
     * Затем вызывает метод updateWorker() из workerService для сохранения изменений в базе данных.
     * @param editEvent Событие изменения ячейки таблицы, содержащее новое значение фамилии работника.
     */
    @FXML
    private void change_surname(TableColumn.CellEditEvent<Worker, String> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setSurname(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }

    /**
     * Обработчик события изменения опыта работы работника в таблице.
     * Получает выделенного работника из таблицы и обновляет его опыт работы в соответствии с новым значением.
     * Если новое значение опыта работы меньше нуля, обновление не происходит и таблица обновляется.
     * Затем вызывает метод updateWorker() из workerService для сохранения изменений в базе данных.
     * @param editEvent Событие изменения ячейки таблицы, содержащее новое значение опыта работы работника.
     */
    @FXML
    private void change_exp(TableColumn.CellEditEvent<Worker, Integer> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();

        Integer ee = editEvent.getNewValue();
        if (ee >= 0) {
            selectedPet.setExperience(ee);
            workerService.updateWorker(selectedPet);
        }
        else table.refresh();
    }

    /**
     * Обработчик события изменения должности работника в таблице.
     * Получает выделенного работника из таблицы и обновляет его должность в соответствии с новым значением.
     * Затем вызывает метод updateWorker() из workerService для сохранения изменений в базе данных.
     * @param editEvent Событие изменения ячейки таблицы, содержащее новое значение должности работника.
     */
    @FXML
    private void change_pos(TableColumn.CellEditEvent<Worker, String> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setPosition(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }

    /**
     * Обработчик события изменения даты рождения работника в таблице.
     * Получает выделенного работника из таблицы и обновляет его дату рождения в соответствии с новым значением.
     * Затем вызывает метод updateWorker() из workerService для сохранения изменений в базе данных.
     * @param editEvent Событие изменения ячейки таблицы, содержащее новое значение даты рождения работника.
     */
    @FXML
    private void change_data(TableColumn.CellEditEvent<Worker, LocalDate> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setDate_bd(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }

    /**
     * Метод для получения отфильтрованного и отсортированного списка работников.
     * Создает новый отфильтрованный список на основе исходного списка работников, используя фильтр из searchField.
     * Затем создает новый отсортированный список на основе отфильтрованного списка и связывает его с компаратором таблицы.
     * @return Отсортированный список работников.
     */
    private SortedList<Worker> getSortedList() {
        SortedList<Worker> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка работников на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка работников, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "Должность", "Имя", "Фамилия", "Дата рождения" и "Опыт работы" каждого работника.
     * @return Отфильтрованный список работников.
     */
    private FilteredList<Worker> getFilteredList() {
        FilteredList<Worker> filteredList = new FilteredList<>(List, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(worker -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (worker.getPosition().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (worker.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (date_converter(worker.getDate_bd().toString()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (worker.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(worker.getExperience()).contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    /**
     * Метод для конвертации даты из формата "yyyy-MM-dd" в формат "dd.MM.yyyy".
     * @param temp Дата в формате "yyyy-MM-dd".
     * @return Дата в формате "dd.MM.yyyy".
     */
    private String date_converter(String temp){
        String[] temp2 = temp.split("-");
        return temp2[2] + '.' + temp2[1] + '.' + temp2[0];
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        choice_box.getItems().addAll(choices);
        choice_box.setValue("Workers");

        setObList();

        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("Date_bd"));
        position_column.setCellValueFactory(new PropertyValueFactory<>("Position"));
        exp_column.setCellValueFactory(new PropertyValueFactory<>("Experience"));

        table.setEditable(true);
        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        date_column.setCellFactory(new LocalDateCellFactory());
        position_column.setCellFactory(ChoiceBoxTableCell.forTableColumn("Doorman", "Receptionist", "Bellboy", "Liftman", "Concierge", "Porter", "Waiter", "Manager"));
        exp_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));

        table.setItems(getSortedList());
    }
}