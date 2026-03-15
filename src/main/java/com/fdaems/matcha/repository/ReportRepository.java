package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Report> reportRowMapper = (rs, rowNum) -> new Report(
            rs.getLong("id"),
            rs.getLong("reporter_id"),
            rs.getLong("reported_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public void save(Long reporterId, Long reportedId) {
        String sql = "INSERT INTO reports (reporter_id, reported_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, reporterId, reportedId);
    }

    public boolean exists(Long reporterId, Long reportedId) {
        String sql = "SELECT COUNT(*) FROM reports WHERE reporter_id = ? AND reported_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reporterId, reportedId);
        return count != null && count > 0;
    }

    public List<Report> findReportsAgainstUser(Long reportedId) {
        String sql = "SELECT * FROM reports WHERE reported_id = ?";
        return jdbcTemplate.query(sql, reportRowMapper, reportedId);
    }
}