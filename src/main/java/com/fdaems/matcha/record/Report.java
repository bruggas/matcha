package com.fdaems.matcha.record;

import java.time.LocalDateTime;

public record Report(
        Long id,
        Long reporterId,
        Long reportedId,
        LocalDateTime createdAt
) {
}
