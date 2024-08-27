package org.core.backend.ticketapp.subscription.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.entity.Plan;
import org.core.backend.ticketapp.subscription.dto.SubscriptionCreateRequestDTO;
import org.core.backend.ticketapp.subscription.dto.SubscriptionCreateResponseDTO;
import org.core.backend.ticketapp.subscription.entity.Subscription;
import org.core.backend.ticketapp.subscription.repository.SubscriptionRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Column;
import java.util.List;
import java.util.UUID;

import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_PLAN_BASE_URL;
import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_SUBSCRIPTION_BASE_URL;

@Slf4j
@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;
    private final AppConfigs appConfigs;
    private final RestTemplate restTemplate;

    @Override
    public List<Subscription> getAll() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream().toList();
    }

    @Override
    public Subscription createSubscription(final SubscriptionCreateRequestDTO createSubscriptionDTO) throws Exception {
        ResponseEntity<SubscriptionCreateResponseDTO> response = null;
        try {
            final var headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + appConfigs.secretKey);
            headers.set("Content-Type", "application/json");
            final var entity = new HttpEntity<>(createSubscriptionDTO, headers);
            response = restTemplate.exchange(PAYSTACK_SUBSCRIPTION_BASE_URL, HttpMethod.POST, entity, SubscriptionCreateResponseDTO.class);
            if (response.getStatusCode().isError()) {
                throw new ApplicationException(400, "req_failed", "Failed to create subscription");
            }

            final var newSubscription = new Subscription();
            newSubscription.setId(UUID.randomUUID());
            newSubscription.setStatus(response.getBody().data().status());
            newSubscription.setVendorId(response.getBody().data().customer());
            newSubscription.setAmount(response.getBody().data().amount());
            newSubscription.setCode(response.getBody().data().subscriptionCode());
            return subscriptionRepository.save(newSubscription);
        } catch (Exception ex) {
            log.error(">>> An Exception occurred : ", ex);
        }
        throw new ApplicationException(400, "req_failed", "Failed to create subscription");
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
