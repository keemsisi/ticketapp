package org.core.backend.ticketapp.passport.dtos.department;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.validation.constraints.NotBlank;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DepartmentDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
}
