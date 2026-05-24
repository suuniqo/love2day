package es.upm.fi.love2day.service;

import java.util.Optional;

import es.upm.fi.love2day.repository.ProfilesRepository;

@Service
public class SwipeService {
    private final SwipeRepository swipesRepository;

    public SwipeService(SwipeRepository repository) {
        this.swipesRepository = repository;
    }

    @Transactional
    public Swipe createSwipe(Long sourceId, Long targetId, SwipeType type) {
        Swipe swipe = Swipe.create(sourceId, targetId, type);
        return swipesRepository.save(swipe);
    }
ç
    public Optional<Swipe> findById(Long id) {
        return swipesRepository.findById(id);
    }

    public Optional<Swipe> findByUserId(Long id) {
        return swipesRepository.findByUserId(id);
    }

    public void deleteSwipe(Long id) {
        swipesRepository.deleteById(id);
    }
}

