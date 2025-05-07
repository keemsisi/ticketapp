package org.core.backend.ticketapp.passport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.github.thecarisma.CopyProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.NotificationProcessorStatus;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.type.PostgresUUIDType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Entity
@Table(name = "notification")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "UUID", typeClass = PostgresUUIDType.class)
@TypeDefs({
        @TypeDef(name = "JSON", typeClass = JsonStringType.class),
        @TypeDef(name = "JSONB", typeClass = JsonBinaryType.class),
        @TypeDef(name = "workflow_approval_status_enum", typeClass = PostgreSQLEnumType.class)
})
public class Notification {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;
    @Column(nullable = false)
    private UUID moduleId;
    @Column(nullable = false)
    private UUID requestedBy;

    @Column(name = "tenant_id", columnDefinition = "uuid not null")
    private UUID tenantId;

    @Type(type = "JSONB")
    @Column(name = "new_data", columnDefinition = "JSONB")
    private String newData;

    @Type(type = "JSONB")
    @Column(name = "old_data", columnDefinition = "JSONB")
    private String oldData;
    @Type(type = "JSONB")
    @Column(name = "meta_data", columnDefinition = "JSONB")
    private String metaData; //json string

    @Type(type = "workflow_approval_status_enum")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "workflow_approval_status_enum")
    private ApprovalStatus approvalStatus;

    @Column(name = "action_name", nullable = false)
    private String actionName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_created", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", nullable = false)
    private LocalDateTime dateCreated;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_approved", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateApproved;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "request_date", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private LocalDateTime dateRequested;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @Column(name = "date_modified", columnDefinition = "TIMESTAMPTZ")
    private LocalDateTime dateModified;

    @Type(type = "JSONB")
    @Column(name = "workflow", columnDefinition = "JSONB")
    private String workflow;

    //    @Transient
    @Column(name = "requested_by_name")
    private String requestedByName;
    @Column(name = "notification_for_user_name")
    private String notificationForUserName;

    @Column(name = "module_subscription_name")
    private String moduleSubscriptionName;

    @Column(name = "description")
    private String description;

    @Column(name = "workflow_id")
    private UUID workflowId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Column(name = "notification_for_user_id")
    private UUID notificationForUserId;

    @Column(name = "should_send_sms_alert")
    @CopyProperty(ignore = true)
    private boolean shouldSendSmsAlert = false;

    @Column(name = "should_send_email_alert")
    @CopyProperty(ignore = true)
    private boolean shouldSendEmailAlert = false;

    @Column(name = "message_id")
    private String messageId;
    @Column(name = "sub_action")
    private String subAction;
    @Column(name = "processor_remark")
    private String processorRemark;
    @Column(name = "processor_status")
    @Enumerated(EnumType.STRING)
    private NotificationProcessorStatus processorStatus;
    @Column(name = "processor_start_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime processorStartDate;
    @Column(name = "processor_end_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime processorEndDate;

    @Column(name = "should_send_in_app_alert", columnDefinition = "boolean default false")
    private boolean shouldSendInAppAlert = false;

    @Column(name = "processed_in_app_status_update", columnDefinition = "boolean default false")
    private boolean processedInAppStatusUpdate = false;

    @Transient
    private String title;
    @Transient
    private String finalApprovalByName;
    @Transient
    private String firstName;
    @Transient
    private String middleName;
    @Transient
    private String lastName;
    @Transient
    private Long index;

    @PrePersist
    public void onCreate() {
        if (ObjectUtils.isEmpty(processorStatus) && approvalStatus == ApprovalStatus.PENDING)
            processorStatus = NotificationProcessorStatus.PENDING;
        if (processorRemark == null) {
            processorRemark = "PENDING";
        }
    }

    public String getRequestedByName() {
        if (ObjectUtils.isNotEmpty(firstName) && ObjectUtils.isNotEmpty(lastName)) {
            String others = middleName == null ? "" : " " + middleName + " ";
            return firstName + others + lastName;
        }
        return requestedByName;
    }
}