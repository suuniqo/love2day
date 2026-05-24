package es.upm.fi.love2day.dto;

public record MatchDto
(
    Long id,
    Long userId1,
    Long userId2,
    LocalDateTime matchedAt,
) {}

