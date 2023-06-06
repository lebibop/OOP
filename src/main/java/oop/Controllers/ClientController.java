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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oop.AddControllers.EditClientController;
import oop.Helpers.*;
import oop.Model.Client;
import oop.Model.Room;
import oop.Services.ClientService;
import oop.Services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;


/**
 * Контроллер для таблицы клиентов.
 * @author lebibop
 */
public class ClientController implements Initializable
{
    @FXML
    private TableColumn<Client, String> name_column;
    @FXML
    private TableColumn<Client, String> surname_column;
    @FXML
    public TableColumn<Client, LocalDate> date_column;
    @FXML
    public TableColumn<Client, LocalDate> arrival_column;
    @FXML
    public TableColumn<Client, LocalDate> departure_column;
    @FXML
    public TableColumn<Client, Integer> stay_column;
    @FXML
    public TableColumn<Client, Room> room_column;
    private static final Logger log = LoggerFactory.getLogger("Client logger");

    @FXML
    private DatePicker arrival;
    @FXML
    private DatePicker departure;

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TextField search;

    @FXML
    private TableView<Client> table = new TableView<>();

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    ClientService clientService = new ClientService();
    RoomService roomService = new RoomService();
    ReportUpdate reportUpdate = new ReportUpdate();
    ObservableList<Client> List = FXCollections.observableArrayList();

