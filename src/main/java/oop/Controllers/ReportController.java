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
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import oop.Helpers.CustomIntegerStringConverter;
import oop.Helpers.LocalDateCellFactory_Client;
import oop.Helpers.UpdateStatus;
import oop.Model.Report;
import oop.Model.Room;
import oop.Services.ReportService;
import oop.Services.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;

import static oop.AddControllers.AddWorkerController.isNumeric;


public class ReportController implements Initializable
{
    @FXML
    private TableColumn<Report, Integer> cpm_column;
    @FXML
    private TableColumn<Report, Integer> fpm_column;
    @FXML
    public TableColumn<Report, Integer> bpm_column;
    @FXML
    public TableColumn<Report, Room> room_column;
    private static final Logger log = LoggerFactory.getLogger("Report logger");

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TextField search;

    @FXML
    private TableView<Report> table = new TableView<Report>();

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    ReportService reportService = new ReportService();
    RoomService roomService = new RoomService();
    ObservableList<Report> List = FXCollections.observableArrayList();

    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Workers"))
            SceneController.getWorkersScene(event);
        if (Objects.equals(choice, "Rooms"))
            SceneController.getRoomsScene(event);
        if (Objects.equals(choice, "Clients"))
            SceneController.getClientsScene(event);

    }


    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getReportsScene(event);
    }
    @FXML
    private void add(ActionEvent event) throws IOException {
        log.debug("adding a report");

        newWindowController.getNewReportWindow();
        if(UpdateStatus.isIsReportAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsReportAdded(false);
        }
        log.info("Report added");
    }



    private void setObList() {
        List.clear();
        List.addAll(reportService.getReports());
    }



    private void remove_row(ActionEvent event) throws MyException, IOException {

        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else {
            ObservableList<Report> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Report report : selectedRows) {
                reportService.deleteReport(report);
            }
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

    @FXML
    private void delete(ActionEvent event)
    {
        try {
            log.debug("deleting a Report");
            search_invalid_label.setText("");
            remove_row(event);
            log.info("Report deleted");
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
    private void save(ActionEvent event) throws IOException
    {
        try
        {
            log.debug("saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_report.csv"));
            for(Report reports : List)
            {
                writer.write(reports.getClients_per_month() + ";" + reports.getFree_per_month() + ";"
                        + reports.getBooked_per_month() + ";"
                        + reports.getRoom().getNumber());
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
    private void upload(ActionEvent event) throws IOException
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
            ObservableList<Report> selectedRows = table.getItems();

            for (Report report : selectedRows) {
                reportService.deleteReport(report);
            }
            String temp;
            do{
                temp = reader.readLine();
                System.out.println(temp);
                if(temp!=null) {
                    String[] temp2 = temp.split(";");
                    if (temp2.length == 4) {
                        if (isNumeric(temp2[0]) && isNumeric(temp2[1]) && isNumeric(temp2[2]) && isNumeric(temp2[3])) {
                            Report st = new Report();
                            st.setClients_per_month(Integer.valueOf(temp2[0]));
                            st.setFree_per_month(Integer.valueOf(temp2[1]));
                            st.setBooked_per_month(Integer.valueOf(temp2[2]));
                            st.setRoom(roomService.getRoom_ByNumber(Integer.parseInt(temp2[3])));


                            reportService.createCl(st);
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





    public void toPDF(ActionEvent actionEvent) throws Exception
    {
        try {
            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(4);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            //my_report_table.addCell(new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Clients p/m", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Free p/m", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Booked p/m", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Room", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (List.isEmpty()) throw new MyException();

            for(Report reports : List)
            {
//                table_cell=new PdfPCell(new Phrase(workers.getId_worker()));
//                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(reports.getClients_per_month())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(reports.getFree_per_month())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(reports.getBooked_per_month())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(reports.getRoom().getNumber())));
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

    private SortedList<Report> getSortedList() {
        SortedList<Report> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }
    private FilteredList<Report> getFilteredList() {
        FilteredList<Report> filteredList = new FilteredList<>(List, b -> true);
        System.out.println(1111);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(report -> {
                    System.out.println(2222);
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(report.getRoom().getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(report.getClients_per_month()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(report.getBooked_per_month()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else
                        return String.valueOf(report.getFree_per_month()).toLowerCase().contains(lowerCaseFilter);

                }));
        return filteredList;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        choice_box.getItems().addAll(choices);
        choice_box.setValue("Choose a table");

        setObList();

        //id_column.setCellValueFactory(new PropertyValueFactory<Worker, Integer>("ID"));
        cpm_column.setCellValueFactory(new PropertyValueFactory<>("clients_per_month"));
        fpm_column.setCellValueFactory(new PropertyValueFactory<>("free_per_month"));
        bpm_column.setCellValueFactory(new PropertyValueFactory<>("booked_per_month"));
        room_column.setCellValueFactory(new PropertyValueFactory<>("room"));

//        ObservableList<Room> roomObservableList = FXCollections.observableArrayList(roomService.getRooms());

        table.setEditable(false);
//        cpm_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
//        fpm_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
//        bpm_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
//        room_column.setCellFactory(ChoiceBoxTableCell.forTableColumn());


        table.setItems(getSortedList());
    }
}