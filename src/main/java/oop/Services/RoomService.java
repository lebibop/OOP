package oop.Services;


import oop.Helpers.HibernateUtil;
import oop.Model.Client;
import oop.Model.Room;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RoomService {

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

    public Room getRoom_ByNumber(Integer number) {
        List<Room> t = getRooms();
        for (Room room : t){
            if (Objects.equals(room.getNumber(), number))
                return room;
        }
        return null;
    }

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

    public Room getRoom(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Room room = session.find(Room.class, id);
            Hibernate.initialize(room.getReportSet());
            Hibernate.initialize(room.getClientSet());
            return room;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

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