package es.upm.fi.love2day.repository;

import es.upm.fi.love2day.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    Page<Message> findByMatchId(Long matchId, Pageable pageable);
    void deleteAllByMatchIdIn(Collection<Long> matchIds);
}
