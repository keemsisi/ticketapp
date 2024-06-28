package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.thecarisma.ExcelColumn;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAvatar {

    private UUID userId;
    @Email
    @ExcelColumn(columnName = "Email")
    @NotBlank
    private String email;

    @ExcelColumn(columnName = "First Name")
    @NotBlank
    private String firstName;

    @ExcelColumn(columnName = "Middle Name")
    private String middleName;

    @ExcelColumn(columnName = "Last Name")
    private String lastName;

    private String profilePictureLocation;
}