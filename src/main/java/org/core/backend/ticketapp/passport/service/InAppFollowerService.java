package org.core.backend.ticketapp.passport.service;

import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.springframework.data.domain.Pageable;

public interface InAppFollowerService extends IService<InAppFollower> {
    Page<InAppFollower> getAllV1(FilterInAppFollowerRequestDTO filter, Pageable pageable);

    Page<InAppFollower> getAllV2(FilterInAppFollowerRequestDTO request, Pageable pageable);
}
