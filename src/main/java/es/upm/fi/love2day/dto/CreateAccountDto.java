package es.upm.fi.love2day.dto;

public record CreateAccountDto(
    String username,
    String email,
    String password
) {}
