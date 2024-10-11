package org.core.backend.ticketapp.marketing_ads.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing_ads.repository.SocialMediaLinkAdvertisementRepository;
import org.core.backend.ticketapp.marketing_ads.service.SocialMediaLinkAdvertisementService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SocialMediaLinkAdvertisementServiceImpl implements SocialMediaLinkAdvertisementService {
    private final SocialMediaLinkAdvertisementRepository repository;

}
