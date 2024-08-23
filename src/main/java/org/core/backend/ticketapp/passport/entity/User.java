package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.thecarisma.CopyProperty;
import io.github.thecarisma.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.enums.UserType;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users", indexes = {@Index(name = "ix_tbl_user_col_email_uq", columnList = "email", unique = true)})
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@OptimisticLocking(type = OptimisticLockType.VERSION)
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Email
    @Column(name = "email", nullable = false)
    @ExcelColumn(columnName = "EMAIL")
    private String email;

    @Column(name = "first_name", nullable = false)
    @ExcelColumn(columnName = "FIRST NAME")
    private String firstName;

    @Column(name = "middle_name")
    @ExcelColumn(columnName = "MIDDLE NAME")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    @ExcelColumn(columnName = "LAST NAME")
    private String lastName;

    @Column(name = "profile_picture_location")
    @ExcelColumn(columnName = "PROFILE PICTURE URL")
    private String profilePictureLocation;

    @Column(name = "dob")
    private LocalDateTime dob;

    @ExcelColumn(columnName = "DOB")
    @Transient
    @JsonIgnore
    private String dateOfBirth;


    @ExcelColumn(columnName = "DEPARTMENT ID", failIfAbsent = false)
    @Transient
    @JsonIgnore
    private String departmentIdString;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "phone", nullable = false)
    @ExcelColumn(columnName = "PHONE")
    private String phone;

    @Column(name = "gender", nullable = false)
    @ExcelColumn(columnName = "GENDER")
    private String gender;

    @Column(name = "occupation")
    @ExcelColumn(columnName = "OCCUPATION", failIfAbsent = false)
    private String occupation;

    @ExcelColumn(columnName = "MARITAL STATUS", failIfAbsent = false)
    @Column(name = "marital_status")
    private String maritalStatus;

    @ExcelColumn(columnName = "HOME ADDRESS", failIfAbsent = false)
    @Column(name = "address")
    private String address;

    @ExcelColumn(columnName = "COUNTRY", failIfAbsent = false)
    @Column(name = "country")
    private String country;

    @ExcelColumn(columnName = "CITY", failIfAbsent = false)
    @Column(name = "city")
    private String city;

    @ExcelColumn(columnName = "POSTAL CODE", failIfAbsent = false)
    @Column(name = "postal_code")
    private String postalCode;

    @ExcelColumn(columnName = "STATE OF ORIGIN", failIfAbsent = false)
    @Column(name = "state_of_origin")
    private String stateOfOrigin;

    @ExcelColumn(columnName = "LGA OF ORIGIN", failIfAbsent = false)
    @Column(name = "lga_of_origin")
    private String lgaOfOrigin;

    @ExcelColumn(columnName = "JOB DESCRIPTION", failIfAbsent = false)
    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "locked", columnDefinition = "bool default(false)")
    private boolean locked;

    @Column(name = "deactivated", columnDefinition = "bool default(false)")
    private boolean deactivated;

    @Transient
    private List<String> roles = new LinkedList<>();


    @Transient
    private List<String> groups = new LinkedList<>();

    @Transient
    private List<String> actions = new LinkedList<>();

    @CopyProperty(ignore = true)
    @JsonIgnore
    @Column(name = "first_time_login", columnDefinition = "bool default(true)")
    private boolean firstTimeLogin;

    @CopyProperty(ignore = true)
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String adminRegisterKey;

    @Column(name = "password_created_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Instant passwordCreatedOn;

    @Column(name = "lock_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockDate;

    @Column(name = "login_attempt", columnDefinition = "int default(0)")
    private int loginAttempt;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "last_failed_login")
    private Date lastFailedLogin;

    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "locked_by")
    private UUID lockedBy;

    @Column(name = "modified_on", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(name = "deleted", columnDefinition = "bool default(false)")
    private boolean deleted;

    @Column(name = "tenant_id", columnDefinition = "UUID")
    private UUID tenantId;

    @Column(name = "password_expiry_date")
    private LocalDateTime passwordExpiryDate;
    /* SECONDARY TABLES COLUMNS */
    @Transient
    private String department;
    @Transient
    private Integer passwordExpirationInDays;
    @Transient
    private Integer accountLockoutDurationInMinutes;
    @Transient
    private Integer inactivePeriodInMinutes;
    @Transient
    private Integer accountLockoutThresholdCount;
    @Transient
    private Boolean twoFaEnabled = false;

    @JsonIgnore
    @Version
    @Column(name = "version", columnDefinition = "numeric(19,2) default 0")
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType userType;


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

    public String getFullName() {
        return String.format("%s %s %s", lastName, StringUtils.defaultIfBlank(middleName, ""), firstName);
    }

    public boolean isTwoFaEnabled() {
        return twoFaEnabled;
    }
}