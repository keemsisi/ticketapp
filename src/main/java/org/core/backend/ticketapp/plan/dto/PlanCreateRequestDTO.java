package org.core.backend.ticketapp.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCreateRequestDTO {
    @NotNull(message = "Plan name cannot be null")
    private String name;
    @NotNull(message = "Interval cannot be null")
    private String interval;
    @NotNull(message = "Amount cannot be null")
    @Min(value = 100, message = "Amount is invalid. It must be a number and be 100 or greater")
    private Double amount;
    private String description;
    private String currency;
}
