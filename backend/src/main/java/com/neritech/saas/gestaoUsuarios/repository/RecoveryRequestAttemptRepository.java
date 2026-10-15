package com.neritech.saas.gestaoUsuarios.repository;

import com.neritech.saas.gestaoUsuarios.domain.RecoveryRequestAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface RecoveryRequestAttemptRepository extends JpaRepository<RecoveryRequestAttempt, Long> {
    long countByEmailHashAndCreatedAtAfter(String emailHash, LocalDateTime threshold);
    long countByIpHashAndCreatedAtAfter(String ipHash, LocalDateTime threshold);
}
