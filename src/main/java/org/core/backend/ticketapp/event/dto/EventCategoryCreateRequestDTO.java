package org.core.backend.ticketapp.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCategoryCreateRequestDTO {
    @NonNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
}
