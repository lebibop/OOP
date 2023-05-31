package oop.AppClasses;

import oop.Helpers.HibernateUtil;
import oop.Model.Report;
import oop.Model.Room;
import oop.Model.Worker;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class app_room {
    public static void main(String[] args) {

        List<Integer> cap = Arrays.asList(1,2,3,4);
        List<Integer> price = Arrays.asList(1000,2000,5000,7000);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for (int i = 102; i<132; i++) {
            Report t = new Report();
            t.setClients_per_month(0);
            t.setFree_per_month(30);
            t.setBooked_per_month(0);
            Room st = new Room();
            Random rand = new Random();
            st.setId_room(i);
            st.setNumber(i);
            st.setStatus("Free");
            st.setCapacity(cap.get(rand.nextInt(cap.size())));
            st.setPrice(price.get(rand.nextInt(price.size())));

            t.setRoom(st);

            session.save(st);
            session.save(t);
        }

        transaction.commit();
        System.exit(0);

    }
}
