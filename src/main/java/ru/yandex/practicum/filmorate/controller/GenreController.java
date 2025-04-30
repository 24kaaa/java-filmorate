package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        log.info("Запрос на получение всех жанров.");

        List<GenreDto> genres = genreService.getAllGenres()
                .stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable int id) {
        log.info("Запрос на получение жанра по ID: {}", id);

        Genre genre = genreService.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден."));
        log.info("Жанр с ID: {} найден", id);
        return new ResponseEntity<>(GenreMapper.mapToGenreDto(genre), HttpStatus.OK);
    }
}