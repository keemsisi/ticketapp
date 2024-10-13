package org.core.backend.ticketapp.marketing.repository;

import org.core.backend.ticketapp.marketing.entity.EventPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventPromotionRepository extends JpaRepository<EventPromotion, UUID> {
}
