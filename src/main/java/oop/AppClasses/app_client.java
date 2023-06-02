package oop.AppClasses;

import oop.Helpers.HibernateUtil;
import oop.Helpers.ReportUpdate;
import oop.Model.Client;
import oop.Model.Report;
import oop.Model.Room;
import oop.Model.Worker;
import oop.Services.RoomService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class app_client {

    public static void main(String[] args){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        ReportUpdate reportUpdate = new ReportUpdate();

        Client client = new Client();
        client.setName("Klim");
        client.setSurname("Nikolaev");
        client.setDate_bd(LocalDate.of(2003,11,8));
        client.setDate_arrival(LocalDate.of(2023,6,12));
        client.setDate_departure(LocalDate.of(2023,6,17));
        client.setStay_lenght(Math.toIntExact(client.getDate_arrival().until(client.getDate_departure(), ChronoUnit.DAYS)));

        Room room = session.get(Room.class, 13);


        // Проверка свободна ли комната в данные даты?


        // Обновление ОТЧЕТА после добавления клиента
        reportUpdate.update_report_add(client, room);


        session.update(room);
        session.save(client);

        transaction.commit();
        System.exit(0);
    }
}
