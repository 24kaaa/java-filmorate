package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film createFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film updatedFilm);

    Optional<Film> getFilm(Long id);

    List<Film>findPopularFilms(int count);

    public void removeFilm(Long id);
}
