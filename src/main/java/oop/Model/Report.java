package oop.Model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @Column(name = "id_report")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_report;
    @Column(name = "clients_per_month")
    private Integer clients_per_month;
    @Column(name = "free_per_month")
    private Integer free_per_month;
    @Column(name = "booked_per_month")
    private Integer booked_per_month;

//    @OneToOne(mappedBy = "report")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room = new Room();

    public Room getRoom() {
        return room;
    }
    public void setRoom(Room room) {
        this.room = room;
    }


    public Report(){}

    public Integer getId_report() {
        return id_report;
    }

    public void setId_report(Integer id_report) {
        this.id_report = id_report;
    }

    public Integer getClients_per_month() {
        return clients_per_month;
    }

    public void setClients_per_month(Integer clients_per_month) {
        this.clients_per_month = clients_per_month;
    }

    public Integer getFree_per_month() {
        return free_per_month;
    }

    public void setFree_per_month(Integer free_per_month) {
        this.free_per_month = free_per_month;
    }

    public Integer getBooked_per_month() {
        return booked_per_month;
    }

    public void setBooked_per_month(Integer booked_per_month) {
        this.booked_per_month = booked_per_month;
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %d",
                this.id_report, this.clients_per_month, this.free_per_month, this.booked_per_month);
    }
}
