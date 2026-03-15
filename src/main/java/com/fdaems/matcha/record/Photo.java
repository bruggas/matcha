package com.fdaems.matcha.record;

public record Photo(
        Long id,
        Long userId,
        String filePath,
        Boolean isProfilePicture
) {
}
