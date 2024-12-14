package org.core.backend.ticketapp.marketing.dao;

import org.core.backend.ticketapp.passport.dtos.follower.FilterInAppFollowerRequestDTO;
import org.core.backend.ticketapp.passport.dtos.follower.InAppFollowerResponseDTO;
import org.springframework.data.domain.Page;

public interface InAppFollowerDAOService {
    Page<InAppFollowerResponseDTO> filterSearch(FilterInAppFollowerRequestDTO request);
}
