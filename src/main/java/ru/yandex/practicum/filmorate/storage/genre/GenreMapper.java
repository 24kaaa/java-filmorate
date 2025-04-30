package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {

    public static GenreDto mapToGenreDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public static Genre mapToGenreModel(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }
}