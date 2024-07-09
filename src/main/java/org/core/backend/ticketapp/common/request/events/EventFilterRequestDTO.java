package org.core.backend.ticketapp.common.request.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.request.AbstractFilterRequestDTO;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

enum EventType {
    PHYSICAL, FREE, PAID, VIRTUAL
}

@AllArgsConstructor
@Getter
@Setter
public class EventFilterRequestDTO extends AbstractFilterRequestDTO {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateApproved;
    private ApprovalStatus approvalStatus;
    private EventCategoryEnum eventCategoryEnum;
    private UUID seatSectionId;
    private String seatSectionType; // eventTicketType each seat has section type
    private double startPrice;
    private double endPrice;
    private String address;
    private String state;
    private String country;
    private String artistName;
    private String eventType;
    private Boolean isPaidEvent;
    private Boolean isPhysicalEvent;
    private String title;
    private String description;
}