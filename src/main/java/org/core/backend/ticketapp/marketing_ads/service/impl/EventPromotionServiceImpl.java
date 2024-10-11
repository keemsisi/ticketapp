package org.core.backend.ticketapp.marketing_ads.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing_ads.repository.EventPromotionRepository;
import org.core.backend.ticketapp.marketing_ads.service.EventPromotionService;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EventPromotionServiceImpl implements EventPromotionService {
    private final EventPromotionRepository repository;
}
