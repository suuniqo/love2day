package es.upm.fi.love2day.dto;

import java.time.Instant;

public record MatchDto(
    Long id,
    Long user1Id,
    Long user2Id,
    Instant createdAt
) {}
