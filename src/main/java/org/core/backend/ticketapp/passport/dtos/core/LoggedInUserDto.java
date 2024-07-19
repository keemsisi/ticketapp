package org.core.backend.ticketapp.passport.dtos.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.thecarisma.ExcelColumn;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoggedInUserDto {

    @JsonProperty("user_id")
    private UUID userId;
    @JsonProperty("client_id")
    private UUID clientId;

    @JsonProperty("tenant_id")
    private UUID tenantId;

    @Email
    @ExcelColumn(columnName = "Email")
    @NotBlank
    private String email;

    @ExcelColumn(columnName = "First Name")
    @NotBlank
    @JsonProperty("first_name")
    private String firstName;

    @ExcelColumn(columnName = "Middle Name")
    private String middleName;

    @ExcelColumn(columnName = "Last Name")
    @JsonProperty("last_name")
    private String lastName;

    private String profilePictureLocation;

    @ExcelColumn(columnName = "Phone", failIfAbsent = false)
    private String phone;

    private String department;

    private String unit;

    private List<String> roles = new ArrayList<>();

    private List<String> scope = new ArrayList<>();
}