package org.core.backend.ticketapp.marketing.dto.job_advertisement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobAdvertisementRequest {
    @NotBlank(message = "title can't be blank")
    private String title;
    @NotBlank(message = "description can't be blank")
    private String description;
    @NotBlank(message = "industry can't be blank")
    private String industry;
    @NotBlank(message = "address can't be blank")
    private String address;
    @NotBlank(message = "address can't be blank")
    private String jobType;
    @NotNull(message = "deadlineDate can't be null")
    private LocalDateTime deadlineDate;
    @NotBlank(message = "cvUrl can't be blank")
    private String cvUrl;
    @NotBlank(message = "overview can't be blank")
    private String overview;
}
