package es.upm.fi.love2day.repository;

import es.upm.fi.love2day.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
