package org.core.backend.ticketapp.passport.service;

import org.core.backend.ticketapp.event.service.IService;
import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.dtos.follower.InAppFollowerResponseDTO;
import org.core.backend.ticketapp.passport.entity.InAppFollower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InAppFollowerService extends IService<InAppFollower> {
    Page<InAppFollowerResponseDTO> getAllV2(FilterInAppFollowerRequestDTO request, Pageable pageable);
}
