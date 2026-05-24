package es.upm.fi.love2day.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.fi.love2day.model.Chat;
import es.upm.fi.love2day.model.Message;

import java.util.Optional;

@Repository
public interface ChatsRepository extends JpaRepository<Chat, Long> {
    // Spring Data JPA auto-implements these from the method name:
    // Optional<Account> findByUsername(String username);
    // Optional<Account> findByEmail(String email);
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
    // Optional<Account> findByUsernameOrEmail(String username, String email);

    void save(Long matchId, Message message);   
    Optional<Chat> findByMatchId(Long matchId);
}

