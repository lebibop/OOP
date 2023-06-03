package oop.Services;


import oop.Helpers.HibernateUtil;
import oop.Helpers.ReportUpdate;
import oop.Model.Client;
import oop.Model.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientService {

    public Boolean createClient(Client client) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(client);
            transaction.commit();
            return transaction.getStatus() == TransactionStatus.COMMITTED;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        return false;
    }

    public Boolean createCl(Client client) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(client);
            transaction.commit();
            return transaction.getStatus() == TransactionStatus.COMMITTED;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        return false;
    }

    public void updateClient(Client client) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(client);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public void deleteClient(Client client) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(client);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    public Client getClient(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Client.class, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Client> getClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Client", Client.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Client> find_clients(LocalDate arrival, LocalDate departure){
        java.util.List<Client> r = getClients();
        java.util.List<Client> g = new ArrayList<>();
        for (Client rr : r){
            if (!rr.getDate_arrival().isBefore(arrival) && !rr.getDate_departure().isAfter(departure))
                g.add(rr);
            }
        return g;
    }
}