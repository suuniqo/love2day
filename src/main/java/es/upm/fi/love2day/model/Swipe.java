package es.upm.fi.love2day.model;

import org.springframework.stereotype.Service;

@Service
public class Swipe {
    /* private final SwipeRepository swipesRepository;

    public SwipeService(SwipeRepository repository) {
        this.swipesRepository = repository;
    }

    @Transactional
    public Swipe createSwipe(Long sourceId, Long targetId, SwipeType type) {
        Swipe swipe = Swipe.create(sourceId, targetId, type);
        return swipesRepository.save(swipe);
    }

    public Optional<Swipe> findById(Long id) {
        return swipesRepository.findById(id);
    }

    public Optional<Swipe> findByUserId(Long id) {
        return swipesRepository.findByUserId(id);
    }

    public void deleteSwipe(Long id) {
        swipesRepository.deleteById(id);
    } */
}

