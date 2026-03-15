package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Interest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InterestRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Interest> interestRowMapper = (rs, rowNum) -> new Interest(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("category")
    );

    public void save(Interest interest) {
        String sql = "INSERT INTO interests (name, category) VALUES (?, ?) ON CONFLICT (name) DO NOTHING";
        jdbcTemplate.update(sql, interest.name(), interest.category());
    }

    public Optional<Interest> findByName(String name) {
        String sql = "SELECT * FROM interests WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, interestRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Interest> findAll() {
        String sql = "SELECT * FROM interests";
        return jdbcTemplate.query(sql, interestRowMapper);
    }

    public List<Interest> findByUserId(Long userId) {
        String sql = """
            SELECT i.* FROM interests i
            JOIN user_interests ui ON i.id = ui.interest_id
            WHERE ui.user_id = ?
            """;
        return jdbcTemplate.query(sql, interestRowMapper, userId);
    }

    public void addInterestToUser(Long userId, Long interestId) {
        String sql = "INSERT INTO user_interests (user_id, interest_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, userId, interestId);
    }

    public void removeInterestFromUser(Long userId, Long interestId) {
        String sql = "DELETE FROM user_interests WHERE user_id = ? AND interest_id = ?";
        jdbcTemplate.update(sql, userId, interestId);
    }
}