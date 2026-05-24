package es.upm.fi.love2day.dto;

import es.upm.fi.love2day.model.MessageStatus;
import java.time.Instant;

public record MessageDto(
    Long id,
    Long senderId,
    Long matchId,
    String mediaKind,
    String content,
    Instant createdAt,
    MessageStatus status
) {}
