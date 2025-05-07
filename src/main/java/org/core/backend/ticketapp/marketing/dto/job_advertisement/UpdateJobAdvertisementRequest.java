package org.core.backend.ticketapp.marketing.dto.job_advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateJobAdvertisementRequest extends CreateJobAdvertisementRequest {
    @NotNull
    private UUID id;
}
