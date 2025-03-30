package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long currentId = 1L;

    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    private void validateFilm(Film film) {
        LocalDate releaseDate = film.getReleaseDate();
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (releaseDate == null || releaseDate.isBefore(minReleaseDate)) {
            String errorMessage = "Дата релиза не может быть раньше 28 декабря 1895 года.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        if (film.getDuration() < 0) {
            String errorMessage = "Продолжительность фильма не может быть меньше нуля.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        String description = film.getDescription();
        if (description == null || description.isEmpty() || description.length() < 1 || description.length() > 200) {
            String errorMessage = "Описание должно содержать от 1 до 200 символов.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }

        String name = film.getName();
        if (name == null || name.trim().isEmpty()) {
            String errorMessage = "Имя фильма не может быть пустым.";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }

    public Collection<Film> getAllFilms() {
        return films.values();
    }

    public Film updateFilm(Film updatedFilm) {
        if (films.containsKey(updatedFilm.getId())) {
            validateFilm(updatedFilm);
            films.put(updatedFilm.getId(), updatedFilm);
            log.info("Фильм обновлен: {}", updatedFilm);
            return updatedFilm;
        }
        throw new NotFoundException("Фильм с ID " + updatedFilm.getId() + " не найден.");
    }

    public Optional<Film> getFilm(Long id) {
        return Optional.ofNullable(films.get(id));
    }
}
