package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        if (films.containsKey(updatedFilm.getId())) {
            validateFilm(updatedFilm);
            films.put(updatedFilm.getId(), updatedFilm);
            log.info("Фильм обновлен: {}", updatedFilm);
            return updatedFilm;
        }
        throw new ValidationException("Фильм с ID " + updatedFilm.getId() + " не найден.");
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            String errorMessage = "Название фильма не может быть пустым.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            String errorMessage = "Максимальная длина описания — 200 символов.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        LocalDate releaseDate = film.getReleaseDate();
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (releaseDate == null || releaseDate.isBefore(minReleaseDate)) {
            String errorMessage = "Дата релиза не может быть раньше 28 декабря 1895 года.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (film.getDuration() <= 0) {
            String errorMessage = "Продолжительность фильма должна быть положительным числом.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
