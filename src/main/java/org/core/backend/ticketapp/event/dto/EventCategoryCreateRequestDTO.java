package org.core.backend.ticketapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryCreateRequestDTO {
    private String name;
    private String description;
}
