package es.upm.fi.love2day.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.fi.love2day.model.Swipe;

@Repository
public interface SwipesRepository extends JpaRepository<Swipe, Long> {
    boolean existsBySourceIdAndTargetId(Long sourceId, Long targetId);
    Optional<Swipe> findBySourceIdAndTargetId(Long sourceId, Long targetId);
    Page<Swipe> findBySourceId(Long sourceId, Pageable pageable);
    void deleteAllBySourceIdOrTargetId(Long sourceId, Long targetId);
}
