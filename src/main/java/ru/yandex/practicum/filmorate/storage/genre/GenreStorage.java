package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Optional<Genre> getGenre(int id);

    List<Genre> findGenresByFilmId(Long id);

    Map<Long, Set<Genre>> findAllFilmGenres();
}
