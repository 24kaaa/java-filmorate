package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage  implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long currentId = 1L;

    public User createUser(User user) {
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

        String login = user.getLogin();
        if (user.getName().equalsIgnoreCase(login)) {
            String errorMessage = "Имя фильма не может совпадать с логином пользователя.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
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

    @Override
    public void removeUser(Long id) {
        log.info("Удаление пользователя c ID: {}", id);

        if (id == null || !users.containsKey(id)) {
            log.warn("Пользователя с ID {} не существует", id);
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }

        for (User user : users.values()) {
            user.getFriends().remove(id);
        }
        users.remove(id);

        log.info("Пользователь с ID: {} удален", id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Поиск пользователя по email: {}", email);

        return users.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        log.info("Поиск пользователя по login: {}", login);

        return users.values()
                .stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        log.info("Добавление в друзья пользователей: {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        if (friend.getFriends() == null) {
            friend.setFriends(new HashSet<>());
        }

        if (user.getFriends().add(friendId) && friend.getFriends().add(userId)) {
            updateUser(user);
            updateUser(friend);
            log.info("Пользователи {} и {} теперь друзья", user.getId(), friend.getId());
        } else {
            log.warn("Пользователи {} и {} уже друзья", user.getId(), friend.getId());
        }

        return user;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        log.info("Удаление из друзей пользователей {}, {}", userId, friendId);

        User user = getUserOrThrow(userId);
        User friend = getUserOrThrow(friendId);

        if (user.getFriends() == null || friend.getFriends() == null) {
            log.warn("У одного или обоих пользователей нет списка друзей.");
            return user;
        }

        boolean removedUserFriend = user.getFriends().remove(friendId);
        boolean removedFriendUser = friend.getFriends().remove(userId);

        if (removedUserFriend || removedFriendUser) {
            updateUser(user);
            updateUser(friend);
            log.info("Пользователи {} и {} теперь не друзья", user.getId(), friend.getId());
        } else {
            log.warn("Пользователи {} и {} не были друзьями", user.getId(), friend.getId());
        }

        return user;
    }

    @Override
    public List<User> findCommonFriends(Long userId, Long otherUserId) {
        log.info("Получение списка общих друзей между пользователями: {} и {}", userId, otherUserId);

        User user = getUserOrThrow(userId);
        User otherUser = getUserOrThrow(otherUserId);

        Set<Long> userFriends = user.getFriends() == null ? new HashSet<>() : user.getFriends();
        Set<Long> otherUserFriends = otherUser.getFriends() == null ? new HashSet<>() : otherUser.getFriends();

        List<User> commonFriends = userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(this::getUserOrThrow)
                .collect(Collectors.toList());

        log.info("Общие друзья между {} и {}: {}", user.getId(), otherUser.getId(), commonFriends.size());
        return commonFriends;
    }

    private User getUserOrThrow(Long id) {
        log.info("Поиск пользователя с id: {}", id);

        return getUser(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с userId " + id + " не найден."));
    }

    private void nameValidation(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());

            log.info("Имя пользователя установлено по умолчанию: {}", user.getName());
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public List<User> findFriends(Long id) {
        log.info("Получение списка друзей пользователя с id: {}", id);

        User user = getUserOrThrow(id);

        log.debug("Пользователь с id {} найден: {}", user.getId(), user);

        return user.getFriends() == null ? Collections.emptyList() :
                user.getFriends().stream()
                        .map(this::getUserOrThrow)
                        .collect(Collectors.toList());
    }
}
