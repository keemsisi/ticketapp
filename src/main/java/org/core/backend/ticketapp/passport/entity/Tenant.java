package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.core.backend.ticketapp.common.dto.OperationalHours;
import org.core.backend.ticketapp.common.dto.SocialMediaDTO;
import org.core.backend.ticketapp.common.entity.AbstractBaseEntity;
import org.core.backend.ticketapp.common.enums.SubscriptionStatus;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Entity
@Getter
@Setter
@Table(name = "tenant", indexes = {
        @Index(name = "ix_tbl_tenant_col_name_uq", columnList = "normalized_name", unique = true)
})
@TypeDefs({@TypeDef(name = "JSONB", typeClass = JsonBinaryType.class),})
public class Tenant extends AbstractBaseEntity {
    @Column(name = "plan_id")
    private UUID planId;
    @Column(name = "normalized_name")
    private String normalizedName;

    @JsonIgnore
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

    @Column(name = "created_on")
    private Date createdOn;

    @Column(name = "modified_on")
    private Date modifiedOn;

    @Column(name = "system_alert_id")
    private UUID systemAlertId;

    @Column(name = "two_fa_enabled", columnDefinition = "bool default(false)")
    private boolean twoFaEnabled;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_status", columnDefinition = "varchar(255) default 'ACTIVE'")
    private SubscriptionStatus subscriptionStatus;

    //business profile
    @Column(name = "website_url")
    private String websiteUrl;
    @Column(name = "name")
    private String name;
    @Type(type = "JSONB")
    @Column(name = "galleries", columnDefinition = "JSONB default null")
    private List<String> galleries;
    @Column(name = "logo_url")
    private String logoUrl;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "secondary_phone_number")
    private String secondaryPhoneNumber;
    @Column(name = "description", columnDefinition = "varchar(2500)")
    private String description;
    @Type(type = "JSONB")
    @Column(name = "social_media_handles", columnDefinition = "JSONB default null")
    private List<SocialMediaDTO> socialMediaHandles;
    @Column(name = "address")
    private String address;
    @Column(name = "country")
    private String country;
    @Column(name = "phone")
    private String phone;
    @Email(message = "Oops! email is invalid!")
    @Column(name = "email")
    private String email;
    @Column(name = "state")
    private String state;
    @Column(name = "currency")
    private String currency;
    @Type(type = "JSONB")
    @Column(name = "operational_hours")
    private OperationalHours operationalHours;

    private String logoLocation;

    @PrePersist
    public void onCreate() {
        if (Objects.isNull(id)) this.id = UUID.randomUUID();
        if (Objects.isNull(dateCreated)) this.dateCreated = LocalDateTime.now();
        if (Objects.isNull(subscriptionStatus)) this.subscriptionStatus = SubscriptionStatus.ACTIVE;
    }
}
