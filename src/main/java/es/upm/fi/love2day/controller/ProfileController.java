package es.upm.fi.love2day.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.upm.fi.love2day.dto.ProfileDto;
import es.upm.fi.love2day.dto.UpdateProfileRequest;
import es.upm.fi.love2day.exceptions.NotFoundException;
import es.upm.fi.love2day.mapper.ProfileMapper;
import es.upm.fi.love2day.model.Profile;
import es.upm.fi.love2day.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @GetMapping("/{userId}")
    public ProfileDto getProfile(@PathVariable Long userId) {
        Profile profile = profileService
            .getProfile(userId)
            .orElseThrow(() -> new NotFoundException("Profile not found: " + userId));

        return profileMapper.toDto(profile);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProfile(@PathVariable Long userId, @RequestBody UpdateProfileRequest request) {
        profileService.updateProfile(userId, request);
    }
}
