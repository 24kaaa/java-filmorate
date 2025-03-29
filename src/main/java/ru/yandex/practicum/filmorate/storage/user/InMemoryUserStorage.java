package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage  implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long currentId = 1L;

    public User addUser(User user) {
        validateUser(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        log.info("Пользователь добавлен: {}", user);
        return user;
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public User updateUser(User updatedUser) {
        long id = updatedUser.getId();
        if (users.containsKey(id)) {
            validateUser(updatedUser);
            updatedUser.setId(id);
            users.put(id, updatedUser);
            log.info("Пользователь обновлен: {}", updatedUser);
            return updatedUser;
        }
        throw new NotFoundException("Пользователь с ID " + id + " не найден.");
    }

    public Optional<User> getUser(Long id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
}
