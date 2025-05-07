package org.core.backend.ticketapp.marketing.dto.job_advertisement.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobAdvertisementApplicationRequest extends CreateJobAdvertisementApplicationRequest {
    @NotNull(message = "applicationId can't be null")
    private UUID id;
}
