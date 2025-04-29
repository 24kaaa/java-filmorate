package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.MpaRatingDto;
import ru.yandex.practicum.filmorate.model.MpaRating;

public class MpaMapper {

    public static MpaRatingDto mapToMpaRatingDto(MpaRating mpaRating) {
        if (mpaRating == null) {
            return null;
        }
        return new MpaRatingDto(mpaRating.getId(), mpaRating.getName());
    }

    public static MpaRating mapToMpaRatingModel(MpaRatingDto dto) {
        if (dto == null) {
            return null;
        }
        return new MpaRating(dto.getId(), dto.getName());
    }
}