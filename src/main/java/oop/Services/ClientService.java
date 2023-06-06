package oop.Services;


import oop.Helpers.HibernateUtil;
import oop.Model.Client;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с таблицей Client в базе данных
 * @author lebibop
 */
public class ClientService {
    /**
     * Создает нового клиента или обновляет существующего в базе данных.
     * @param client объект, представляющий клиента
     * @return true, если операция выполнена успешно, false в противном случае
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
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

    /**
     * Обновляет информацию о клиенте в базе данных.
     * @param client объект, представляющий клиента с обновленными данными
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
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

    /**
     * Удаляет информацию о клиенте из базы данных.
     * @param client объект, представляющий клиента, которого нужно удалить
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
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

    /**
     * Возвращает список всех клиентов из базы данных.
     * @return список объектов, представляющих клиентов
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public List<Client> getClients() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Client", Client.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Находит всех клиентов, чьи даты прибытия и отъезда входят в интервал с заданными датами.
     * @param arrival дата прибытия
     * @param departure дата отъезда
     * @return список объектов, представляющих клиентов, чьи даты прибытия и отъезда входят в интервал с заданными датами
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
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