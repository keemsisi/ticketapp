package org.core.backend.ticketapp.marketing.service;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.marketing.entity.SponsoredOffer;

import java.util.Optional;

public interface SponsoredOfferService extends IService<SponsoredOffer> {
    Optional<SponsoredOffer> findByCode(String code);
}
