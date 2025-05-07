package org.core.backend.ticketapp.transaction.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.repository.BankAccountDetailsRepository;
import org.core.backend.ticketapp.transaction.dto.BankResponse;
import org.core.backend.ticketapp.transaction.entity.BankAccountDetails;
import org.core.backend.ticketapp.transaction.service.payment.paystack.PayStackPaymentProcessorImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class BankAccountDetailsAccountDetailsServiceImpl implements BankAccountDetailsService {
    private final BankAccountDetailsRepository bankAccountDetailsRepository;
    private final PayStackPaymentProcessorImpl payStackPaymentProcessor;

    @Override
    public BankAccountDetails getById(final UUID id) {
        final var bankAccountDetails = bankAccountDetailsRepository.findById(id).orElseThrow(
                () -> new ApplicationException(404, "not_found", "Resource not found"));
        bankAccountDetails.setDeleted(true);
        return bankAccountDetails;
    }


    @Override
    public List<BankAccountDetails> getByTenantId(final UUID id) {
        return bankAccountDetailsRepository.findByTenantId(id);
    }

    @Override
    public BankAccountDetails getByUserId(final UUID id) {
        return bankAccountDetailsRepository.findByUserId(id).orElseThrow(
                () -> new ApplicationException(404, "not_found", "Resource not found"));
    }

    @Override
    public Page<BankAccountDetails> getAll(final Pageable pageable) {
        return bankAccountDetailsRepository.getAll(pageable);
    }

    @Override
    public BankAccountDetails delete(final UUID id) {
        final var bankAccountDetails = getById(id);
        bankAccountDetails.setDeleted(true);
        bankAccountDetailsRepository.save(bankAccountDetails);
        return bankAccountDetails;
    }

    @Override
    public BankResponse getBanks() {
        return payStackPaymentProcessor.getBanks();
    }
}
