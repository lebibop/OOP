package oop.Model;

import javax.persistence.*;

@Entity
@Table(name = "report")
public class Report {
    @Id
    @Column(name = "id_report")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_report;
    @Column(name = "month")
    private Integer month;
    @Column(name = "clients_per_month")
    private Integer clients_per_month;
    @Column(name = "free_per_month")
    private Integer free_per_month;
    @Column(name = "booked_per_month")
    private Integer booked_per_month;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private Room room = new Room();

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return String.format("%d %d %d %d",
                this.month, this.clients_per_month, this.free_per_month, this.booked_per_month);
    }
}
