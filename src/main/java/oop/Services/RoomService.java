package oop.Services;


import oop.Helpers.HibernateUtil;
import oop.Model.Client;
import oop.Model.Room;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для работы с таблицей Room в базе данных
 * @author lebibop
 */
public class RoomService {

    /**
     * Создает новый номер в базе данных или обновляет существующий.
     * @param room объект, представляющий номер
     * @return true, если операция выполнена успешно, false в противном случае
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public Boolean createRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(room);
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
     * Обновляет данные о номере в базе данных.
     * @param room объект, представляющий номер
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public void updateRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(room);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Получает объект номера по его номеру.
     * @param number номер номера
     * @return объект, представляющий номер, или null, если номер не найден
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public Room getRoom_ByNumber(Integer number) {
        List<Room> t = getRooms();
        for (Room room : t){
            if (Objects.equals(room.getNumber(), number))
                return room;
        }
        return null;
    }

    /**
     * Удаляет объект номера из базы данных.
     * @param room объект, представляющий номер
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public void deleteRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(room);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Получает список всех номеров из базы данных.
     * @return список объектов, представляющих номера
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public List<Room> getRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Room> rooms = session.createQuery("from Room", Room.class).list();
            for(Room room : rooms){
                Hibernate.initialize(room.getReportSet());
                Hibernate.initialize(room.getClientSet());
            }
            return rooms;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * Находит свободные номера в заданный период времени с заданной вместимостью.
     * @param arrival дата заезда
     * @param departure дата выезда
     * @param capacity вместимость номера
     * @return список объектов, представляющих свободные номера
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public List<Room> find_rooms(LocalDate arrival, LocalDate departure, Integer capacity){
        java.util.List<Room> r = getRooms();
        java.util.List<Room> g = new ArrayList<>();
        for (Room rr : r){
            if (!Objects.equals(rr.getCapacity(), capacity) && capacity!=0)
                continue;

            boolean flag = true;
            for (Client c : rr.getClientSet()) {
                if ( (c.getDate_arrival().isBefore(arrival) && c.getDate_departure().isAfter(arrival)) ||
                        c.getDate_arrival().isEqual(arrival) ||
                        (c.getDate_arrival().isAfter(arrival) && c.getDate_arrival().isBefore(departure)))
                {
                    flag = false;
                    break;
                }
            }
            if (flag) g.add(rr);
        }
        return g;
    }

    /**
     * Находит свободные номера в заданный период времени с заданной вместимостью + номер, занятый текущим клиентом.
     * @param client текущий клиент
     * @param arrival дата заезда
     * @param departure дата выезда
     * @param capacity вместимость номера
     * @return список объектов, представляющих свободные номера
     * @throws HibernateException если возникла ошибка при работе с Hibernate
     */
    public List<Room> find_rooms_edit(Client client, LocalDate arrival, LocalDate departure, Integer capacity){
        java.util.List<Room> r = getRooms();
        java.util.List<Room> g = new ArrayList<>();
        for (Room rr : r){
            if (!Objects.equals(rr.getCapacity(), capacity))
                continue;

            boolean flag = true;
            for (Client c : rr.getClientSet()) {
                if (c.equals(client))
                    continue;
                if ( (c.getDate_arrival().isBefore(arrival) && c.getDate_departure().isAfter(arrival)) ||
                        c.getDate_arrival().isEqual(arrival) ||
                        (c.getDate_arrival().isAfter(arrival) && c.getDate_arrival().isBefore(departure))) {
                    flag = false;
                    break;
                }
            }
            if (flag) g.add(rr);
        }
        return g;
    }
}