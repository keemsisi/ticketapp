package org.core.backend.ticketapp.event.repository;

import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>, PagingAndSortingRepository<Event, UUID> {

    @Query(value = "SELECT e FROM event e  WHERE " +
            "(:approvalStatus IS NULL OR e.approvalStatus = :approvalStatus) AND " +
            "(:eventCategory IS NULL OR e.eventCategory = :eventCategory) AND " +
            "(:title IS NULL OR e.title LIKE %:title%) AND " +
            "(:address IS NULL OR e.streetAddress LIKE %:address%) AND " +
            "(:state IS NULL OR e.location LIKE %:state%) AND " +
            "(:country IS NULL OR e.location LIKE %:country%) AND " +
            "(:isPaidEvent IS NULL OR e.freeEvent = :isPaidEvent) AND " +
            "(:isPhysicalEvent IS NULL OR e.physicalEvent = :isPhysicalEvent)" +
            "(:description is NULL e.description LIKE %:artistName%)" +
            "(:description IS NULL OR e.description LIKE %:description%)",
            nativeQuery = true)
    List<Event> findByFilter(
            ApprovalStatus approvalStatus, EventCategoryEnum eventCategory, String title, String address,
            String state, String country, boolean isPaidEvent, boolean isPhysicalEvent, String artistName, String description
    );

}
