package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
@Primary
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long currentId = 1L;

@Override
    public Film createFilm(Film film) {
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
        if (description == null || description.trim().isEmpty() || description.length() < 1 || description.length() > 200) {
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
    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        Film existingFilm = getFilm(film.getId()).orElse(null);
        if (!film.getId().equals(existingFilm.getId())) {
            throw new ValidationException("ID фильма в запросе не соответствует существующему фильму.");
        }
        log.info("Обновление фильма: {}", film);
        if (film.getId() == null || !films.containsKey(film.getId())) {
            log.warn("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Фильм {} обновлен", film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        log.info("Получение топ {} популярных фильмов по количеству лайков.", count);
        return films.values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void removeFilm(Long id) {
        log.info("Удаление фильма с ID {}", id);
        if (!films.containsKey(id)) {
            log.warn("Фильм с ID {} не найден", id);
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }
        films.remove(id);
        log.info("Фильм с ID {} удален", id);
    }
}
