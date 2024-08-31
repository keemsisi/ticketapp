package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.SubscriptionStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;


@Entity
@Getter
@Setter
@Table(name = "tenant", indexes = {@Index(name = "ix_tbl_tenant_col_name_uq", columnList = "normalized_name", unique = true)})
public class Tenant extends AbstractBaseEntity {
    @Column(name = "plan_id")
    private UUID planId;
    @Column(name = "name")
    private String name;
    @Column(name = "normalized_name")
    private String normalizedName;
    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "currency")
    private String currency;

    @Column(name = "logo_location")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String logoLocation;

    @Column(name = "password_expiration_in_days", columnDefinition = "int default(90)")
    private int passwordExpirationInDays;

    @Column(name = "account_lockout_duration_in_minutes", columnDefinition = "int default(60)")
    private int accountLockoutDurationInMinutes;

    @Column(name = "inactive_period_in_minutes", columnDefinition = "int default(5)")
    private int inactivePeriodInMinutes;

    @Column(name = "account_lockout_threshold_count", columnDefinition = "int default(3)")
    private int accountLockoutThresholdCount;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    private Date modifiedOn;

    @Column(name = "deleted", columnDefinition = "bool default(false)")
    private boolean deleted;

    @Column(name = "system_alert_id")
    private UUID systemAlertId;

    @Column(name = "two_fa_enabled", columnDefinition = "bool default(false)")
    private boolean twoFaEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", columnDefinition = "varchar(255) default 'ACTIVE'")
    private SubscriptionStatus subscriptionStatus;

    @PrePersist
    public void onCreate() {
        if (Objects.isNull(id)) this.id = UUID.randomUUID();
        if (Objects.isNull(dateCreated)) this.dateCreated = LocalDateTime.now();
        if (Objects.isNull(subscriptionStatus)) this.subscriptionStatus = SubscriptionStatus.ACTIVE;
    }
}
