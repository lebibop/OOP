package oop.Model;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "worker")
public class Worker {
    @Id
    @Column(name = "id_worker")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_worker;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "date_bd")
    private LocalDate date_bd;
    @Column(name = "position")
    private String position;
    @Column(name = "experience")
    private Integer experience;




    public Worker() {

    }


    public Integer getId_worker() {
        return id_worker;
    }

    public void setId_worker(Integer id_worker) {
        this.id_worker = id_worker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = capitalize(name.toLowerCase());
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = capitalize(surname.toLowerCase());
    }


    public LocalDate getDate_bd() {
        return date_bd;
    }

    public void setDate_bd(LocalDate date_bd) {
        this.date_bd = date_bd;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = capitalize(position.toLowerCase());
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public String toString () {
        return String.format("%d %s %s %s %s %s",
                this.id_worker, this.name, this.surname, this.date_bd, this.position, this.experience);
    }

}
