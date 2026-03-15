package com.fdaems.matcha.record;

public record Location(
        Long userId,
        Double latitude,
        Double longitude,
        String cityName
) {
}
