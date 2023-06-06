package oop.Services;


import oop.Helpers.HibernateUtil;
import oop.Model.Worker;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с таблицей Worker в базе данных
 * @author lebibop
 */
public class WorkerService {

    /**
     * Создает нового работника в базе данных или обновляет существующего.
     * @param worker объект, представляющий работника
     * @return true, если транзакция была успешно завершена, иначе false
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public Boolean createWorker(Worker worker) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(worker);
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
     * Обновляет данные о работнике в базе данных.
     * @param worker объект, представляющий работника
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public void updateWorker(Worker worker) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(worker);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Удаляет данные о работнике из базы данных.
     * @param worker объект, представляющий работника
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public void deleteWorker(Worker worker) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(worker);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Получает список всех работников из базы данных.
     * @return список объектов, представляющих работников
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public List<Worker> getWorkers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Worker", Worker.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

}