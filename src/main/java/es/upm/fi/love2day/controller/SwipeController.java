package es.upm.fi.love2day.controller;

import es.upm.fi.love2day.dto.SwipeDto;
import es.upm.fi.love2day.mapper.SwipeMapper;
import es.upm.fi.love2day.model.Swipe;
import es.upm.fi.love2day.service.SwipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swipe")
@CrossOrigin(origins = "*")
public class SwipeController {

    private final SwipeService swipeService;
    private final SwipeMapper swipeMapper;

    public SwipeController(SwipeService swipeService, SwipeMapper swipeMapper) {
        this.swipeService = swipeService;
        this.swipeMapper = swipeMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SwipeDto createSwipe(@RequestBody CreateSwipeDto request) {
        Swipe swipe = swipeService.createSwipe(
            request.sourceId(),
            request.targetId(),
            request.type()
        );

        return swipeMapper.toDto(swipe);
    }

    @DeleteMapping("/{swipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSwipe(@PathVariable Long swipeId) {
        swipeService.deleteSwipe(swipeId);
    }

}

