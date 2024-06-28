package org.core.backend.ticketapp.passport.dtos.unit;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UnitDto {
    @NotNull
    private String name;
    private String description;
    @NotNull
    private UUID departmentId;
}
