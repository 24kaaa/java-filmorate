package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.AbstractDbStorage;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage extends AbstractDbStorage<MpaRating> implements MpaStorage {

    private static final String FIND_ALL_MPA_RATINGS_QUERY = "SELECT * FROM mpa";

    private static final String FIND_MPA_RATING_BY_ID_QUERY = "SELECT * FROM mpa WHERE mpa_id = ?";

    public MpaDbStorage(JdbcTemplate jdbcTemplate, RowMapper<MpaRating> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public List<MpaRating> getAllMpaRatings() {
        return findMany(FIND_ALL_MPA_RATINGS_QUERY);
    }

    @Override
    public Optional<MpaRating> getMpaRatingById(int id) {
        return findOne(FIND_MPA_RATING_BY_ID_QUERY, id);
    }
}
