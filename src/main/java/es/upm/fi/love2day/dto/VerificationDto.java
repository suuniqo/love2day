package es.upm.fi.love2day.dto;

public record VerificationDto
(
    Long userId,
    VerificationStatus status,
) {}

