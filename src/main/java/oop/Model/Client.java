package oop.Model;

import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.time.LocalDate;
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

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }


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
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    @Override
    public String toString() {
        return String.format("%d %s %s %s %s %s %d",
                this.id_client, this.name, this.surname, this.date_bd, this.date_arrival, this.date_departure, this.stay_lenght);
    }
}
