package oop.Model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "id_room")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room;
    @Column(name = "number")
    private Integer number;
    @Column(name = "capacity")
    private Integer capacity; // ChoiceBox -----> 1/2/3/4

    @Column(name = "price")
    private Integer price;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Report> reportSet = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "room", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Client> clientSet = new ArrayList<>();

    public List<Client> getClientSet() {
        return clientSet;
    }

    public void setClientSet(List<Client> clientSet) {
        this.clientSet = clientSet;
    }

    public void addClient(Client client) {
        clientSet.add(client);
        client.setRoom(this);
    }
    public void removeClient(Client client) {
        clientSet.remove(client);
        client.setRoom(null);
    }

    public List<Report> getReportSet() {
        return reportSet;
    }

    public void setReportSet(List<Report> reportSet) {
        this.reportSet = reportSet;
    }

    public void addReport(Report report) {
        reportSet.add(report);
        report.setRoom(this);
    }
    public void removeReport(Report report) {
        reportSet.remove(report);
        report.setRoom(null);
    }

    public Room(){}

    public Integer getId_room() {
        return id_room;
    }

    public void setId_room(Integer id_room) {
        this.id_room = id_room;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }


    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    @Override
    public String toString() {
        return String.format("%d", this.number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(id_room, room.id_room) &&
                Objects.equals(number, room.number) &&
                Objects.equals(capacity, room.capacity) &&
                Objects.equals(price, room.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_room, number, capacity, price);
    }
}
