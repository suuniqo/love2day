package es.upm.fi.love2day.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.fi.love2day.dto.MatchDto;
import es.upm.fi.love2day.mapper.MatchMapper;
import es.upm.fi.love2day.service.MatchService;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService;
    private final MatchMapper matchMapper;

    public MatchController(MatchService matchService, MatchMapper matchMapper) {
        this.matchService = matchService;
        this.matchMapper = matchMapper;
    }

    @GetMapping("/{userId}")
    public Page<MatchDto> getMatches(
        @PathVariable Long userId,
        @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return matchService.getMatches(userId, pageable).map(matchMapper::toDto);
    }
}

