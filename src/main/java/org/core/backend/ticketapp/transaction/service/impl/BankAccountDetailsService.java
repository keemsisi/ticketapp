package org.core.backend.ticketapp.transaction.service.impl;

import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface BankAccountDetailsService {
    BankAccountDetails getById(final UUID id);

    BankAccountDetails getByUserId(final UUID id);

    Page<BankAccountDetails> getAll(final Pageable pageable);

    BankAccountDetails delete(final UUID id);
}
