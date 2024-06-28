package org.core.backend.ticketapp.passport.service.core.workflow;


import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.passport.dtos.notification.SingleRequestApprovalDto;
import org.core.backend.ticketapp.passport.entity.Notification;
import org.core.backend.ticketapp.passport.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.Message;
import java.util.List;
import java.util.UUID;

public interface INotification {
    Page<Notification> getWorkflowNotificationWithStatus(ApprovalStatus status, UUID uuid, Pageable pageable);

    void approveRequest(SingleRequestApprovalDto singleRequestApprovalDto) throws Exception;

    List<Notification> getWorkflowNotificationWithStatus(ApprovalStatus status, UUID moduleId);

    void sendNotificationToClients(List<User> userUUuids, Message message);

    void pushNotificationToPermittedClients(Notification notification);

}
