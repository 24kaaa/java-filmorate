package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void getAllFilms() {
        Film film1 = new Film();
        film1.setName("Начало");
        film1.setDescription("Умопомрачительный триллер");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);
        filmController.addFilm(film1);

        Film film2 = new Film();
        film2.setName("Темный рыцарь");
        film2.setDescription("Супергеройский фильм");
        film2.setReleaseDate(LocalDate.of(2008, 7, 18));
        film2.setDuration(152);
        filmController.addFilm(film2);

        Collection<Film> films = filmController.getAllFilms();
        assertEquals(2, films.size());
    }

    @Test
    void addFilm() {
        Film film = new Film();
        film.setName("Начало");
        film.setDescription("Умопомрачительный триллер");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        Film result = filmController.addFilm(film);

        assertNotNull(result.getId());
        assertEquals("Начало", result.getName());
        assertEquals("Умопомрачительный триллер", result.getDescription());
        assertEquals(LocalDate.of(2010, 7, 16), result.getReleaseDate());
        assertEquals(148, result.getDuration());
    }

    @Test
    void updateFilm() {
        Film film = new Film();
        film.setName("Начало");
        film.setDescription("Умопомрачительный триллер");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        filmController.addFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setName("Начало (Обновлено)");
        updatedFilm.setDescription("Обновленное описание");
        updatedFilm.setReleaseDate(LocalDate.of(2010, 7, 16));
        updatedFilm.setDuration(150);

        Film result = filmController.updateFilm(1, updatedFilm);

        assertEquals("Начало (Обновлено)", result.getName());
        assertEquals("Обновленное описание", result.getDescription());
        assertEquals(150, result.getDuration());
    }
}