package es.upm.fi.love2day.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.fi.love2day.model.Match;

@Repository
public interface MatchesRepository extends JpaRepository<Match, Long> {
    boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);
    void deleteAllByUser1IdOrUser2Id(Long user1Id, Long user2Id);
    Page<Match> findByUser1IdOrUser2Id(Long user1Id, Long user2Id, Pageable pageable);
}
