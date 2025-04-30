package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class GenreServiceImp implements GenreService {

    private final GenreStorage genreStorage;

    @Override
    public List<Genre> getAllGenres() {
        log.info("Получение всех жанров.");
        List<Genre> genres = new ArrayList<>(genreStorage.getAllGenres());
        return genres;
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        log.info("Получение жанра по ID: {}", id);
        return genreStorage.getGenre(id);
    }
}