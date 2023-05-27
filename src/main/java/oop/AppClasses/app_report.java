package oop.AppClasses;

import oop.Model.Report;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class app_report {
    public static void main(String[] args){
        Report st = new Report();

        st.setId_report(1);
        st.setClients_per_month(10);
        st.setBooked_per_month(14);
        st.setFree_per_month(16);
        System.out.println(st.toString());

    }
}
