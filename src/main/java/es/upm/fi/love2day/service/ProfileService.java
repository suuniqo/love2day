package es.upm.fi.love2day.service;

import java.util.Optional;

import es.upm.fi.love2day.repository.ProfilesRepository;

@Service
public class ProfileService {
    private final ProfilesRepository profilesRepository;

    public ProfileService(ProfilesRepository repository) {
        this.profilesRepository = repository;
    }

    public Profile createProfile(Long userId, String name, String bio) {
        Profile profile = Profile.create(userId, name, bio);

        return profilesRepository.save(profile);
    }

    public Optional<Profile> findById(Long id) {
        return profilesRepository.findById(id);
    }

    //TODO: Tiene que enviar una lista de perfiles?
    //Además, cómo hacemos el query
    public List<Profile> findByPreferences(Long sourceId) {
        return profilesRepository.findPreferences(sourceId);
    }

    //TODO:Deberíamos poner setters? O un método updateProfile?

    public void deleteSwipe(Long id) {
        swipesRepository.deleteById(id);
    }
}

