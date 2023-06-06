package oop.Controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import oop.Helpers.*;
import oop.Model.Room;
import oop.Services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * Контроллер для таблицы комнат.
 * @author lebibop
 */
public class RoomController implements Initializable
{
    @FXML
    private TableColumn<Room, Integer> number_column;
    @FXML
    public TableColumn<Room, Integer> capacity_column;
    @FXML
    public TableColumn<Room, Integer> price_column;
    private static final Logger log = LoggerFactory.getLogger("Room logger");

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private DatePicker arrival;
    @FXML
    private DatePicker departure;
    @FXML
    private ChoiceBox<Integer> cap;

    @FXML
    private TextField search;

    @FXML
    private TableView<Room> table = new TableView<>();

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    RoomService roomService = new RoomService();
    ObservableList<Room> List = FXCollections.observableArrayList();


    /**
     * Обработчик события выбора элемента в выпадающем списке.
     * Определяет выбранный элемент и вызывает соответствующий метод SceneController для отображения соответствующей сцены.
     * @param event Событие выбора элемента в выпадающем списке.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Clients"))
            SceneController.getClientsScene(event);
        if (Objects.equals(choice, "Workers"))
            SceneController.getWorkersScene(event);
        if (Objects.equals(choice, "Reports"))
            SceneController.getReportsScene(event);
    }

    /**
     * Обработчик события поиска свободных номеров по датам и вместимости.
     * Извлекает значения дат и вместимости из соответствующих элементов управления.
     * Вызывает метод сервиса поиска свободных номеров с передачей параметров.
     * Если введены некорректные данные, выбрасывает исключение MyException и выводит сообщение об ошибке.
     */
    @FXML
    private void search_date(){
        try {
            log.debug("Searching");
            LocalDate arr = arrival.getValue();
            LocalDate dep = departure.getValue();
            Integer capacity = cap.getValue();

            if (arrival.getValue() == null || departure.getValue() == null || !arrival.getValue().isBefore(departure.getValue()))
                throw new MyException();

            List.clear();
            List.addAll(roomService.find_rooms(arr, dep, capacity));
            log.info("Search is done");
        }
        catch (MyException e) {
            log.warn("Exception: " + "Incorrect input for Date search");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Incorrect input for Date search");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK) {
                IOAlert.close();
            }
        }
    }

    /**
     * Обработчик события отображения всех номеров.
     * Вызывает метод сервиса получения всех номеров и добавляет их в список для отображения в таблице.
     */
    @FXML
    private void all_rooms() {
        log.debug("Room table is adding");
        List.clear();
        List.addAll(roomService.getRooms());
        log.info("Room table is done");
    }

    /**
     * Обработчик события обновления экрана.
     * Вызывает метод SceneController для отображения сцены с номерами.
     * @param event Событие обновления экрана.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getRoomsScene(event);
    }

    /**
     * Обработчик события добавления нового номера.
     * Вызывает метод NewWindowController для отображения окна добавления нового номера.
     * Если номер был успешно добавлен, обновляет экран с номерами.
     * @param event Событие добавления нового номера.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void add(ActionEvent event) throws IOException {
        log.debug("adding a room");

        newWindowController.getNewRoomWindow();
        if(UpdateStatus.isIsRoomAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsRoomAdded(false);
        }
        log.info("room added");
    }

    /**
     * Метод для установки списка номеров в таблицу.
     * Очищает список, затем добавляет в него все номера из сервиса.
     */
    private void setObList() {
        log.debug("Room table is adding");
        List.clear();
        List.addAll(roomService.getRooms());
        log.info("Room table is done");
    }

    /**
     * Обработчик события удаления выбранных номеров из таблицы.
     * Удаляет выбранные номера из сервиса.
     * Если ни один номер не выбран, выбрасывает исключение MyException.
     * После удаления номеров обновляет экран с номерами.
     * @param event Событие удаления номеров.
     * @throws MyException Если ни один номер не выбран.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    private void remove_row(ActionEvent event) throws MyException, IOException {
        log.debug("removing a worker");
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else {
            ObservableList<Room> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Room room : selectedRows) {
                roomService.deleteRoom(room);
            }
            log.info("remove is done");
            refreshScreen(event);
        }
    }

    static class MyException extends Exception {
        public MyException()
        {
            super("Choose a row to delete");
        }
    }

    /**
     * Обработчик события удаления выбранных номеров из таблицы.
     * Удаляет выбранные номера из сервиса.
     * Если ни один номер не выбран, выбрасывает исключение MyException.
     * После удаления номеров обновляет экран с номерами.
     * @param event Событие удаления номеров.
     */
    @FXML
    private void delete(ActionEvent event) {
        try {
            log.debug("deleting a room");
            search_invalid_label.setText("");
            remove_row(event);
            log.info("room deleted");
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
     * Обработчик события сохранения списка номеров в файл.
     * Сохраняет список номеров в файл "saves/save.csv".
     * Если произошла ошибка ввода-вывода при сохранении, выбрасывает исключение IOException.
     * После сохранения открывает папку "saves".
    */
    @FXML
    private void save() {
        try {
            log.debug("saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_room.csv"));
            for(Room rooms : List) {
                writer.write(rooms.getNumber()  + ";"
                        + rooms.getCapacity() + ";" + rooms.getPrice());
                writer.newLine();
            }
            writer.close();
            Desktop.getDesktop().open(new File("saves"));
            log.info("saved to file");
        }
        catch (IOException e) {
            log.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK) {
                IOAlert.close();
            }
        }
    }


    /**
     * Обработчик события нажатия на кнопку сохранения таблицы комнат в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_room.pdf".
     * Если список работников пуст, выбрасывает исключение MyException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    public void toPDF() throws Exception {
        try {
            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("pdf/report_room.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(3);
            my_report_table.setWidthPercentage(100f);
            my_report_table.setTotalWidth(PageSize.A4.getWidth() - my_pdf_report.leftMargin() - my_pdf_report.rightMargin()); // устанавливаем фиксированную ширину таблицы
            my_report_table.setLockedWidth(true);

            PdfPCell headerCell = new PdfPCell(new Phrase("Rooms REPORT"));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setColspan(3);
            headerCell.setPaddingBottom(10);
            my_report_table.addCell(headerCell);

            PdfPCell table_cell;

            my_report_table.addCell(new PdfPCell(new Phrase("Number", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Capacity", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Price", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));

            if (List.isEmpty()) throw new Exception();

            for(Room rooms : List) {
                table_cell=new PdfPCell(new Phrase(String.valueOf(rooms.getNumber())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(rooms.getCapacity())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(rooms.getPrice())));
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
     * Обработчик события изменения номера комнаты в таблице.
     * При изменении номера комнаты проверяет, что новый номер больше 0 и не совпадает с номером другой комнаты.
     * Если новый номер не уникален, выбрасывает исключение MyException.
     * При возникновении исключения выводит сообщение об ошибке и обновляет таблицу.
     * Если изменение номера прошло успешно, обновляет базу данных и таблицу.
     * @param editEvent Событие изменения номера комнаты в таблице.
     */
    @FXML
    private void change_number(TableColumn.CellEditEvent<Room, Integer> editEvent) {
        try {
            Room selectedPet = table.getSelectionModel().getSelectedItem();

            Integer ee = editEvent.getNewValue();
            if (ee > 0) {
                for (Room rooms : List) {
                    if (Objects.equals(rooms.getNumber(), ee) && selectedPet != rooms) {
                        throw new MyException();
                    }
                }
                selectedPet.setNumber(ee);
                roomService.updateRoom(selectedPet);
            } else table.refresh();
        }
        catch (MyException myEx) {
            log.error("Exception " + "A room with this number already exists");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText("A room with this number already exists");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            table.refresh();
        }
    }

    /**
     * Обработчик события изменения вместимости комнаты в таблице.
     * При изменении вместимости комнаты проверяет, что новое значение больше 0.
     * Если новое значение не удовлетворяет условию, обновляет таблицу без изменений.
     * Если изменение вместимости прошло успешно, обновляет базу данных, таблицу и отчет.
     * @param editEvent Событие изменения вместимости комнаты в таблице.
     */
    @FXML
    private void change_capacity(TableColumn.CellEditEvent<Room, Integer> editEvent) {
        Room selectedPet = table.getSelectionModel().getSelectedItem();

        Integer ee = editEvent.getNewValue();
        if (ee > 0) {
            selectedPet.setCapacity(ee);
            roomService.updateRoom(selectedPet);
            new ReportUpdate().update_report_change(roomService.getRoom_ByNumber(selectedPet.getNumber()));
        }
        else table.refresh();
    }

    /**
     * Обработчик события изменения цены комнаты в таблице.
     * При изменении цены комнаты проверяет, что новое значение больше или равно 0.
     * Если новое значение не удовлетворяет условию, обновляет таблицу без изменений.
     * Если изменение цены прошло успешно, обновляет базу данных и таблицу.
     * @param editEvent Событие изменения цены комнаты в таблице.
     */
    @FXML
    private void change_price(TableColumn.CellEditEvent<Room, Integer> editEvent) {
        Room selectedPet = table.getSelectionModel().getSelectedItem();

        Integer ee = editEvent.getNewValue();
        if (ee >= 0) {
            selectedPet.setPrice(ee);
            roomService.updateRoom(selectedPet);
        }
        else table.refresh();
    }

    /**
     * Возвращает отсортированный список комнат с учетом фильтрации и текущего порядка сортировки в таблице.
     * @return Отсортированный список комнат.
     */
    private SortedList<Room> getSortedList() {
        SortedList<Room> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка комнат на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка комнат, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "Номер", "Вместимость" и "Цена" каждой комнаты.
     * @return Отфильтрованный список работников.
     */
    private FilteredList<Room> getFilteredList() {
        FilteredList<Room> filteredList = new FilteredList<>(List, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(room -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (String.valueOf(room.getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(room.getCapacity()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(room.getPrice()).contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        choice_box.getItems().addAll(choices);
        choice_box.setValue("Rooms");

        cap.getItems().addAll(0,1,2,3,4);
        cap.setValue(1);

        setObList();

        number_column.setCellValueFactory(new PropertyValueFactory<>("Number"));
        capacity_column.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        price_column.setCellValueFactory(new PropertyValueFactory<>("Price"));

        table.setEditable(true);

        number_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        capacity_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        price_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));

        table.setItems(getSortedList());
    }
}