package org.core.backend.ticketapp.marketing_ads.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing_ads.repository.FormDataRepository;
import org.core.backend.ticketapp.marketing_ads.service.FormDataService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FormDataServiceImpl implements FormDataService {
    private final FormDataRepository repository;
}
