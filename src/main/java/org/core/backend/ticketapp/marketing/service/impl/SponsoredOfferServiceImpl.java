package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing.repository.SponsoredOfferRepository;
import org.core.backend.ticketapp.marketing.service.SponsoredOfferService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SponsoredOfferServiceImpl implements SponsoredOfferService {
    private final SponsoredOfferRepository repository;
}
