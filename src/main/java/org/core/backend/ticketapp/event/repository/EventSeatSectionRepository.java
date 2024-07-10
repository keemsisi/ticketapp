package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
@Transactional
public interface EventSeatSectionRepository extends JpaRepository<EventSeatSection, UUID>, PagingAndSortingRepository<EventSeatSection, UUID> {
}
