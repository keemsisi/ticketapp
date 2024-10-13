package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing.repository.JobAdvertisementRepository;
import org.core.backend.ticketapp.marketing.service.JobAdvertisementService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JobAdvertisementServiceImpl implements JobAdvertisementService {
    private final JobAdvertisementRepository repository;
}
