package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;

class UserControllerTest {
    private InMemoryUserStorage inMemoryUserStorage;

    @BeforeEach
    void setUp() {
        inMemoryUserStorage = new InMemoryUserStorage();
        User initialUser = new User();
        initialUser.setId(1L);
        initialUser.setEmail("ivan@example.com");
        initialUser.setLogin("ivan123");
        initialUser.setName("Иван");
        initialUser.setBirthday(LocalDate.of(1990, 5, 20));
        inMemoryUserStorage.createUser(initialUser);
    }

    @Test
    void getAllUsers() {
        Collection<User> users = inMemoryUserStorage.getAllUsers();
        assertEquals(1, users.size(), "Количество пользователей должно быть 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("maria@example.com");
        user2.setLogin("maria456");
        user2.setName("Мария");
        user2.setBirthday(LocalDate.of(1992, 8, 15));
        inMemoryUserStorage.createUser(user2);

        users = inMemoryUserStorage.getAllUsers();
        assertEquals(2, users.size(), "Количество пользователей должно быть 2");
    }

    @Test
    void addUser() {
        User newUser = new User();
        newUser.setEmail("petr@example.com");
        newUser.setLogin("petr123");
        newUser.setName("Петр");
        newUser.setBirthday(LocalDate.of(1992, 1, 1));

        User addedUser = inMemoryUserStorage.createUser(newUser);

        assertNotNull(addedUser, "Добавленный пользователь не должен быть null");
        assertEquals(2, addedUser.getId(), "ID добавленного пользователя должен быть 2");
        assertEquals("petr@example.com", addedUser.getEmail(), "Email добавленного пользователя неверен");
        assertEquals("petr123", addedUser.getLogin(), "Login добавленного пользователя неверен");
        assertEquals("Петр", addedUser.getName(), "Имя добавленного пользователя неверно");
        assertEquals(LocalDate.of(1992, 1, 1), addedUser.getBirthday(), "Дата рождения добавленного пользователя неверна");
    }

    @Test
    void updateUser() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("ivan.old@example.com");
        existingUser.setLogin("ivanOld123");
        existingUser.setName("Иван Стариков");
        existingUser.setBirthday(LocalDate.of(1991, 6, 15));

        inMemoryUserStorage.createUser(existingUser);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("ivan.new@example.com");
        updatedUser.setLogin("ivanNew123");
        updatedUser.setName("Иван Новиков");
        updatedUser.setBirthday(LocalDate.of(1991, 6, 15));

        User resultUser = inMemoryUserStorage.updateUser(updatedUser);

        assertNotNull(resultUser, "Обновленный пользователь не должен быть null");
        assertEquals(1, resultUser.getId(), "ID обновленного пользователя должен быть 1");
        assertEquals("ivan.new@example.com", resultUser.getEmail(), "Email обновленного пользователя неверен");
        assertEquals("ivanNew123", resultUser.getLogin(), "Login обновленного пользователя неверен");
        assertEquals("Иван Новиков", resultUser.getName(), "Имя обновленного пользователя неверно");
        assertEquals(LocalDate.of(1991, 6, 15), resultUser.getBirthday(), "Дата рождения обновленного пользователя неверна");
    }
}