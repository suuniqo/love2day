package es.upm.fi.love2day.dto;

import com.sun.istack.NotNull;

public record CreateAccountRequest(
    @NotNull String username,
    @NotNull String email,
    @NotNull String password
) {}
