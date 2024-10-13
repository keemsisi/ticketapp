package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing.repository.EventPromotionRepository;
import org.core.backend.ticketapp.marketing.service.EventPromotionService;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class EventPromotionServiceImpl implements EventPromotionService {
    private final EventPromotionRepository repository;
}
