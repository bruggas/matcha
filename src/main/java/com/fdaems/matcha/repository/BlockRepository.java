package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Block;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BlockRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Block> blockRowMapper = (rs, rowNum) -> new Block(
            rs.getLong("id"),
            rs.getLong("blocker_id"),
            rs.getLong("blocked_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public void save(Long blockerId, Long blockedId) {
        String sql = "INSERT INTO blocks (blocker_id, blocked_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, blockerId, blockedId);
    }

    public void delete(Long blockerId, Long blockedId) {
        String sql = "DELETE FROM blocks WHERE blocker_id = ? AND blocked_id = ?";
        jdbcTemplate.update(sql, blockerId, blockedId);
    }

    public boolean exists(Long blockerId, Long blockedId) {
        String sql = "SELECT COUNT(*) FROM blocks WHERE blocker_id = ? AND blocked_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, blockerId, blockedId);
        return count != null && count > 0;
    }

    public List<Block> findBlockedUsers(Long blockerId) {
        String sql = "SELECT * FROM blocks WHERE blocker_id = ?";
        return jdbcTemplate.query(sql, blockRowMapper, blockerId);
    }
}