package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    Collection<User> getAllUsers();

    User updateUser(User updatedUser);

    Optional<User> getUser(Long id);
}
