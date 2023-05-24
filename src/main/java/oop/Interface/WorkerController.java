package oop.Interface;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import oop.oop.UpdateStatus;
import oop.oop.Worker;
import oop.oop.WorkerService;
import oop.oop.newWindowController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WorkerController implements Initializable
{
    @FXML
    public TableColumn<Worker, String> date_column;
    @FXML
    public TableColumn<Worker, String> position_column;
    @FXML
    public TableColumn<Worker, String> exp_column;
    private static final Logger log = LoggerFactory.getLogger("Worker logger");



    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableColumn<Worker, String> name_column;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Worker, String> surname_column;

    @FXML
    private TableView<Worker> table;

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    WorkerService workerService = new WorkerService();
    ObservableList<Worker> List = FXCollections.observableArrayList();




//    @FXML
//    private void initialize() {
//        setTexts();
//        setObList();
//        fillTable();
//        addTableSettings();
//        exitBtn.setOnAction(SceneController::close);
//    }



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



    private void remove_row() throws MyException
    {
        table.setItems(List);
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else table.getItems().remove(selectedID);
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
            remove_row();
            log.info("worker deleted");
        }
        catch (MyException myEx){
            log.warn("Exception " + myEx);
            search_invalid_label.setText("Choose a row to delete");
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
                writer.write(workers.getId_worker() + ";" + workers.getName() + ";" + workers.getSurname() + ";"
                        + workers.getDate_bd() + ";" + workers.getPosition() + ";" + workers.getExperience());
                writer.newLine();
            }
            writer.close();
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
        try
        {
            log.debug("uploading to file");
            BufferedReader reader = new BufferedReader(new FileReader("saves/save.csv"));
            String temp;
            List.clear();
            do{
                temp = reader.readLine();
                if(temp!=null){
                    String[] temp2 = temp.split(";");
                    String[] words = temp2[3].split("-");
                    List.add(new Worker(Integer.parseInt(temp2[0]),temp2[1],temp2[2], LocalDate.of(Integer.parseInt(words[2]), Integer.parseInt(words[1]), Integer.parseInt(words[0])),temp2[4],Integer.parseInt(temp2[5])));
                }
            }
            while(temp!=null);
            table.setItems(List);
            reader.close();
            log.info("uploaded to file");
        }
        catch (IOException e)
        {
            log.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
            IOAlert.setContentText("Error");
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
                table_cell=new PdfPCell(new Phrase(workers.getExperience()));
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
                    } else if (worker.getDate_bd().toString().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (worker.getSurname().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(worker.getExperience()).contains(lowerCaseFilter);
                }));
        return filteredList;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        choice_box.getItems().addAll(choices);
        choice_box.setValue("Choose a table");

        setObList();

        //id_column.setCellValueFactory(new PropertyValueFactory<Worker, Integer>("ID"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("Date_bd"));
        position_column.setCellValueFactory(new PropertyValueFactory<>("Position"));
        exp_column.setCellValueFactory(new PropertyValueFactory<>("Experience"));





        System.out.println(getSortedList());
        table.setItems(getSortedList());
//        table.setEditable(true);
//        name_column.setCellFactory(TextFieldTableCell.<Worker>forTableColumn());
//        surname_column.setCellFactory(TextFieldTableCell.<Worker>forTableColumn());
//        position_column.setCellFactory(TextFieldTableCell.<Worker>forTableColumn());

//        search();
    }





}

