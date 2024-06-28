package org.core.backend.ticketapp.passport.service.core.activitylog;

import org.core.backend.ticketapp.passport.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IActivityLog {

    void save(ActivityLog activityLog);

    Page<ActivityLog> getUserActivitiesPaged(Pageable pageable);

    List<ActivityLog> getUserActivities();
}
