package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import java.util.stream.Collectors;

@Repository
public class FilmMapper {

    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();
        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        if (film.getMpaRating() != null) {
            MpaRatingDto mpa = new MpaRatingDto(film.getMpaRating().getId(), film.getMpaRating().getName());
            dto.setMpa(mpa);
        }
        dto.setGenres(film.getGenres()
                .stream()
                .map(genre -> new GenreDto(genre.getId(),genre.getName()))
                .collect(Collectors.toSet()));
        dto.setLikes(film.getLikes());
        return dto;
    }

    public static Film mapToFilmModel(FilmDto dto) {
        Film film = new Film();

        film.setId(dto.getId());
        film.setName(dto.getName());
        film.setDescription(dto.getDescription());
        film.setReleaseDate(dto.getReleaseDate());
        film.setDuration(dto.getDuration());

        if (dto.getMpa() != null) {
            MpaRating mpaRating = new MpaRating(dto.getMpa().getId(), dto.getMpa().getName());
            mpaRating.setId(dto.getMpa().getId());
            film.setMpaRating(mpaRating);
        }

        if (dto.getGenres() != null) {
            film.setGenres(dto.getGenres()
                    .stream()
                    .map(genreDto -> new Genre(genreDto.getId(), genreDto.getName()))
                    .collect(Collectors.toSet()));
        }

        film.setLikes(dto.getLikes());
        return film;
    }
}
