package org.core.backend.ticketapp.passport.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.core.backend.ticketapp.common.enums.ApprovalStatus;
import org.core.backend.ticketapp.common.enums.NotificationType;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestParam {
    @NotNull
    private UUID moduleId;
    private UUID notificationId;
    private UUID clientCompanyId;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDateApproved;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDateApproved;
    private ApprovalStatus status;
    private UUID requestedById;
    private boolean forUserId;
    private boolean byUserId;
    private String actionName;
    private NotificationType type;
    private Long index;
    private Integer size = 20;
    private Sort.Direction order = Sort.Direction.DESC;
    private String[] sortBy;
    private Integer page;
    private boolean paged;
    private String accountNumber;
    private String search;
    private UUID notificationForUserId; //only super admin should be allowed
    private UUID notificationByUserId;

    @AllArgsConstructor
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PagedParam {
        private Long index;
        private Integer size = 20;
        private Sort.Direction order = Sort.Direction.DESC;
        private String[] sortBy;
        private Integer page;
    }
}
