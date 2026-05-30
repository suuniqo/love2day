package es.upm.fi.love2day.dto;

import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(
    @NotNull String username,
    @NotNull String email,
    @NotNull String password
) {}
