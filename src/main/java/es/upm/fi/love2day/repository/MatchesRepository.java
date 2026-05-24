package es.upm.fi.love2day.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.upm.fi.love2day.model.Match;

@Repository
public interface MatchesRepository extends JpaRepository<Match, Long> {
    boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);
    Page<Match> findByUser1IdOrUser2Id(Long user1Id, Long user2Id, Pageable pageable);
    @Query("SELECT m.id FROM Match m WHERE m.user1Id = :userId OR m.user2Id = :userId")
    List<Long> findMatchIdsByUserId(@Param("userId") Long userId);
}
