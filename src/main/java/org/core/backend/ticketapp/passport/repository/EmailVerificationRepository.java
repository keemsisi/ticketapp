package org.core.backend.ticketapp.passport.repository;

import org.core.backend.ticketapp.passport.entity.EmailVerification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Repository
public interface EmailVerificationRepository extends CrudRepository<EmailVerification, Long> {

    @Query(value = "SELECT * FROM email_verification WHERE user_id = ?1 AND email_address = ?2 AND token_used = 0", nativeQuery = true)
    List<EmailVerification> findByMember(UUID userId, String email, int operation, int status, Pageable pageable);

    @Query(value = "SELECT * FROM email_verification WHERE token = ?1",
            nativeQuery = true)
    Optional<EmailVerification> findByToken(String token);

    @Modifying
    @Query(value = "UPDATE email_verification SET token_used = 0 WHERE user_id = ?1 AND operation = ?2",
            nativeQuery = true)
    int invalidateEmailVerifications(UUID userId, int operation);

}
