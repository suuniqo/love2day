package es.upm.fi.love2day.dto;

import java.time.Instant;

public record ChatDto(
    Long matchId,
    Instant createdAt,
    boolean isActive
) {}

