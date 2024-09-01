package org.core.backend.ticketapp.passport.dtos.core;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.thecarisma.CopyProperty;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.passport.entity.UserRole;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Convert;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto extends UserLiteDto {
    private final List<UUID> roleIds = List.of();
    private final List<UUID> groupIds = List.of();
    private final List<UUID> actionIds = List.of();
    private final List<UserPermission> permissions = new ArrayList<>();
    @Convert(disableConversion = true)
    @CopyProperty(ignore = true)
    @Type(type = "numeric_boolean")
    private boolean locked;
    @Convert(disableConversion = true)
    @CopyProperty(ignore = true)
    private boolean deactivated;
    private DateTime lastChangePasswordDate;
    private List<UserRole> roles;
    private boolean firstTimeLogin;
    private Calendar passwordExpiryDate;
    private DateTime lockDate;
    private int loginAttempt;
    private String password;
    @JsonIgnore
    private UUID tenantId;
}