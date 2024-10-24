package org.core.backend.ticketapp.marketing.dto.formdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFormDataRequest extends CreateFormDataRequest {
    @NotNull(message = "id is required!")
    private UUID id;
}
