package es.upm.fi.love2day.repository;

import es.upm.fi.love2day.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    // findBy
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);

    // exists
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
