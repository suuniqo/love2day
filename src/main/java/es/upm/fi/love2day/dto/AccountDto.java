package es.upm.fi.love2day.dto;

//Los records ya definen automáticamente los getters, setters y el constructor.

public record AccountDto
(
    Long id;
    String username;
    String email;
    boolean isVerified;
    Instant createdAt;
    Instant lastLoginAt;
) {}

