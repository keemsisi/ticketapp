package org.core.backend.ticketapp.marketing.dao;

import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.springframework.data.domain.Pageable;

public interface InAppFollowerDAOService {

    Page<InAppFollower> filterSearchFollowers(FilterInAppFollowerRequestDTO request, Pageable pageable);
}
