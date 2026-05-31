package es.upm.fi.love2day.dto;

import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(
    @NotNull String mediaKind,
    @NotNull String content
) {}
