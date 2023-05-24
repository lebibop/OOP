package oop.oop;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.sql.Date;

public class app_worker {
    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence-Hotel");
        EntityManager em = emf.createEntityManager();

        System.out.println("Test started");
        em.getTransaction().begin();
        Worker st = new Worker();

        st.setName("Greg");
        st.setSurname("Bob");
        st.setDate_bd(LocalDate.of(2000, 4, 9));
        st.setPosition("Director");
        st.setExperience(18);

        em.persist(st);
        em.getTransaction().commit();

        System.out.println("New Worker ID is " + st.getId_worker());
    }

}
