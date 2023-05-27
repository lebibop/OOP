package oop.AppClasses;

import oop.Helpers.HibernateUtil;
import oop.Model.Client;
import oop.Model.Report;
import oop.Model.Room;
import oop.Model.Worker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class app_client {
    public static void main(String[] args){
//        Client st = new Client();
//
//        st.setId_client(1);
//        st.setName("Klim");
//        st.setSurname("Nikolaev");
//        st.setDate_bd(LocalDate.of(2003,11,8));
//        st.setDate_arrival(LocalDate.of(2023,5,20));
//        st.setDate_departure(LocalDate.of(2023,5,29));
//        st.setStay_lenght(Math.toIntExact(st.getDate_arrival().until(st.getDate_departure(), ChronoUnit.DAYS)));
//        System.out.println(st.toString());


//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction transaction = session.beginTransaction();

        Client client = new Client();
        client.setName("Klim");
        client.setSurname("Nikolaev");
        client.setDate_bd(LocalDate.of(2003,11,8));
        client.setDate_arrival(LocalDate.of(2023,5,20));
        client.setDate_departure(LocalDate.of(2023,5,29));
        client.setStay_lenght(Math.toIntExact(client.getDate_arrival().until(client.getDate_departure(), ChronoUnit.DAYS)));
        System.out.println(client.toString());

        Room room = new Room();
        room.setNumber(134);
        room.setStatus("Free");
        room.setCapacity(3);
        room.setPrice(3500);
        System.out.println(room.toString());

        Room room1 = new Room();
        room1.setNumber(22);
        room1.setStatus("Free");
        room1.setCapacity(44);
        room1.setPrice(654);
        System.out.println(room.toString());


        Report report = new Report();
        report.setClients_per_month(100);
        report.setBooked_per_month(1);
        report.setFree_per_month(65);

        Report report1 = new Report();
        report1.setClients_per_month(21);
        report1.setBooked_per_month(12);
        report1.setFree_per_month(77);


//        room.setReport(report);
//        room1.setReport(report1);
//        client.setRoom(room);

        report.setRoom(room);
        client.setRoom(room);


        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        session.saveOrUpdate(room);
        session.saveOrUpdate(report);
        session.saveOrUpdate(client);
//        session.saveOrUpdate(room);
//        session.saveOrUpdate(room1);
//        session.saveOrUpdate(client);

        transaction.commit();
        System.exit(0);
    }
}
