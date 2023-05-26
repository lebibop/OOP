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
import javafx.scene.control.Label;
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
    private TableView<Worker> table = new TableView<Worker>();

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    WorkerService workerService = new WorkerService();
    ObservableList<Worker> List = FXCollections.observableArrayList();

    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Clients"))
            SceneController.getClientsScene(event);

    }


    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getWorkersScene(event);
    }
    @FXML
    private void add(ActionEvent event) throws IOException {
        log.debug("adding a worker");

        newWindowController.getNewPetWindow();
        if(UpdateStatus.isIsWorkerAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsWorkerAdded(false);
        }
        log.info("worker added");
    }



    private void setObList() {
        List.clear();
        List.addAll(workerService.getWorkers());
    }



    private void remove_row(ActionEvent event) throws MyException, IOException {

        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else {
            ObservableList<Worker> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Worker worker : selectedRows) {
                workerService.deleteWorker(worker);
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
            log.debug("deleting a worker");
            search_invalid_label.setText("");
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
    @FXML
    private void save(ActionEvent event) throws IOException
    {
        try
        {
            log.debug("saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save.csv"));
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





    public void toPDF(ActionEvent actionEvent) throws Exception
    {
        try {
            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(5);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            //my_report_table.addCell(new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Surname", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Date", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Position", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Experience", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));

            if (List.isEmpty()) throw new MyException();

            for(Worker workers : List)
            {
//                table_cell=new PdfPCell(new Phrase(workers.getId_worker()));
//                my_report_table.addCell(table_cell);
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

    @FXML
    private void change_name(TableColumn.CellEditEvent<Worker, String> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setName(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }
    @FXML
    private void change_surname(TableColumn.CellEditEvent<Worker, String> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setSurname(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }

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

    @FXML
    private void change_pos(TableColumn.CellEditEvent<Worker, String> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setPosition(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }

    @FXML
    private void change_data(TableColumn.CellEditEvent<Worker, LocalDate> editEvent) {
        Worker selectedPet = table.getSelectionModel().getSelectedItem();
        selectedPet.setDate_bd(editEvent.getNewValue());
        workerService.updateWorker(selectedPet);
    }




    private SortedList<Worker> getSortedList() {
        SortedList<Worker> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }
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

    private String date_converter(String temp){
        String[] temp2 = temp.split("-");
        return temp2[2] + '.' + temp2[1] + '.' + temp2[0];
    }







    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        choice_box.getItems().addAll(choices);
        choice_box.setValue("Choose a table");

        setObList();

        //id_column.setCellValueFactory(new PropertyValueFactory<Worker, Integer>("ID"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("Date_bd"));
        position_column.setCellValueFactory(new PropertyValueFactory<>("Position"));
        exp_column.setCellValueFactory(new PropertyValueFactory<>("Experience"));


        table.setEditable(true);
        name_column.setCellFactory(TextFieldTableCell.<Worker>forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.<Worker>forTableColumn());
        date_column.setCellFactory(new LocalDateCellFactory());
        position_column.setCellFactory(ChoiceBoxTableCell.forTableColumn("Doorman", "Receptionist", "Bellboy", "Liftman", "Concierge", "Porter", "Waiter", "Manager"));
        exp_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));






        table.setItems(getSortedList());

    }





}

