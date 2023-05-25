package oop.oop;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class app_worker {
    public static void main(String[] args){

        List<String> names = Arrays.asList("Andrey", "David", "Anton", "Mikhail", "Vladislav", "Ivan", "Konstantin", "Timur", "Matvey", "Aleksei", "Aleksandr");
        List<String> surnames = Arrays.asList("Ivanov", "Smirnov", "Sidorov", "Popov", "Egorov", "Morozov", "Pavlov", "Zakharov", "Romanov", "Airapetov");
        List<String> positions = Arrays.asList("Doorman", "Receptionist", "Bellboy", "Liftman", "Concierge", "Porter", "Waiter", "Manager");

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        for (int i = 0; i<30; i++) {
            Worker st = new Worker();
            Random rand = new Random();
            st.setName(names.get(rand.nextInt(names.size())));
            st.setSurname(surnames.get(rand.nextInt(surnames.size())));
            st.setDate_bd(LocalDate.of(ThreadLocalRandom.current().nextInt(1960, 2000 + 1), ThreadLocalRandom.current().nextInt(1, 12), ThreadLocalRandom.current().nextInt(1, 28)));
            st.setPosition(positions.get(rand.nextInt(positions.size())));
            st.setExperience(ThreadLocalRandom.current().nextInt(1, 12));
            session.saveOrUpdate(st);
        }

        transaction.commit();
    }

}
