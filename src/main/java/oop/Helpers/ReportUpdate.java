package oop.Helpers;

import oop.Model.Client;
import oop.Model.Report;
import oop.Model.Room;

import java.time.LocalDate;
import java.time.Month;

public final class ReportUpdate {
    public void update_report_add(Client client, Room room){
        LocalDate arrivalDate = client.getDate_arrival();
        LocalDate departureDate = client.getDate_departure();

        Report report;
        if (arrivalDate.getMonth() == departureDate.getMonth()){
            report = room.getReportSet().get(arrivalDate.getMonth().getValue() - 1);
            report.setClients_per_month(report.getClients_per_month() + room.getCapacity());
            report.setBooked_per_month(report.getBooked_per_month() + client.getStay_lenght());
            report.setFree_per_month(report.getFree_per_month() - report.getBooked_per_month());

            System.out.println(report);

            client.setRoom(room);
            room.addClient(client);
        }

        if (arrivalDate.getMonth() != departureDate.getMonth()){
            Month arrival = arrivalDate.getMonth();
            Month departure = departureDate.getMonth();

            if (departure.getValue() - arrival.getValue() > 1){
                for (int i = arrival.getValue(); i<departure.getValue() - 1; i++) {
                    Report rep = room.getReportSet().get(i);

                    rep.setClients_per_month(rep.getClients_per_month() + room.getCapacity());
                    int temp = rep.getBooked_per_month();
                    rep.setBooked_per_month(rep.getFree_per_month());
                    rep.setFree_per_month(temp);
                }
            }


            Report report_arr = room.getReportSet().get(arrival.getValue() - 1);
            Report report_dep = room.getReportSet().get(departure.getValue() - 1);

            report_arr.setClients_per_month(report_arr.getClients_per_month() + room.getCapacity());
            report_arr.setBooked_per_month(report_arr.getBooked_per_month() + (arrival.maxLength() - arrivalDate.getDayOfMonth()));
            report_arr.setFree_per_month(report_arr.getFree_per_month() - (arrival.maxLength() - arrivalDate.getDayOfMonth()));

            report_dep.setClients_per_month(report_dep.getClients_per_month() + room.getCapacity());
            report_dep.setBooked_per_month(report_dep.getBooked_per_month() + departureDate.getDayOfMonth());
            report_dep.setFree_per_month(report_dep.getFree_per_month() - departureDate.getDayOfMonth());



            client.setRoom(room);
            room.addClient(client);
        }
    }
}
