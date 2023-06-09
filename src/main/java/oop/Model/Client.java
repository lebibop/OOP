package oop.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id_client")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_client;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "date_bd")
    private LocalDate date_bd;   // DatePicker
    @Column(name = "date_arrival")
    private LocalDate date_arrival; // DatePicker
    @Column(name = "date_departure")
    private LocalDate date_departure; // DatePicker
    @Column(name = "stay_lenght")
    private Integer stay_lenght;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    public Client(){}

    public Integer getId_client() {
        return id_client;
    }

    public void setId_client(Integer id_client) {
        this.id_client = id_client;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = capitalize(name);
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = capitalize(surname);
    }

    public LocalDate getDate_bd() {
        return date_bd;
    }

    public void setDate_bd(LocalDate date_bd) {
        this.date_bd = date_bd;
    }

    public LocalDate getDate_arrival() {
        return date_arrival;
    }

    public void setDate_arrival(LocalDate date_arrival) {
        this.date_arrival = date_arrival;
    }

    public LocalDate getDate_departure() {
        return date_departure;
    }

    public void setDate_departure(LocalDate date_departure) {
        this.date_departure = date_departure;
    }

    public Integer getStay_lenght() {
        return stay_lenght;
    }

    public void setStay_lenght(Integer stay_lenght) {
        this.stay_lenght = stay_lenght;
    }


    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }

    public static String capitalize(String str) {
        if (str == null || str.length() == 0)
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public String toString() {
        return String.format("%d %s %s %s %s %s %d",
                this.id_client, this.name, this.surname, this.date_bd, this.date_arrival, this.date_departure, this.stay_lenght);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id_client, client.id_client) &&
                Objects.equals(name, client.name) &&
                Objects.equals(surname, client.surname) &&
                Objects.equals(date_bd, client.date_bd) &&
                Objects.equals(date_arrival, client.date_arrival) &&
                Objects.equals(date_departure, client.date_departure) &&
                Objects.equals(stay_lenght, client.stay_lenght) &&
                Objects.equals(room, client.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_client, name, surname, date_bd, date_arrival, date_departure, stay_lenght, room);
    }
}
