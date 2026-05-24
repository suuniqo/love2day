package es.upm.fi.love2day.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.fi.love2day.model.Verification;

@Repository
public interface VerificationsRepository extends JpaRepository<Verification, Long> {}
