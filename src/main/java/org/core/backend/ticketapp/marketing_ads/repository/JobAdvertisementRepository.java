package org.core.backend.ticketapp.marketing_ads.repository;

import org.core.backend.ticketapp.marketing_ads.entity.JobAdvertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobAdvertisementRepository extends JpaRepository<JobAdvertisement, UUID> {
}
