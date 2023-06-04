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
import javafx.stage.FileChooser;
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
import java.time.temporal.ChronoUnit;
import java.util.*;

import static oop.AddControllers.AddWorkerController.isNumeric;


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
    private TableView<Client> table = new TableView<Client>();

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    ClientService clientService = new ClientService();
    RoomService roomService = new RoomService();
    ReportUpdate reportUpdate = new ReportUpdate();
    ObservableList<Client> List = FXCollections.observableArrayList();

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


    @FXML
    private void search_date() throws MyException {
        try {
            LocalDate arr = arrival.getValue();
            LocalDate dep = departure.getValue();

            if (arrival.getValue() == null || departure.getValue() == null || !arrival.getValue().isBefore(departure.getValue()))
                throw new MyException();

            List.clear();
            List.addAll(clientService.find_clients(arr, dep));
        }
        catch (MyException e) {
            log.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Incorrect input for Date search");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    @FXML
    private void all_clients() {
        List.clear();
        List.addAll(clientService.getClients());
    }


    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getClientsScene(event);
    }
    @FXML
    private void add(ActionEvent event) throws IOException{
            newWindowController.getNewClientWindow();
            if (UpdateStatus.isIsClientAdded()) {
                refreshScreen(event);
                UpdateStatus.setIsClientAdded(false);
            }
            log.info("Client added");
    }

    private void setObList() {
        List.clear();
        List.addAll(clientService.getClients());
    }



    private void remove_row(ActionEvent event) throws MyException, IOException {

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
        }
    }



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
    @FXML
    private void save(ActionEvent event) throws IOException {
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
    @FXML
    private void upload(ActionEvent event) throws IOException {}




    public void toPDF(ActionEvent actionEvent) throws Exception
    {
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


    @FXML
    private void edit(ActionEvent event) throws MyException{
        try {
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
                    refreshScreen(event);
                    UpdateStatus.setIsClientAdded(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (MyException ex){
            log.error("Exception " + ex);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            IOAlert.setContentText("Select row to edit");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    @FXML
    private void change_name(TableColumn.CellEditEvent<Client, String> editEvent) {
        Client selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setName(editEvent.getNewValue());
        clientService.updateClient(selectedPet);
    }
    @FXML
    private void change_surname(TableColumn.CellEditEvent<Client, String> editEvent) {
        Client selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setSurname(editEvent.getNewValue());
        clientService.updateClient(selectedPet);
    }
    @FXML
    private void change_data(TableColumn.CellEditEvent<Client, LocalDate> editEvent) {
//        Client selectedPet = table.getSelectionModel().getSelectedItem();
//        System.out.println(editEvent.getNewValue());
//        selectedPet.setDate_bd(editEvent.getNewValue());
//        clientService.updateClient(selectedPet);
    }
    @FXML
    private void change_arrival(TableColumn.CellEditEvent<Client, LocalDate> editEvent) {
//        Client selectedPet = table.getSelectionModel().getSelectedItem();
//        selectedPet.setDate_arrival(editEvent.getNewValue());
//        clientService.updateClient(selectedPet);
    }

    @FXML
    private void change_departure(TableColumn.CellEditEvent<Client, LocalDate> editEvent) {
//        Client selectedPet = table.getSelectionModel().getSelectedItem();
//        selectedPet.setDate_departure(editEvent.getNewValue());
//        clientService.updateClient(selectedPet);
    }

    @FXML
    private void change_room(TableColumn.CellEditEvent<Client, Room> editEvent) {
//        Client selectedPet = table.getSelectionModel().getSelectedItem();
//        Room room = editEvent.getNewValue();
//        selectedPet.setRoom(room);
//        clientService.updateClient(selectedPet);
    }

    private SortedList<Client> getSortedList() {
        SortedList<Client> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }
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
        name_column.setCellFactory(TextFieldTableCell.<Client>forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.<Client>forTableColumn());
        date_column.setCellFactory(new LocalDateCellFactory_Client());
        arrival_column.setCellFactory(new LocalDateCellFactory_Client_arr());
        departure_column.setCellFactory(new LocalDateCellFactory_Client_dep());
        room_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(roomObservableList));


        table.setItems(getSortedList());
    }
}