package org.core.backend.ticketapp.subscription.service;

import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.subscription.entity.Subscription;
import org.core.backend.ticketapp.subscription.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;

    @Override
    public List<Subscription> getAll() {
        List<Subscription> orders = subscriptionRepository.findAll();
        return orders.stream().toList();
    }

    @Override
    public Subscription getById(UUID id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found", id.toString()));
    }

    @Override
    public void delete(UUID id) {
        Subscription order = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found", id.toString()));
        subscriptionRepository.delete(order);
    }
}
