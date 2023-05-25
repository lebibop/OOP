package oop.oop;

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


    public Worker(Integer id_worker, String name, String surname, LocalDate date_bd, String position, Integer experience) {
        this.id_worker = id_worker;
        this.name = name;
        this.surname = surname;
        this.date_bd = date_bd;
        this.position = position;
        this.experience = experience;
    }



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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    @Override
    public String toString () {
        return String.format("%d %s %s %s %s %s",
                this.id_worker, this.name, this.surname, this.date_bd, this.position, this.experience);
    }

}
