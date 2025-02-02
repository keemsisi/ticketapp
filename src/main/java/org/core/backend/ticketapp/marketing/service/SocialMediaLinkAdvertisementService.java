package org.core.backend.ticketapp.marketing.service;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;
import org.core.backend.ticketapp.marketing.entity.SocialMediaLinkAdvertisement;

public interface SocialMediaLinkAdvertisementService extends IService<SocialMediaLinkAdvertisement> {

    ExternalAppFollower follow(final FollowUserSocialLinkRequest request);
}
