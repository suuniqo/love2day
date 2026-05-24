package es.upm.fi.love2day.repository;

import es.upm.fi.love2day.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Long> {
    // Spring Data JPA auto-implements these from the method name:
    // Optional<Account> findByUsername(String username);
    // Optional<Account> findByEmail(String email);
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
    // Optional<Account> findByUsernameOrEmail(String username, String email);
    //
    Page<Message> findByMatchId(Long matchId, Pageable pageable);
}
