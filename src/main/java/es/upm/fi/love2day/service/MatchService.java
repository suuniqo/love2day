package es.upm.fi.love2day.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.fi.love2day.model.Match;
import es.upm.fi.love2day.repository.MatchesRepository;

@Service
public class MatchService {
    private final MatchesRepository matchesRepository;
    private final ChatService chatService;

    public MatchService(
        MatchesRepository repository,
        ChatService chatService
    ) {
        this.matchesRepository = repository;
        this.chatService = chatService;
    }

    @Transactional(readOnly = true)
    private boolean existsByUserIds(Long user1Id, Long user2Id) {
        return matchesRepository.existsByUser1IdAndUser2Id(user1Id, user2Id)
            || matchesRepository.existsByUser1IdAndUser2Id(user2Id, user1Id);
    }

    @Transactional(readOnly = true)
    public Optional<Long> getOpposite(Long userId, Match match) {
        if (match.getUser1Id() == userId) {
            return Optional.of(match.getUser2Id());
        }
        if (match.getUser2Id() == userId) {
            return Optional.of(match.getUser1Id());
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Long> findOpposite(Long userId, Long matchId) {
        return matchesRepository
            .findById(matchId)
            .flatMap(match -> getOpposite(userId, match));
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
        List<Long> matchIds = matchesRepository.findMatchIdsByUserId(userId);
        chatService.deleteByMatchIds(matchIds);
        matchesRepository.deleteAllByIdInBatch(matchIds);
    }
}
