package com.fdaems.matcha.record;

import java.time.LocalDateTime;

public record Message(
        Long id,
        Long senderId,
        Long receiverId,
        String content,
        LocalDateTime createdAt,
        Boolean isRead
) {
}
