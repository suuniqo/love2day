package es.upm.fi.love2day.dto;

import com.sun.istack.NotNull;

public record SendMessageRequest(
    @NotNull String mediaKind,
    @NotNull String content
) {}
