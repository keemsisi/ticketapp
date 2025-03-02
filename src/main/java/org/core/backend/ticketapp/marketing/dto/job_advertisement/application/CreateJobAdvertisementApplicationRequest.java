package org.core.backend.ticketapp.marketing.dto.job_advertisement.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobAdvertisementApplicationRequest {
    @NotNull(message = "jobId can't be null")
    private UUID jobId;
    @Email(message = "valid email address is required")
    private String email;
    @NotBlank(message = "fullName max is 250!")
    @Length(max = 5000, message = "resumeUlr max is 250!")
    private String fullName;
    @NotBlank(message = "resumeUlr max is 5000!")
    @Length(max = 5000, message = "resumeUlr max is 5000!")
    private String resumeUrl;
    @Length(max = 10000, message = "coverLetter min and max is required!")
    private String coverLetter;
    private String linkedinProfile;
}
