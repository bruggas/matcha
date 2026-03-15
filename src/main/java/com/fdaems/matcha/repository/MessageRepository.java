package com.fdaems.matcha.repository;

import com.fdaems.matcha.record.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Message> messageRowMapper = (rs, rowNum) -> new Message(
            rs.getLong("id"),
            rs.getLong("sender_id"),
            rs.getLong("receiver_id"),
            rs.getString("content"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getBoolean("is_read")
    );

    public void save(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, message.senderId(), message.receiverId(), message.content());
    }

    public List<Message> findConversation(Long user1Id, Long user2Id) {
        String sql = """
            SELECT * FROM messages
            WHERE (sender_id = ? AND receiver_id = ?)
               OR (sender_id = ? AND receiver_id = ?)
            ORDER BY created_at ASC
            """;
        return jdbcTemplate.query(sql, messageRowMapper, user1Id, user2Id, user2Id, user1Id);
    }

    public void markAsRead(Long receiverId, Long senderId) {
        String sql = "UPDATE messages SET is_read = true WHERE receiver_id = ? AND sender_id = ? AND is_read = false";
        jdbcTemplate.update(sql, receiverId, senderId);
    }
}