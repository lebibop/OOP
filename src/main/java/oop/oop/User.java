package oop.oop;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty Name;
    private final SimpleStringProperty Surname;
    private final SimpleIntegerProperty Age;

    public User(Integer id, String name, String surname, Integer age) {
        ID = new SimpleIntegerProperty(id);
        Name = new SimpleStringProperty(name);
        Surname = new SimpleStringProperty(surname);
        Age = new SimpleIntegerProperty(age);
    }

    public Integer getID() {
        return ID.get();
    }

    public String getName() {
        return Name.get();
    }

    public String getSurname() {
        return Surname.get();
    }

    public Integer getAge() { return Age.get();}

    public void setID(Integer ID) {
        this.ID.set(ID);
    }

    public void setName(String name) {
        this.Name.set(name);
    }

    public void setSurname(String surname) {
        this.Surname.set(surname);
    }

    public void setAge(Integer age) {
        this.Age.set(age);
    }
}