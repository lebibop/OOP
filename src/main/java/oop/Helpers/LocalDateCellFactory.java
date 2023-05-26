package oop.Helpers;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import oop.Model.Worker;
import oop.Services.WorkerService;

import java.time.LocalDate;
import java.sql.Date;



public class LocalDateCellFactory implements Callback<TableColumn<Worker, LocalDate>, TableCell<Worker, LocalDate>> {
    WorkerService workerService = new WorkerService();
    @Override
    public TableCell<Worker, LocalDate> call(TableColumn<Worker, LocalDate> col) {
        return new TableCell<Worker, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.editableProperty().set(false);
                datePicker.setOnAction((e) -> {
                    commitEdit(datePicker.getValue());
                });
                this.setGraphic(datePicker);
            }

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                }
            }

            @Override
            public void commitEdit(LocalDate newValue) {
                super.commitEdit(newValue);
                Worker data = getTableView().getColumns().get(0).getTableView().getItems().get(getIndex());
                data.setDate_bd(Date.valueOf(newValue).toLocalDate());
//                System.out.println(data.getDate_bd());
                workerService.updateWorker(data);
            }

            @Override
            public void startEdit() {
                super.startEdit();
                LocalDate value = getItem();
                if (value != null) {
                    datePicker.setValue(value);
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
            }
        };
    }
}
