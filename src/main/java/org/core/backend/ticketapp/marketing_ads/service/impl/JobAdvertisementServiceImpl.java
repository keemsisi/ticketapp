package org.core.backend.ticketapp.marketing_ads.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing_ads.repository.JobAdvertisementRepository;
import org.core.backend.ticketapp.marketing_ads.service.JobAdvertisementService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobAdvertisementServiceImpl implements JobAdvertisementService {
    private final JobAdvertisementRepository repository;
}
