package com.fdaems.matcha.record;

import java.time.LocalDateTime;

public record Block(
        Long id,
        Long blockerId,
        Long blockedId,
        LocalDateTime createdAt
) {
}
