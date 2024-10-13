package org.core.backend.ticketapp.marketing.service.impl;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.marketing.repository.FormDataRepository;
import org.core.backend.ticketapp.marketing.service.FormDataService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FormDataServiceImpl implements FormDataService {
    private final FormDataRepository repository;
}
