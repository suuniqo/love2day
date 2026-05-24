package es.upm.fi.love2day.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.dto.UpdateProfileRequest;
import es.upm.fi.love2day.exceptions.BadRequestException;
import es.upm.fi.love2day.model.Profile;
import es.upm.fi.love2day.repository.ProfilesRepository;

@Service
public class ProfileService {
    private final ProfilesRepository profilesRepository;

    public ProfileService(ProfilesRepository repository) {
        this.profilesRepository = repository;
    }

    @Transactional
    public Profile createProfile(Long userId) {
        if (profilesRepository.existsById(userId)) {
            throw new IllegalStateException("Profile already exists: " + userId);
        }

        Profile profile = Profile.create(userId);

        return profilesRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public Optional<Profile> getProfile(Long userId) {
        return profilesRepository.findById(userId);
    }

    @Transactional(readOnly = true)
    public Page<Profile> findByPreferences(
        Long userId,
        LocalDate minAge,
        LocalDate maxAge,
        Double lat,
        Double lon,
        Integer maxDistanceKm,
        Pageable pageable
    ) {
        return profilesRepository.findByPreferences(userId, minAge, maxAge, lat, lon, maxDistanceKm, pageable);
    }

    @Transactional
    private void applyUpdate(Profile profile, UpdateProfileRequest request) {
        if (request.displayName()   != null) profile.setDisplayName(request.displayName());
        if (request.gender()        != null) profile.setGender(request.gender());
        if (request.orientation()   != null) profile.setOrientation(request.orientation());
        if (request.location()      != null) profile.setLocation(request.location());
        if (request.birthDate()     != null) profile.setBirthDate(request.birthDate());
        if (request.bio()           != null) profile.setBio(request.bio());
        if (request.minAge()        != null) profile.setMinAge(request.minAge());
        if (request.maxAge()        != null) profile.setMaxAge(request.maxAge());
        if (request.maxDistanceKm() != null) profile.setMaxDistanceKm(request.maxDistanceKm());
    }

    @Transactional
    public void updateProfile(Long userId, UpdateProfileRequest request) {
        Profile profile = profilesRepository
            .findById(userId)
            .orElseThrow(() -> new BadRequestException("Profile doesn't exist: " + userId));

        applyUpdate(profile, request);

        profilesRepository.save(profile);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        profilesRepository.deleteById(userId);
    }
}
