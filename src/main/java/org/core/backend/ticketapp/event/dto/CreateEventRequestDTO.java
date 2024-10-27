package org.core.backend.ticketapp.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.core.backend.ticketapp.common.enums.EventTicketType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEventRequestDTO {
    @NotBlank
    @NotNull
    private String title;

    @NotNull
    @NotBlank
    @Length(max = 1000)
    private String description;

    @NotNull
    @NotBlank
    private String location;

    @NotNull
    @NotBlank
    private String eventBanner = "event-banner.jpg";

    @NotNull
    @NotBlank
    private String streetAddress;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "eventEndDate is required!")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventEndDate;

    @NotNull(message = "theme is required!")
    private String theme;

    @NotNull
    private List<EventSeatSection> seatSections;

    @NotNull
    private EventTicketType ticketType;

    @NotBlank(message = "eventType is required")
    private String eventType;

    private BankAccountDetailsDTO bankAccountDetails;
    private boolean physicalEvent;
    private boolean freeEvent;
    private int locationNumber;
    private boolean isPublic = true;
    private String timeZone = "Africa/Lagos";
    private boolean approvalRequired;
    private boolean recurring = false;
    private Set<String> categories;
    private String link;
    private String frequency;

    @JsonProperty(value = "frequency")
    public String getFrequency() {
        if (!isRecurring() && ObjectUtils.isEmpty(frequency)) {
            throw new ApplicationException(400, "not_allowed", "Frequency of the event is required!");
        }
        return frequency;
    }
}
