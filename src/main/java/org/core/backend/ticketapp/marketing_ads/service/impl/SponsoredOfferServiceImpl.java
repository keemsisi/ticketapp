package org.core.backend.ticketapp.marketing_ads.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing_ads.repository.SponsoredOfferRepository;
import org.core.backend.ticketapp.marketing_ads.service.SponsoredOfferService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SponsoredOfferServiceImpl implements SponsoredOfferService {
    private final SponsoredOfferRepository repository;
}
