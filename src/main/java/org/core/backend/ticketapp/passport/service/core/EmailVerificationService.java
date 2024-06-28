package org.core.backend.ticketapp.passport.service.core;


import org.core.backend.ticketapp.passport.entity.EmailVerification;
import org.core.backend.ticketapp.passport.repository.EmailVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;


@Service
public class EmailVerificationService {

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    public boolean hasTokenExpired(String token) {
        Optional<EmailVerification> emailVerification = getByToken(token);
        if (!emailVerification.isPresent()) {
            return false;
        }
        Date expiryDate = new Date(emailVerification.get().getExpiryDate() * 1000);

        if (expiryDate.before(new Date())) {
            changeVerificationStatus(emailVerification.get(), false);
            return true;
        }
        return  false;
    }

    public void changeVerificationStatus(EmailVerification emailVerification, boolean used) {
        emailVerification.setTokenUsed(used);
        emailVerificationRepository.save(emailVerification);
    }

    public int invalidatePendingEmailVerifications(UUID id, boolean used) {
        return emailVerificationRepository.invalidateEmailVerifications(id, used ? 1 : 0);
    }

    public Optional<EmailVerification> getByToken(String token) {
        return emailVerificationRepository.findByToken(token);
    }
}
