package es.upm.fi.love2day.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.model.Match;
import es.upm.fi.love2day.repository.MatchesRepository;

@Service
public class MatchService {
    private final MatchesRepository matchesRepository;

    public MatchService(MatchesRepository repository) {
        this.matchesRepository = repository;
    }

    private boolean existsByUserIds(Long user1Id, Long user2Id) {
        return matchesRepository.existsByUser1IdAndUser2Id(user1Id, user2Id)
            || matchesRepository.existsByUser1IdAndUser2Id(user2Id, user1Id);
    }

    @Transactional
    public Match createMatch(Long user1Id, Long user2Id) {
        if (existsByUserIds(user1Id, user2Id)) {
            throw new IllegalStateException("Match already made between " + user1Id + " and " + user2Id);
        }

        Match match = Match.create(user1Id, user2Id);
        matchesRepository.save(match);

        return match;
    }

    @Transactional(readOnly = true)
    public Page<Match> getMatches(Long userId, Pageable pageable) {
        return matchesRepository.findByUser1IdOrUser2Id(userId, userId, pageable);
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        matchesRepository.deleteAllByUser1IdOrUser2Id(userId, userId);
    }
}
