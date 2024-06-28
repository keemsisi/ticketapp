package org.core.backend.ticketapp.passport.dtos.core;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.thecarisma.CopyProperty;
import io.github.thecarisma.ExcelColumn;
import lombok.Data;
import org.core.backend.ticketapp.common.enums.Gender;
import org.core.backend.ticketapp.common.util.ExcelColumnToBoolean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    private UUID id;

    @Email
    @Column(name = "email")
    @CopyProperty(ignore = true)
    @ExcelColumn(columnName = "Email")
    private String email;

    @Column(name = "first_name")
    @ExcelColumn(columnName = "First Name")
    private String firstName;

    @Column(name = "middle_name")
    @ExcelColumn(columnName = "Middle Name")
    private String middleName;

    @Column(name = "last_name")
    @ExcelColumn(columnName = "Last Name")
    private String lastName;

    @Column(name = "profile_picture_location")
    private String profilePictureLocation;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "phone")
    @ExcelColumn(columnName = "Phone", failIfAbsent = false)
    private String phone;

    @Column(name = "gender")
    @ExcelColumn(columnName = "Gender", failIfAbsent = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "occupation")
    @ExcelColumn(columnName = "Occupation", failIfAbsent = false)
    private String occupation;

    @ExcelColumn(columnName = "Marital Status", failIfAbsent = false)
    @Column(name = "marital_status")
    private String maritalStatus;

    @ExcelColumn(columnName = "Address", failIfAbsent = false)
    @Column(name = "address")
    private String address;

    @ExcelColumn(columnName = "Country", failIfAbsent = false)
    @Column(name = "country")
    private String country;

    @ExcelColumn(columnName = "City", failIfAbsent = false)
    @Column(name = "city")
    private String city;

    @ExcelColumn(columnName = "Postal Code", failIfAbsent = false)
    @Column(name = "postal_code")
    private String postalCode;

    @ExcelColumn(columnName = "State Of Origin", failIfAbsent = false)
    @Column(name = "state_of_origin")
    private String stateOfOrigin;

    @ExcelColumn(columnName = "LGA Of Origin", failIfAbsent = false)
    @Column(name = "lga_of_origin")
    private String lgaOfOrigin;

    @ExcelColumn(columnName = "Job Description", failIfAbsent = false)
    @Column(name = "job_description")
    private String jobDescription;

    @ExcelColumn(columnName = "PricingSubscription", failIfAbsent = false)
    @Column(name = "department_id")
    private UUID departmentId;

    @ExcelColumn(columnName = "Unit", failIfAbsent = false)
    @Column(name = "unit_id")
    private UUID unitId;

    @Convert(disableConversion = true)
    @Column(name = "locked")
    private boolean locked;

    @Column(name = "deactivated")
    private boolean deactivated;

    @CopyProperty(ignore = true)
    @Transient
    private Set<UserRoleDto> roles = new HashSet<>();

    @CopyProperty(ignore = true)
    @Transient
    private Set<UserActionDto> actions = new HashSet<>();

    @CopyProperty(ignore = true)
    @JsonIgnore
    @Column(name = "first_time_login")
    private boolean firstTimeLogin;

    @CopyProperty(ignore = true)
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String adminRegisterKey;

    @Transient
    @ExcelColumn(columnName = "IsSupervisor", converter = ExcelColumnToBoolean.class)
    private boolean isSupervisor;

    @Transient
    @ExcelColumn(columnName = "IsSuperAdmin", converter = ExcelColumnToBoolean.class)
    private boolean isSuperAdmin;

    @Column(name = "password_expiry_date", columnDefinition = "TIMESTAMP")
    @ExcelColumn(columnName = "Password Expiry Date")
    private Date passwordExpiryDate;

    @Column(name = "password_created_on", columnDefinition = "TIMESTAMP")
    @ExcelColumn(columnName = "Password Creation Date")
    private Date passwordCreatedOn;

    @Column(name = "lock_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockDate;

    @Column(name = "login_attempt")
    private int loginAttempt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "modified_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(name = "is_deleted")
    private String isDeleted;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return deactivated;
    }

    @Override
    public boolean isEnabled() {
        return deactivated;
    }

    @Data
    public static class Request {
        @NotNull
        @NotEmpty
        @NotBlank(message = "The username value cannot be empty")
        String email;

        @NotNull
        @NotEmpty
        @NotBlank(message = "The password value cannot be empty")
        String password;
    }

    @Data
    public static class Response {
        String accessToken;

        String message;

        User user;

        boolean firstTimeLogin = true;
    }

}