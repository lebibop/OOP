package oop.Helpers;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import oop.Model.Client;
import oop.Services.ClientService;
import java.sql.Date;
import java.time.LocalDate;


public class LocalDateCellFactory_Client_dep implements Callback<TableColumn<Client, LocalDate>, TableCell<Client, LocalDate>> {
    ClientService clientService = new ClientService();
    @Override
    public TableCell<Client, LocalDate> call(TableColumn<Client, LocalDate> col) {
        return new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.editableProperty().set(false);
                datePicker.setOnAction((e) -> commitEdit(datePicker.getValue()));
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
                Client data = getTableView().getColumns().get(0).getTableView().getItems().get(getIndex());
                data.setDate_departure(Date.valueOf(newValue).toLocalDate());
//                System.out.println(data.getDate_bd());
                clientService.updateClient(data);
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
