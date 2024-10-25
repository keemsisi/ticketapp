package org.core.backend.ticketapp.marketing.dto.formdata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerFormDataRequest {
    @NotBlank(message = "firstName can't be blank")
    private String firstName;
    @NotBlank(message = "lastName can't be blank")
    private String lastName;
    @Max(value = 15, message = "phoneNumber max is 15")
    @NotBlank(message = "phoneNumber can't be blank")
    private String phoneNumber;
    @Email(message = "Oops! Invalid email address")
    @NotBlank(message = "email can't be blank")
    private String email;
    @NotNull(message = "dob can't be null")
    private LocalDateTime dob;
    @NotBlank(message = "address can't be blank")
    private String address;
    @NotBlank(message = "code can't be blank")
    private String code;
}
