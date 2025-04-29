package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage extends AbstractDbStorage<Film> implements FilmStorage {

    private static final String FIND_ALL_FILM_QUERY = "SELECT f.*, m.name AS mpa_name FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id";

    private static final String FIND_FILM_BY_ID_QUERY = "SELECT f.*, m.name AS mpa_name FROM films f JOIN mpa m ON f.mpa_id = m.mpa_id WHERE f.film_id = ?";

    private static final String INSERT_FILM_QUERY = "INSERT INTO films " +
            "(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ? )";

    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?," +
            " duration = ?, mpa_id = ? WHERE film_id = ?";

    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE film_id = ?";

    private static final String FIND_POPULAR_FILMS_QUERY = "SELECT f.*, m.name AS mpa_name, " +
            "COUNT(l.user_id) AS like_count " +
            "FROM films f " +
            "JOIN mpa m ON f.mpa_id = m.mpa_id " +
            "LEFT JOIN likes l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id, m.name, f.name, f.description, f.release_date, f.duration " +
            "ORDER BY like_count DESC, f.film_id ASC " +
            "LIMIT ?";

    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

    private static final String FIND_GENRES_BY_FILM_ID_QUERY = "SELECT g.genre_id, g.name " +
            "FROM genres g " +
            "JOIN film_genres fg ON g.genre_id = fg.genre_id " +
            "WHERE fg.film_id = ?";

    private final RowMapper<Genre> genreRowMapper;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> filmRowMapper, RowMapper<Genre> genreRowMapper) {
        super(jdbcTemplate, filmRowMapper);
        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public List<Film> getAllFilms() {
        return findMany(FIND_ALL_FILM_QUERY);
    }

    @Override
    public Film createFilm(Film film) {
        filmValidation(film);
        long filmId = insert(
                INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getId()
        );
        film.setId(filmId);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                update(INSERT_FILM_GENRES_QUERY, filmId, genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Film existingFilm = getFilm(film.getId()).orElse(null);
        if (!film.getId().equals(existingFilm.getId())) {
            throw new ValidationException("ID фильма в запросе не соответствует существующему фильму.");
        }
        update(
                UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        Optional<Film> optionalFilm = findOne(FIND_FILM_BY_ID_QUERY, id);
        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            List<Genre> genres = jdbcTemplate.query(FIND_GENRES_BY_FILM_ID_QUERY, genreRowMapper, id);
            film.setGenres(new HashSet<>(genres));
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public List<Film> findPopularFilms(int count) {
        return findMany(FIND_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public void removeFilm(Long id) {
        update(DELETE_FILM_QUERY, id);
    }

    private void filmValidation(Film film) {
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
}