    /**
     * Обработчик события выбора элемента в выпадающем списке.
     * Определяет выбранный элемент и вызывает соответствующий метод SceneController для отображения соответствующей сцены.
     * @param event Событие выбора элемента в выпадающем списке.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Workers"))
            SceneController.getWorkersScene(event);
        if (Objects.equals(choice, "Rooms"))
            SceneController.getRoomsScene(event);
        if (Objects.equals(choice, "Reports"))
            SceneController.getReportsScene(event);

    }

    static class MyException extends Exception
    {
        public MyException()
        {
            super("Choose a row to delete");
        }
    }

    /**
     * Выполняет поиск клиентов на основе выбранных дат заезда и выезда.
     * Если даты недействительны, отображается сообщение об ошибке.
     */
    @FXML
    private void search_date() {
        try {
            log.debug("searching by date");
            LocalDate arr = arrival.getValue();
            LocalDate dep = departure.getValue();

            if (arrival.getValue() == null || departure.getValue() == null || !arrival.getValue().isBefore(departure.getValue()))
                throw new MyException();

            List.clear();
            List.addAll(clientService.find_clients(arr, dep));
            log.info("searching is done");
        }
        catch (MyException e) {
            log.warn("Exception " + "Incorrect input for Date search");
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
     * Обработчик события отображения всех клиентов.
     * Вызывает метод сервиса получения всех клиентов и добавляет их в список для отображения в таблице.
     */
    @FXML
    private void all_clients() {
        log.debug("getting all clients");
        List.clear();
        List.addAll(clientService.getClients());
        log.info("getting is done");
    }

    /**
     * Обработчик события обновления экрана.
     * Вызывает метод SceneController для отображения сцены с номерами.
     * @param event Событие обновления экрана.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getClientsScene(event);
    }

    /**
     * Обработчик события добавления нового клиента.
     * Вызывает метод NewWindowController для отображения окна добавления нового клиента.
     * Если номер был успешно добавлен, обновляет экран с клиентами.
     * @param event Событие добавления нового клиента.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void add(ActionEvent event) throws IOException{
        log.debug("adding client");
            newWindowController.getNewClientWindow();
            if (UpdateStatus.isIsClientAdded()) {
                log.info("Client added");
                refreshScreen(event);
                UpdateStatus.setIsClientAdded(false);
            }

    }

    /**
     * Метод для установки списка клиентов в таблицу.
     * Очищает список, затем добавляет в него всех клиентов из сервиса.
     */
    private void setObList() {
        log.debug("getting clients");
        List.clear();
        List.addAll(clientService.getClients());
        log.info("getting is done");
    }


    /**
     * Обработчик события удаления выбранных клиентов из таблицы.
     * Удаляет выбранных клиентов из сервиса.
     * Если ни один клиент не выбран, выбрасывает исключение MyException.
     * После удаления клиентов обновляет экран с клиентами.
     * @param event Событие удаления номеров.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    private void remove_row(ActionEvent event) throws MyException, IOException {
        log.debug("removing a row");
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else {
            ObservableList<Client> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Client client : selectedRows) {
                reportUpdate.update_report_delete(client, roomService.getRoom_ByNumber(client.getRoom().getNumber()));
                roomService.updateRoom(roomService.getRoom_ByNumber(client.getRoom().getNumber()));
                clientService.deleteClient(client);
            }
            refreshScreen(event);
            log.info("removing is done");
        }
    }


    /**
     * Обработчик события удаления выбранных клиентов из таблицы.
     * Удаляет выбранных клиентов из сервиса.
     * Если ни один клиент не выбран, выбрасывает исключение MyException.
     * После удаления клиентов обновляет экран с клиентами.
     * @param event Событие удаления клиента.
     */
    @FXML
    private void delete(ActionEvent event)
    {
        try {
            log.debug("deleting a Client");
            search_invalid_label.setText("");
            remove_row(event);
            log.info("Client deleted");
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
     * Обработчик события сохранения списка клиентов в файл.
     * Сохраняет список клиентов в файл "saves/save.csv".
     * Если произошла ошибка ввода-вывода при сохранении, выбрасывает исключение IOException.
     * После сохранения открывает папку "saves".
     */
    @FXML
    private void save() {
        try {
            log.debug("saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_client.csv"));
            for(Client clients : List) {
                writer.write(clients.getName() + ";" + clients.getSurname() + ";"
                        + clients.getDate_bd() + ";" + clients.getDate_arrival() + ";"
                        + clients.getDate_departure() + ";"
                        + clients.getRoom().getNumber());
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

    /**
     * Обработчик события нажатия на кнопку сохранения таблицы клиентов в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_client.pdf".
     * Если список КЛИЕНТОВ пуст, выбрасывает исключение MyException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    public void toPDF() {
        try {
            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("pdf/report_client.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(7);
            my_report_table.setWidthPercentage(100f);
            my_report_table.setTotalWidth(PageSize.A4.getWidth() - my_pdf_report.leftMargin() - my_pdf_report.rightMargin()); // устанавливаем фиксированную ширину таблицы
            my_report_table.setLockedWidth(true);

            PdfPCell headerCell = new PdfPCell(new Phrase("Clients REPORT"));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setColspan(7);
            headerCell.setPaddingBottom(10);
            my_report_table.addCell(headerCell);

            PdfPCell table_cell;

            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Surname", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Date", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Arrival date", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Departure date", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Lenght stay", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Room", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (List.isEmpty()) throw new MyException();

            for(Client clients : List)
            {
                table_cell=new PdfPCell(new Phrase(clients.getName()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(clients.getSurname()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(clients.getDate_bd())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(clients.getDate_arrival())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(clients.getDate_departure())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(clients.getStay_lenght())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(clients.getRoom().getNumber())));
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
     * Обрабатывает событие нажатия на кнопку редактирования.
     * Если в таблице выбрана строка, открывает новое окно с выбранным элементом для редактирования.
     * Если строка не выбрана, отображает сообщение об ошибке.
     * @param event Событие действия, вызванное нажатием кнопки редактирования.
     */
    @FXML
    private void edit(ActionEvent event) {
        try {
            log.debug("editing a row");
            int selectedID = table.getSelectionModel().getSelectedIndex();
            if (selectedID == -1) throw new MyException();
            Client selectedItem = table.getSelectionModel().getSelectedItem();
            // вызываем окно редактирования объекта
            try {
                // загрузка FXML-файла с макетом окна редактирования
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/editClient.fxml"));
                Parent root = loader.load();
                // получение контроллера для окна редактирования
                EditClientController editController = loader.getController();
                // передаем выбранный элемент в контроллер
                editController.setEditedObject(selectedItem);

                // создание нового окна
                Stage editStage = new Stage();
                editStage.setTitle("Edit object");
                editStage.setScene(new Scene(root));
                // отображение нового окна как модального
                editStage.initModality(Modality.APPLICATION_MODAL);
                editStage.showAndWait();
                if (UpdateStatus.isIsClientAdded()) {
                    log.info("editing is done");
                    refreshScreen(event);
                    UpdateStatus.setIsClientAdded(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (MyException ex){
            log.error("Exception " + "Select row to edit");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            IOAlert.setContentText("Select row to edit");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    /**
     * Возвращает отсортированный список клиентов с учетом фильтрации и текущего порядка сортировки в таблице.
     * @return Отсортированный список клиентов.
     */
    private SortedList<Client> getSortedList() {
        SortedList<Client> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка клиентов на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка клиентов, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "имя", "фамилия", "дата рождения", "дата прибытия", "дата отбытия" и "номер комнаты" каждой комнаты.
     * @return Отфильтрованный список работников.
     */
    private FilteredList<Client> getFilteredList() {
        FilteredList<Client> filteredList = new FilteredList<>(List, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(client -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    if (String.valueOf(client.getRoom().getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (client.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (date_converter(client.getDate_bd().toString()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (date_converter(client.getDate_departure().toString()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (date_converter(client.getDate_arrival().toString()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (client.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(client.getStay_lenght()).contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    private String date_converter(String temp){
        String[] temp2 = temp.split("-");
        return temp2[2] + '.' + temp2[1] + '.' + temp2[0];
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        choice_box.getItems().addAll(choices);
        choice_box.setValue("Clients");

        setObList();

        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("Date_bd"));
        arrival_column.setCellValueFactory(new PropertyValueFactory<>("Date_arrival"));
        departure_column.setCellValueFactory(new PropertyValueFactory<>("Date_departure"));
        stay_column.setCellValueFactory(new PropertyValueFactory<>("stay_lenght"));
        room_column.setCellValueFactory(new PropertyValueFactory<>("room"));

        ObservableList<Room> roomObservableList = FXCollections.observableArrayList(roomService.getRooms());

        table.setEditable(false);
        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.forTableColumn());
        date_column.setCellFactory(new LocalDateCellFactory_Client());
        arrival_column.setCellFactory(new LocalDateCellFactory_Client_arr());
        departure_column.setCellFactory(new LocalDateCellFactory_Client_dep());
        room_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(roomObservableList));


        table.setItems(getSortedList());
    }
}