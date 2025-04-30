package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;

    @Override
    public List<MpaRating> getAllMpaRatings() {
        log.info("Получение списка всех МРА");
        return new ArrayList<>(mpaStorage.getAllMpaRatings());
    }

    @Override
    public Optional<MpaRating> getMpaRatingById(int id) {
        log.info("Получение МРА по ID {}: ", id);
        return mpaStorage.getMpaRatingById(id);
    }
}