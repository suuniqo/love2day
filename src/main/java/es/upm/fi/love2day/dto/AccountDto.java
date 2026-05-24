package es.upm.fi.love2day.dto;

import java.time.Instant;

public record AccountDto(
    Long id,
    String username,
    String email,
    boolean verified,
    Instant createdAt
) {}
