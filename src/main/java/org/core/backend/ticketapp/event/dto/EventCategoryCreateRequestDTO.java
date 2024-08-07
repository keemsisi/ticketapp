package org.core.backend.ticketapp.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    private List<String> keywords;
}
