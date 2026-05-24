package es.upm.fi.love2day.dto;

public record CreateAccountRequest(
    String username,
    String email,
    String password
) {}
