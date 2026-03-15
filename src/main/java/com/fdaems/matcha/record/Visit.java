package com.fdaems.matcha.record;

import java.time.LocalDateTime;

public record Visit(
        Long id,
        Long visitorId,
        Long visitedId,
        LocalDateTime createdAt
) {}