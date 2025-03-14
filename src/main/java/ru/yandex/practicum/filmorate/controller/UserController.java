package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        validateUser(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
        if (users.containsKey(id)) {
            validateUser(updatedUser);
            updatedUser.setId(id);
            users.put(id, updatedUser);
            log.info("Пользователь обновлен: {}", updatedUser);
            return updatedUser;
        }
        throw new ValidationException("Пользователь с ID " + id + " не найден.");
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !user.getEmail().contains("@")) {
            String errorMessage = "Электронная почта не может быть пустой и должна содержать символ '@'.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (user.getLogin() == null || user.getLogin().trim().isEmpty() || user.getLogin().contains(" ")) {
            String errorMessage = "Логин не может быть пустым и не должен содержать пробелы.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            String errorMessage = "Дата рождения не может быть в будущем.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
