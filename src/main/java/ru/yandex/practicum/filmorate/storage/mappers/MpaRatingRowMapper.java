package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRatingRowMapper implements RowMapper<MpaRating> {

    @Override
    public MpaRating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int mpaId = resultSet.getInt("mpa_id");
        String name = resultSet.getString("name");
        return new MpaRating(mpaId, name);
    }
}