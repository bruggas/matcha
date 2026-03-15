package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LocationRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Location> locationRowMapper = (rs, rowNum) -> new Location(
            rs.getLong("user_id"),
            rs.getDouble("latitude"),
            rs.getDouble("longitude"),
            rs.getString("city_name")
    );

    public void saveOrUpdate(Location location) {
        String sql = """
            INSERT INTO user_location (user_id, latitude, longitude, city_name)
            VALUES (?, ?, ?, ?)
            ON CONFLICT (user_id) DO UPDATE 
            SET latitude = EXCLUDED.latitude,
                longitude = EXCLUDED.longitude,
                city_name = EXCLUDED.city_name
            """;
        jdbcTemplate.update(sql, location.userId(), location.latitude(), location.longitude(), location.cityName());
    }

    public Optional<Location> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_location WHERE user_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, locationRowMapper, userId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_location WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}