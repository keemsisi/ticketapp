package org.core.backend.ticketapp.event.service;

import org.core.backend.ticketapp.event.entity.EventWishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventWishListService extends IService<EventWishList> {
    Page<EventWishList> getUserWisList(Pageable pageable);
}
