package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VisitRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Visit> visitRowMapper = (rs, rowNum) -> new Visit(
            rs.getLong("id"),
            rs.getLong("visitor_id"),
            rs.getLong("visited_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public void save(Long visitorId, Long visitedId) {
        String sql = "INSERT INTO visits (visitor_id, visited_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, visitorId, visitedId);
    }

    public List<Visit> findVisitsToUserId(Long visitedId) {
        String sql = "SELECT * FROM visits WHERE visited_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, visitRowMapper, visitedId);
    }

    public List<Visit> findVisitsByUserId(Long visitorId) {
        String sql = "SELECT * FROM visits WHERE visitor_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, visitRowMapper, visitorId);
    }
}