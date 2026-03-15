package com.fdaems.matcha.record;

import java.time.LocalDateTime;

public record Like(
        Long id,
        Long likerId,
        Long likedId,
        LocalDateTime createdAt
) {
}
