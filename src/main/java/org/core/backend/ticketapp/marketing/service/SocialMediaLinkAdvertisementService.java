package org.core.backend.ticketapp.marketing.service;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.marketing.dto.social.FilterSearchSocialMediaLinksRequest;
import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SocialMediaLinkAdvertisementService extends IService<SocialMediaLinkAdvertisement> {

    Page<SocialMediaLinkAdvertisement> getAll(FilterSearchSocialMediaLinksRequest request, Pageable pageable);
}
