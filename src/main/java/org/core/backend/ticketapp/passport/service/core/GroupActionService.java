package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.entity.GroupActions;
import org.core.backend.ticketapp.passport.repository.GroupActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class GroupActionService extends BaseRepoService<GroupActions> {

    @Autowired
    private GroupActionRepository repository;

    @Autowired
    public GroupActionService(GroupActionRepository repository) {
        super(repository);
    }

    public boolean create(List<UUID> actionIds, UUID groupId, LoggedInUserDto user) {

        List<GroupActions> groupActions = new ArrayList<>();

        for (UUID actionId : actionIds) {
            GroupActions action = new GroupActions();
            action.setId(UUID.randomUUID());
            action.setGroupId(groupId);
            action.setActionId(actionId);
            action.setCreatedOn(new Date());
            action.setCreatedBy(user.getUserId());
            groupActions.add(action);
        }
        repository.saveAll(groupActions);
        return true;
    }

    public int deleteByGroupId(UUID groupId) {
        return repository.deleteByGroupId(groupId);
    }

    public int deleteByUserIdGroupId(UUID groupId, UUID userId) {
        return repository.deleteByUserIdGroupId(groupId, userId);
    }

}
