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
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import oop.Model.Report;
import oop.Model.Room;
import oop.Services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Контроллер для таблицы отчетов.
 * @author lebibop
 */
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
    private ChoiceBox<Month> choice_box_month;

    @FXML
    private TextField search;

    @FXML
    private TableView<Report> table = new TableView<>();

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    ReportService reportService = new ReportService();
    ObservableList<Report> List = FXCollections.observableArrayList();

    /**
     * Обработчик события выбора значения в выпадающем списке.
     * Получает выбранное значение и вызывает соответствующий метод в классе SceneController для отображения соответствующей сцены.
     * @param event событие выбора значения в выпадающем списке
     * @throws IOException если возникает ошибка ввода-вывода при отображении сцены
     */
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

    /**
     * Обработчик события выбора месяца в выпадающем списке.
     * Очищает список отчетов и добавляет в него отчеты за выбранный месяц.
     */
    @FXML
    private void getChoices_month() {
        log.debug("getting reports");
        List.clear();
        List.addAll(reportService.getReports(choice_box_month.getValue().getValue()));
        choice_box_month.setValue(choice_box_month.getValue());
        log.info("getting is done");
    }

    /**
     * Устанавливает список отчетов.
     * Добавляет в список отчеты за предыдущий месяц.
     */
    private void setObList() {
        log.debug("Adding reports");
        List.clear();
        List.addAll(reportService.getReports(LocalDate.now().minusMonths(1).getMonthValue()));
        log.info("Adding is done");
    }

    /**
     * Обработчик события нажатия на кнопку сохранения отчета в файл.
     * Сохраняет отчет в формате CSV в файл "save_report.csv" в папке "saves".
     */
    @FXML
    private void save() {
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

    /**
     * Обработчик события нажатия на кнопку сохранения таблицы отчетов в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_report.pdf".
     * Если список работников пуст, выбрасывает исключение MyException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    public void toPDF() throws Exception
    {
        try {
            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("pdf/report_report.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(4);
            my_report_table.setWidthPercentage(100f);
            my_report_table.setTotalWidth(PageSize.A4.getWidth() - my_pdf_report.leftMargin() - my_pdf_report.rightMargin()); // устанавливаем фиксированную ширину таблицы
            my_report_table.setLockedWidth(true);

            PdfPCell headerCell = new PdfPCell(new Phrase("REPORT by " + choice_box_month.getValue()));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setColspan(4);
            headerCell.setPaddingBottom(10);
            my_report_table.addCell(headerCell);

            PdfPCell table_cell;

            my_report_table.addCell(new PdfPCell(new Phrase("Clients p/m", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Free p/m", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Booked p/m", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Room", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (List.isEmpty()) throw new Exception();

            for(Report reports : List)
            {
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
        catch (FileNotFoundException | DocumentException e)
        {
            log.warn("Exception " + e);
            e.printStackTrace();
        }
    }

    /**
     * Метод для получения отфильтрованного и отсортированного списка отчетов.
     * Создает новый отфильтрованный список на основе исходного списка отчетов, используя фильтр из searchField.
     * Затем создает новый отсортированный список на основе отфильтрованного списка и связывает его с компаратором таблицы.
     * @return Отсортированный список отчетов.
     */
    private SortedList<Report> getSortedList() {
        SortedList<Report> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка отчетов на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка отчетов, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "Номер комнаты", "Количество клиентов за месяц", "Занятые дни за месяц", "Свободные дни за месяц" каждого отчета.
     * @return Отфильтрованный список отчетов.
     */
    private FilteredList<Report> getFilteredList() {
        FilteredList<Report> filteredList = new FilteredList<>(List, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(report -> {
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
        choice_box.setValue("Reports");

        choice_box_month.getItems().addAll(Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL, Month.MAY,
                Month.JUNE, Month.JULY, Month.AUGUST, Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER);
        choice_box_month.setValue(LocalDate.now().minusMonths(1).getMonth());

        setObList();

        cpm_column.setCellValueFactory(new PropertyValueFactory<>("clients_per_month"));
        fpm_column.setCellValueFactory(new PropertyValueFactory<>("free_per_month"));
        bpm_column.setCellValueFactory(new PropertyValueFactory<>("booked_per_month"));
        room_column.setCellValueFactory(new PropertyValueFactory<>("room"));

        table.setEditable(false);
//        cpm_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
//        fpm_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
//        bpm_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
//        room_column.setCellFactory(ChoiceBoxTableCell.forTableColumn());

        table.setItems(getSortedList());
    }
}