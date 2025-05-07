package org.core.backend.ticketapp.marketing.dto.formdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFormDataRequest {
    @NotBlank(message = "title can't be blank")
    private String title;
    @NotBlank(message = "description can't be blank")
    private String description;
}
