package org.core.backend.ticketapp.marketing.service.impl;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.marketing.dto.social.FollowUserSocialLinkRequest;
import org.core.backend.ticketapp.marketing.entity.ExternalAppFollower;

public interface ExternalAppFollowerService extends IService<ExternalAppFollower> {
    ExternalAppFollower follow(FollowUserSocialLinkRequest request);
}
