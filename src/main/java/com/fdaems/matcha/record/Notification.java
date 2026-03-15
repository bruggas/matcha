package com.fdaems.matcha.record;

import java.time.LocalDateTime;

public record Notification(
        Long id,
        Long senderId,
        Long receiverId,
        NotificationType type,
        LocalDateTime createdAt,
        Boolean isRead
) {
}
