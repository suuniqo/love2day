package es.upm.fi.love2day.dto;

import es.upm.fi.love2day.model.SwipeType;
import jakarta.validation.constraints.NotNull;

public record CreateSwipeDto(
    @NotNull Long sourceId,
    @NotNull Long targetId,
    @NotNull SwipeType type
) {}
