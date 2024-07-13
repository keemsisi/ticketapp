package org.core.backend.ticketapp.common.request.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.EventCategoryEnum;
import org.core.backend.ticketapp.common.request.AbstractFilterRequestDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

enum EventType {
    PHYSICAL, FREE, PAID, VIRTUAL
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventFilterRequestDTO extends AbstractFilterRequestDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateApproved;
    private ApprovalStatus approvalStatus;
    private EventCategoryEnum eventCategory;
    private UUID seatSectionId;
    private String seatSectionType; // eventTicketType each seat has section type
    private Double startPrice;
    private Double endPrice;
    private String address;
    private String state;
    private String location;
    private String artistName;
    private Boolean isPaidEvent;
    private Boolean isPhysicalEvent;
    private String title;
    private String description;

}