package oop.oop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class UserTest {
    User testUser =  new User("11", "Klim", "Nikolaev", "19");
    @BeforeEach
    void setUp() {
        System.out.println("Запуск теста");
    }
    @AfterEach
    void tearDown() {
        System.out.println("Завершение теста");
    }


    @Test
    void getID() {
        assertEquals("11", testUser.getID());
    }

    @Test
    void getName() {
        assertEquals("Klim", testUser.getName());
    }

    @Test
    void getSurname() {
        assertEquals("Nikolaev", testUser.getSurname());
    }

    @Test
    void getAge() {
        assertEquals("19", testUser.getAge());
    }
}