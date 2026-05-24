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
    public Swipe createSwipe(String username, String email, String passwordRaw) {
        if (swipesRepository.existsByUsername(username)) {
            throw new ConflictException("Username already taken");
        }
        if (swipesRepository.existsByEmail(email)) {
            throw new ConflictException("Email already registered");
        }

        Swipe swipe = Swipe.create(
            username,
            email,
            passwordEncoder.encode(passwordRaw)
        );

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
    }
}

