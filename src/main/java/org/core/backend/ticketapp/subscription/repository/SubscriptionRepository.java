package org.core.backend.ticketapp.subscription.repository;

import org.core.backend.ticketapp.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
}
