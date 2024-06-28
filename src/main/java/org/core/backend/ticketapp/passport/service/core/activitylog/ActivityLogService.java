package org.core.backend.ticketapp.passport.service.core.activitylog;


import org.core.backend.ticketapp.passport.entity.ActivityLog;
import org.core.backend.ticketapp.passport.repository.ActivityLogRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogService implements IActivityLog {
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void save(ActivityLog activityLog) {
        activityLogRepository.save(activityLog);
    }

    @Override
    public Page<ActivityLog> getUserActivitiesPaged(Pageable pageable) {
        return activityLogRepository.getUserActivities(jwtTokenUtil.getUser().getUserId(), pageable);
    }

    @Override
    public List<ActivityLog> getUserActivities() {
        return activityLogRepository.getUserActivities(jwtTokenUtil.getUser().getUserId());
    }
}
