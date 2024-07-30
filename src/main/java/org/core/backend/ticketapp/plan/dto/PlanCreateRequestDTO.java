package org.core.backend.ticketapp.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Digits;
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
        @Digits(integer = 6, fraction = 2)
        private Integer amount;
}
