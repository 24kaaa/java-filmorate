package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    Collection<User> getAllUsers();

    User updateUser(User updatedUser);

    Optional<User> getUser(Long id);

    public void removeUser(Long id);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByLogin(String login);

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> findFriends(Long id);

    List<User> findCommonFriends(Long userId, Long otherUserId);
}
