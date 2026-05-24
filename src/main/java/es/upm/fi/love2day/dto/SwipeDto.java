package es.upm.fi.love2day.dto;

public record SwipeDto
(
    Long id;
    Long sourceUserId;
    Long targetUserId;
    SwipeType swipeType;
    Instant createdAt;
) {}

