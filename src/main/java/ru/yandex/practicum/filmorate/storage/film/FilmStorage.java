package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Collection<Film> getAllFilms();

    Film updateFilm(Film updatedFilm);

    Optional<Film> getFilm(Long id);
}
