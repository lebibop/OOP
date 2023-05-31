package oop.Model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @Column(name = "id_room")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room;
    @Column(name = "number")
    private Integer number;
    @Column(name = "status")
    private String status;  // ChoiceBox ----> Free/Booked
    @Column(name = "capacity")
    private Integer capacity; // ChoiceBox -----> 1/2/3/4

    @Column(name = "price")
    private Integer price;
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Client> clientSet = new ArrayList<Client>();

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

//    @OneToOne
//    @JoinColumn(name = "report_id_report")
    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Report report;
    public Report getReport() {
        return report;
    }
    public void setReport(Report report) {
        this.report = report;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = capitalize(status.toLowerCase());
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
}
