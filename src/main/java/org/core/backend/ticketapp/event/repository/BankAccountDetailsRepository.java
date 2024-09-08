package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.passport.entity.BankAccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountDetailsRepository extends JpaRepository<BankAccountDetails, UUID>, PagingAndSortingRepository<BankAccountDetails, UUID> {

}
