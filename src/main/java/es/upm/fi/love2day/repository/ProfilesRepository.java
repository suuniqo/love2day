package es.upm.fi.love2day.repository;

import es.upm.fi.love2day.model.Profile;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilesRepository extends JpaRepository<Profile, Long> {
    @Query("""
        SELECT p FROM Profile p
        WHERE p.userId != :userId
        AND (:minAge IS NULL OR p.birthDate <= :minAge)
        AND (:maxAge IS NULL OR p.birthDate >= :maxAge)
        AND (:maxDistanceKm IS NULL OR (
            6371 * acos(
                cos(radians(:lat)) * cos(radians(p.location.latitude)) *
                cos(radians(p.location.longitude) - radians(:lon)) +
                sin(radians(:lat)) * sin(radians(p.location.latitude))
            )
        ) <= :maxDistanceKm)
        AND p.userId NOT IN (
            SELECT s.targetId FROM Swipe s WHERE s.sourceId = :userId
        )
    """)
    Page<Profile> findByPreferences(
        @Param("userId") Long userId,
        @Param("minAge") LocalDate minAge,
        @Param("maxAge") LocalDate maxAge,
        @Param("lat") Double lat,
        @Param("lon") Double lon,
        @Param("maxDistanceKm") Integer maxDistanceKm,
        Pageable pageable
    );
}

