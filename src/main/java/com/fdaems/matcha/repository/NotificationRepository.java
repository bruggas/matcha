package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Notification;
import com.fdaems.matcha.record.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Notification> notificationRowMapper = (rs, rowNum) -> new Notification(
            rs.getLong("id"),
            rs.getLong("receiver_id"),
            rs.getLong("sender_id"),
            NotificationType.valueOf(rs.getString("type")),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getBoolean("is_read")
    );

    public void save(Notification notification) {
        String sql = "INSERT INTO notifications (receiver_id, sender_id, type) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, notification.receiverId(), notification.senderId(), notification.type().name());
    }

    public List<Notification> findByReceiverId(Long receiverId) {
        String sql = "SELECT * FROM notifications WHERE receiver_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, notificationRowMapper, receiverId);
    }

    public void markAsRead(Long notificationId, Long receiverId) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ? AND receiver_id = ?";
        jdbcTemplate.update(sql, notificationId, receiverId);
    }

    public void markAllAsRead(Long receiverId) {
        String sql = "UPDATE notifications SET is_read = true WHERE receiver_id = ? AND is_read = false";
        jdbcTemplate.update(sql, receiverId);
    }
}