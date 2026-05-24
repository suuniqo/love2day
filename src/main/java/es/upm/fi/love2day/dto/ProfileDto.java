package es.upm.fi.love2day.dto;

public record ProfileDto
(
    Long userId,
    String name,
    Gender gender,
    Gender orientation,
    Location location,
    LocalDate dateOfBirth,
    String bio,
) {}

