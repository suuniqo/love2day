package es.upm.fi.love2day.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.exceptions.BadRequestException;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.model.Match;
import es.upm.fi.love2day.model.Swipe;
import es.upm.fi.love2day.model.SwipeType;
import es.upm.fi.love2day.repository.SwipesRepository;

@Service
public class SwipeService {
    // Match es null si no hay match
    public record SwipeResult(Swipe swipe, Match match) {}

    private final SwipesRepository swipesRepository;
    private final MatchService matchService;

    public SwipeService(SwipesRepository repository, MatchService matchService) {
        this.swipesRepository = repository;
        this.matchService = matchService;
    }

    @Transactional
    private Match tryCreateMatch(Long sourceId, Long targetId, SwipeType type) {
        if (!type.isLike()) {
            return null;
        }

        boolean mutualLike = swipesRepository
            .findBySourceIdAndTargetId(targetId, sourceId)
            .map(Swipe::getType)
            .map(SwipeType::isLike)
            .orElse(false);

        return mutualLike
            ? matchService.createMatch(sourceId, targetId)
            : null;
    }

    @Transactional
    public SwipeResult createSwipe(Long sourceId, Long targetId, SwipeType type) {
        if (swipesRepository.existsBySourceIdAndTargetId(sourceId, targetId)) {
            throw new BadRequestException("Swipe already made from " + sourceId + " to " + targetId);
        }

        Swipe swipe = Swipe.create(sourceId, targetId, type);
        swipesRepository.save(swipe);

        Match match = tryCreateMatch(sourceId, targetId, type);

        return new SwipeResult(swipe, match);
    }

    @Transactional(readOnly = true)
    public Page<Swipe> getSwipeHistory(Long userId, Pageable pageable) {
        return swipesRepository.findBySourceId(userId, pageable);
    }

    @Transactional
    public void rewindSwipe(Long id) {
        boolean isLike = swipesRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Swipe not found: " + id))
            .getType()
            .isLike();

        if (!isLike) {
            throw new BadRequestException("Cannot rewind a swipe which isn't a like");
        }

        swipesRepository.deleteById(id);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        swipesRepository.deleteAllBySourceIdOrTargetId(userId, userId);
        matchService.deleteByUserId(userId);
    }
}
