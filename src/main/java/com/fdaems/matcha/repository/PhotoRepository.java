package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Photo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PhotoRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Photo> photoRowMapper = (rs, rowNum) -> new Photo(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("file_path"),
            rs.getBoolean("is_profile_picture")
    );

    public void save(Photo photo) {
        String sql = "INSERT INTO user_photos (user_id, file_path, is_profile_picture) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, photo.userId(), photo.filePath(), photo.isProfilePicture());
    }

    public List<Photo> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_photos WHERE user_id = ?";
        return jdbcTemplate.query(sql, photoRowMapper, userId);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM user_photos WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void unsetProfilePictures(Long userId) {
        String sql = "UPDATE user_photos SET is_profile_picture = false WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public void setProfilePicture(Long photoId, Long userId) {
        String sql = "UPDATE user_photos SET is_profile_picture = true WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, photoId, userId);
    }
}