package es.upm.fi.love2day.dto;

public record MessageDto
(
    Long id,
    Long senderId,
    Long matchId,
    String mediaKind,
    String content,
    LocalDateTime createdAt,
    MessageStatus status,
) {}


