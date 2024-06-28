package org.core.backend.ticketapp.passport.socketio.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.NotificationProcessorStatus;
import org.core.backend.ticketapp.common.enums.NotificationType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    UUID id;
    UUID createdBy;
    String requestedByName;
    String actionName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    Date dateCreated;
    UUID moduleId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    Date dateReceived;
    @Enumerated(EnumType.STRING)
    ApprovalStatus status;
    @Enumerated(EnumType.STRING)
    NotificationType notificationType;
    private String title;
    private String description;
    private NotificationProcessorStatus processorStatus;
    private String processorRemark;
}
