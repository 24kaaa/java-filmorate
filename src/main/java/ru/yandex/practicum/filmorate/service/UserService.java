package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User getUserById(Long id) {
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundException("Юзер с id  " + id + " не найден"));
    }

    public void addFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        initializeFriendsList(user);
        initializeFriendsList(friend);

        if (user.getFriends().add(friendId)) {
            friend.getFriends().add(userId);
        }
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    private void initializeFriendsList(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
    }

    public List<User> getCommonFriends(Long userId1, Long userId2) {
        User user1 = getUserById(userId1);
        User user2 = getUserById(userId2);

        Set<Long> commonFriendIds = user1.getFriends().stream()
                .filter(user2.getFriends()::contains)
                .collect(Collectors.toSet());

        return commonFriendIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        initializeFriendsList(user);
        initializeFriendsList(friend);

        if (user.getFriends().remove(friendId)) {
            friend.getFriends().remove(userId);
        }
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
    }

    public Collection<User> getUserFriends(Long id) {
        return getUserById(id).getFriends().stream()
                .map(this::getUserById).toList();
    }
}
