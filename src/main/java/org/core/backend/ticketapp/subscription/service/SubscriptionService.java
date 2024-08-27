package org.core.backend.ticketapp.subscription.service;

import org.core.backend.ticketapp.subscription.dto.SubscriptionCreateRequestDTO;
import org.core.backend.ticketapp.subscription.entity.Subscription;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    Subscription createSubscription(SubscriptionCreateRequestDTO createRequestDTO) throws Exception;
    List<Subscription> getAll();
    Subscription getById(final UUID id);
    void delete(final UUID id);
}
