package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.MpaRatingDto;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.mpa.MpaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public ResponseEntity<List<MpaRatingDto>> getAllMpaRatings() {
        log.info("Запрос на получение всех рейтингов МРА.");

        List<MpaRatingDto> mpaRatings = mpaService.getAllMpaRatings()
                .stream()
                .map(MpaMapper::mapToMpaRatingDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(mpaRatings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MpaRatingDto> getMpaRatingById(@PathVariable Integer id) {
        log.info("Запрос на получение рейтинга МРА по ID: {}", id);

        MpaRating mpaRating = mpaService.getMpaRatingById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг МРА с id " + id + " не найден."));
        log.info("Рейтинг МРА с ID: {} найден.", id);
        return new ResponseEntity<>(MpaMapper.mapToMpaRatingDto(mpaRating), HttpStatus.OK);
    }
}