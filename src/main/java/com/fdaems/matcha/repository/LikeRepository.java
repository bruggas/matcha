package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Like> likeRowMapper = (rs, rowNum) -> new Like(
            rs.getLong("id"),
            rs.getLong("liker_id"),
            rs.getLong("liked_id"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public void save(Long likerId, Long likedId) {
        String sql = "INSERT INTO likes (liker_id, liked_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        jdbcTemplate.update(sql, likerId, likedId);
    }

    public void delete(Long likerId, Long likedId) {
        String sql = "DELETE FROM likes WHERE liker_id = ? AND liked_id = ?";
        jdbcTemplate.update(sql, likerId, likedId);
    }

    public boolean exists(Long likerId, Long likedId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE liker_id = ? AND liked_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, likerId, likedId);
        return count != null && count > 0;
    }

    public List<Like> findLikesByUserId(Long likedId) {
        String sql = "SELECT * FROM likes WHERE liked_id = ?";
        return jdbcTemplate.query(sql, likeRowMapper, likedId);
    }

    public List<Like> findLikesMadeByUserId(Long likerId) {
        String sql = "SELECT * FROM likes WHERE liker_id = ?";
        return jdbcTemplate.query(sql, likeRowMapper, likerId);
    }
}