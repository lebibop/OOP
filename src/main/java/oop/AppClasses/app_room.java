package oop.AppClasses;

import oop.Helpers.HibernateUtil;
import oop.Model.Report;
import oop.Model.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class app_room {
    public static void main(String[] args) {

        List<Integer> cap = Arrays.asList(1,2,3,4);
        List<Integer> price = Arrays.asList(1000,2000,5000,7000);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for (int i = 100; i<111; i++) {
            Report january = new Report();
            january.setMonth(1);
            january.setClients_per_month(0);
            january.setFree_per_month(31);
            january.setBooked_per_month(0);

            Report february = new Report();
            february.setMonth(2);
            february.setClients_per_month(0);
            february.setFree_per_month(28);
            february.setBooked_per_month(0);

            Report march = new Report();
            march.setMonth(3);
            march.setClients_per_month(0);
            march.setFree_per_month(31);
            march.setBooked_per_month(0);

            Report april = new Report();
            april.setMonth(4);
            april.setClients_per_month(0);
            april.setFree_per_month(30);
            april.setBooked_per_month(0);

            Report may = new Report();
            may.setMonth(5);
            may.setClients_per_month(0);
            may.setFree_per_month(31);
            may.setBooked_per_month(0);

            Report june = new Report();
            june.setMonth(6);
            june.setClients_per_month(0);
            june.setFree_per_month(30);
            june.setBooked_per_month(0);

            Report july = new Report();
            july.setMonth(7);
            july.setClients_per_month(0);
            july.setFree_per_month(31);
            july.setBooked_per_month(0);

            Report august = new Report();
            august.setMonth(8);
            august.setClients_per_month(0);
            august.setFree_per_month(31);
            august.setBooked_per_month(0);

            Report september = new Report();
            september.setMonth(9);
            september.setClients_per_month(0);
            september.setFree_per_month(30);
            september.setBooked_per_month(0);

            Report october = new Report();
            october.setMonth(10);
            october.setClients_per_month(0);
            october.setFree_per_month(31);
            october.setBooked_per_month(0);

            Report november = new Report();
            november.setMonth(11);
            november.setClients_per_month(0);
            november.setFree_per_month(30);
            november.setBooked_per_month(0);

            Report december = new Report();
            december.setMonth(12);
            december.setClients_per_month(0);
            december.setFree_per_month(31);
            december.setBooked_per_month(0);

            Room room = new Room();
            Random rand = new Random();
            room.setNumber(i);
            room.setCapacity(cap.get(rand.nextInt(cap.size())));
            room.setPrice(price.get(rand.nextInt(price.size())));

            january.setRoom(room);
            february.setRoom(room);
            march.setRoom(room);
            april.setRoom(room);
            may.setRoom(room);
            june.setRoom(room);
            july.setRoom(room);
            august.setRoom(room);
            september.setRoom(room);
            october.setRoom(room);
            november.setRoom(room);
            december.setRoom(room);
            
            room.addReport(january);
            room.addReport(february);
            room.addReport(march);
            room.addReport(april);
            room.addReport(may);
            room.addReport(june);
            room.addReport(july);
            room.addReport(august);
            room.addReport(september);
            room.addReport(october);
            room.addReport(november);
            room.addReport(december);

            System.out.println(room.getReportSet());

            session.save(room);
            session.save(january);
            session.save(february);
            session.save(march);
            session.save(april);
            session.save(may);
            session.save(june);
            session.save(july);
            session.save(august);
            session.save(september);
            session.save(october);
            session.save(november);
            session.save(december);
        }

        transaction.commit();
        System.exit(0);

    }
}
