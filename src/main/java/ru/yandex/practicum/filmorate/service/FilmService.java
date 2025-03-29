package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.*;


@AllArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film getFilmById(Long id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм с id  " + id + " не найден"));
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (!userExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        initializeLikes(film);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь с id " + userId + " уже ставил лайк фильму с id " + filmId);
        }
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLikeFilm(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (!userExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        initializeLikes(film);
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed()
                        .thenComparing(Film::getName))
                .limit(count)
                .toList();
    }

    private void initializeLikes(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
    }

    private boolean userExists(Long userId) {
        return userStorage.getUser (userId).isPresent();
    }
}
