package org.core.backend.ticketapp.transaction.service.impl;

import org.core.backend.ticketapp.transaction.dto.BankResponse;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface BankAccountDetailsService {
    BankAccountDetails getById(final UUID id);

    List<BankAccountDetails> getByTenantId(UUID id);

    BankAccountDetails getByUserId(final UUID id);

    Page<BankAccountDetails> getAll(final Pageable pageable);

    BankAccountDetails delete(final UUID id);

    BankResponse getBanks();
}
