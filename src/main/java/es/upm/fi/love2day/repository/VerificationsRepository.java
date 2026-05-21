package es.upm.fi.love2day.repository;

import es.upm.fi.love2day.model.Verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationsRepository extends JpaRepository<Verification, Long> {
    // Spring Data JPA auto-implements these from the method name:
    // Optional<Account> findByUsername(String username);
    // Optional<Account> findByEmail(String email);
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
    // Optional<Account> findByUsernameOrEmail(String username, String email);
}
