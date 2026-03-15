package com.fdaems.matcha.record;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record User(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        String passwordHash,
        GenderType gender,
        LocalDate birthday,
        String phoneNumber,
        String bio,
        SexualPreferenceType sexualPreference,
        Integer rating,
        LocalDateTime lastConnection,
        Boolean isVerified,
        String verificationToken,
        String resetToken
) {}