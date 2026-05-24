package es.upm.fi.love2day.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.fi.love2day.dto.ProfileDto;
import es.upm.fi.love2day.mapper.ProfileMapper;
import es.upm.fi.love2day.service.DiscoveryService;

@RestController
@RequestMapping("/discovery")
public class DiscoveryController {
    private final DiscoveryService discoveryService;
    private final ProfileMapper profileMapper;

    public DiscoveryController(DiscoveryService discoveryService, ProfileMapper profileMapper) {
        this.discoveryService = discoveryService;
        this.profileMapper = profileMapper;
    }

    @GetMapping("/{userId}")
    public Page<ProfileDto> getDiscoveryProfiles(
        @PathVariable Long userId,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return discoveryService
            .getDiscovery(userId, pageable)
            .map(profileMapper::toDto);
    }
}
