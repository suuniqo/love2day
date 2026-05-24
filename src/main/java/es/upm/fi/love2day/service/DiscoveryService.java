package es.upm.fi.love2day.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.model.Location;
import es.upm.fi.love2day.model.Preferences;
import es.upm.fi.love2day.model.Profile;

@Service
public class DiscoveryService {
    private final ProfileService profileService;

    public DiscoveryService(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Transactional(readOnly = true)
    public Page<Profile> getDiscovery(Long userId, Pageable pageable) {
        Profile profile = profileService
            .getProfile(userId)
            .orElseThrow(() -> new NotFoundException("Profile not found: " + userId));

        Preferences prefs = profile.getPreferences();
        Location location = profile.getLocation();

        LocalDate minAge = prefs.getMaxAge() != null
            ? LocalDate.now().minusYears(prefs.getMaxAge())
            : null;

        LocalDate maxAge = prefs.getMinAge() != null
            ? LocalDate.now().minusYears(prefs.getMinAge())
            : null;

        Double lat = location != null ? location.getLatitude() : null;
        Double lon = location != null ? location.getLongitude() : null;

        return profileService.findByPreferences(
            userId,
            minAge,
            maxAge,
            lat,
            lon,
            prefs.getMaxDistanceKm(),
            pageable
        );
    }
}
