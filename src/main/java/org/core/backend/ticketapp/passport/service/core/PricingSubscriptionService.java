package org.core.backend.ticketapp.passport.service.core;


import org.core.backend.ticketapp.passport.dao.PricingSubscriptionDao;
import org.core.backend.ticketapp.passport.entity.PricingSubscription;
import org.core.backend.ticketapp.passport.repository.PricingSubscriptionRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;
import java.util.UUID;


@Service
public class PricingSubscriptionService extends BaseRepoService<PricingSubscription> {

    @Autowired
    private PricingSubscriptionRepository departmentRepository;

    @Autowired
    private PricingSubscriptionDao pricingSubscriptionDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public PricingSubscriptionService(PricingSubscriptionRepository repository) {
        super(repository);
    }

    public Object getAll(String name, Pageable pageable) throws ParseException {
        if (pageable == null) {
            return departmentRepository.getAll(name, jwtTokenUtil.getUser().getTenantId());
        } else {
            return pricingSubscriptionDao.getAll(name, pageable);
        }
    }

    public Optional<PricingSubscription> getByName(String name) {
        return departmentRepository.findByName(name, jwtTokenUtil.getUser().getTenantId());
    }

    public Optional<PricingSubscription> getByUUID(UUID id) {
        return departmentRepository.findByUUID(id);
    }
}
