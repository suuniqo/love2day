package es.upm.fi.love2day.dto;

import es.upm.fi.love2day.model.SwipeType;

public record CreateSwipeDto(
    Long sourceId,
    Long targetId,
    SwipeType type
) {}
