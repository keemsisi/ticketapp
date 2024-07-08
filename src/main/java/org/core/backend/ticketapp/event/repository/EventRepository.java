package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>, PagingAndSortingRepository<Event, UUID> {
//    List<Event> getEventByCategory(String category, String searchParams);
}
