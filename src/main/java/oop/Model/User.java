package oop.Model;

public class User {
    private String ID;
    private String Name;
    private String Surname;
    private String Age;

    public User(String id, String name, String surname, String age) {
        ID = id;
        Name = name;
        Surname = surname;
        Age = age;
    }

    public String getID() {
        return this.ID;
    }

    public String getName() {
        return this.Name;
    }

    public String getSurname() {
        return this.Surname;
    }

    public String getAge() { return this.Age;}

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setSurname(String surname) {
        this.Surname = surname;
    }

    public void setAge(String age) {
        this.Age = age;
    }
}