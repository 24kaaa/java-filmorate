package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updatedFilm) {
        return inMemoryFilmStorage.updateFilm(updatedFilm);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.removeLikeFilm(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
    }
}
