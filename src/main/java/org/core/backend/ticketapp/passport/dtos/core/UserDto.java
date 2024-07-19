package org.core.backend.ticketapp.passport.dtos.core;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.thecarisma.CopyProperty;
import io.github.thecarisma.ExcelColumn;
import lombok.Data;
import org.core.backend.ticketapp.common.enums.UserType;
import org.core.backend.ticketapp.passport.entity.UserRole;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

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

    @ExcelColumn(columnName = "JOB DESCRIPTION", failIfAbsent = false)
    private String jobDescription;

    @ExcelColumn(columnName = "DEPARTMENT ID", failIfAbsent = false)
    private UUID departmentId;

    @ExcelColumn(columnName = "CHN", failIfAbsent = false)
    private String chn = "";

    @ExcelColumn(columnName = "ACCOUNT NUMBER", failIfAbsent = false)
    private String accountNumber = "";

    @Convert(disableConversion = true)
    @CopyProperty(ignore = true)
    @Type(type = "numeric_boolean")
    private boolean locked;

    @Convert(disableConversion = true)
    @CopyProperty(ignore = true)
    private boolean deactivated;

    private DateTime lastChangePasswordDate;

    private List<UserRole> roles;
    private List<UUID> roleIds = List.of();
    private List<UUID> groupIds = List.of();
    private List<UUID> actionIds = List.of();

    private List<UserPermission> permissions = new ArrayList<>();

    private boolean firstTimeLogin;

    private String adminRegisterKey;

    private boolean isSupervisor;

    private boolean isSuperAdmin;

    private Calendar passwordExpiryDate;

    private DateTime lockDate;

    private int loginAttempt;

    @NotNull
    private UserType type = UserType.INDIVIDUAL;
}