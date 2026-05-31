package es.upm.fi.love2day.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.upm.fi.love2day.dto.CreateSwipeDto;
import es.upm.fi.love2day.dto.SwipeDto;
import es.upm.fi.love2day.dto.SwipeResultDto;
import es.upm.fi.love2day.mapper.SwipeMapper;
import es.upm.fi.love2day.mapper.SwipeResultMapper;
import es.upm.fi.love2day.service.SwipeService;
import es.upm.fi.love2day.service.SwipeService.SwipeResult;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/swipe")
public class SwipeController {
    private final SwipeService swipeService;
    private final SwipeMapper swipeMapper;
    private final SwipeResultMapper swipeResultMapper;

    public SwipeController(SwipeService swipeService, SwipeMapper swipeMapper, SwipeResultMapper swipeResultMapper) {
        this.swipeService = swipeService;
        this.swipeMapper = swipeMapper;
        this.swipeResultMapper = swipeResultMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SwipeResultDto createSwipe(@Valid @RequestBody CreateSwipeDto request) {
        SwipeResult result = swipeService.createSwipe(
            request.sourceId(),
            request.targetId(),
            request.type()
        );

        return swipeResultMapper.toDto(result);
    }

    @GetMapping("/history/{userId}")
    public Page<SwipeDto> getSwipeHistory(
        @PathVariable Long userId,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return swipeService.getSwipeHistory(userId, pageable).map(swipeMapper::toDto);
    }

    @DeleteMapping("/{swipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rewindSwipe(@PathVariable Long swipeId) {
        swipeService.rewindSwipe(swipeId);
    }
}
