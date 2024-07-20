package org.core.backend.ticketapp.passport.dtos.core;

import io.github.thecarisma.CopyProperty;
import io.github.thecarisma.ExcelColumn;
import lombok.*;
import org.core.backend.ticketapp.common.enums.UserType;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLiteDto {
    @NotNull
    private UserType type;

    @CopyProperty(ignore = true)
    private UUID id;

    @Email
    @ExcelColumn(columnName = "EMAIL")
    @NotBlank
    private String email;

    @ExcelColumn(columnName = "FIRST NAME")
    @NotBlank
    private String firstName;

    @ExcelColumn(columnName = "MIDDLE NAME")
    private String middleName;

    @ExcelColumn(columnName = "LAST NAME")
    private String lastName;
    private String profilePictureLocation;

    @ExcelColumn(columnName = "DOB")
    private LocalDate dob;
    private String password;

    @ExcelColumn(columnName = "PHONE", failIfAbsent = false)
    private String phone;

    @NotBlank
    @NotNull
    @ExcelColumn(columnName = "GENDER", failIfAbsent = false)
    private String gender;

    @ExcelColumn(columnName = "OCCUPATION", failIfAbsent = false)
    private String occupation;

    @ExcelColumn(columnName = "MARITAL STATUS", failIfAbsent = false)
    private String maritalStatus;

    @ExcelColumn(columnName = "ADDRESS", failIfAbsent = false)
    @Column(name = "address")
    private String address;

    @ExcelColumn(columnName = "COUNTRY", failIfAbsent = false)
    private String country;

    @ExcelColumn(columnName = "CITY", failIfAbsent = false)
    private String city;

    @ExcelColumn(columnName = "POSTAL CODE", failIfAbsent = false)
    @Column(name = "postal_code")
    private String postalCode;

    @ExcelColumn(columnName = "STATE OF ORIGIN", failIfAbsent = false)
    private String stateOfOrigin;

    @ExcelColumn(columnName = "LGA OF ORIGIN", failIfAbsent = false)
    private String lgaOfOrigin;
}