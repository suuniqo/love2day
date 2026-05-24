package es.upm.fi.love2day.dto;

public record AccountDto
(
    Long id;
    String username;
    String email;
    boolean isVerified;
    Instant createdAt;
    Instant lastLoginAt;
) {}

