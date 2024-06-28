package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "reminder_notification", indexes = {@Index(name = "ix_tbl_col_reminder_date_uq", columnList = "reminder_date")})
public class ReminderNotification {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "description", columnDefinition = "VARCHAR(250) DEFAULT 'notification'")
    private String description;

    @Column(name = "date_created", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private LocalDateTime dateCreated;

    @Column(name = "modified_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime dateModified;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "modified_by")
    private UUID modifiedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Column(name = "reminder_date", nullable = false)
    private LocalDateTime reminderDate;

    @Column(name = "repeat", nullable = false)
    private boolean repeat;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "count")
    private Integer count = 0;

    @Column(name = "user_email")
    @JsonIgnore
    private String userEmail;
}
