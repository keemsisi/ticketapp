package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.core.UserGroupDto;
import org.core.backend.ticketapp.passport.entity.GroupUsers;
import org.core.backend.ticketapp.passport.repository.GroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupUserService extends BaseRepoService<GroupUsers> {

    @Autowired
    private GroupUserRepository repository;

    @Autowired
    public GroupUserService(GroupUserRepository repository) {
        super(repository);
    }

    public boolean create(List<UUID> userIds, UUID groupId, LoggedInUserDto user) {

        List<GroupUsers> groupUsers = new ArrayList<>();
        for (UUID userId : userIds) {
            GroupUsers gu = new GroupUsers();
            gu.setId(UUID.randomUUID());
            gu.setGroupId(groupId);
            gu.setUserId(userId);
            gu.setCreatedOn(new Date());
            gu.setCreatedBy(user.getUserId());
            groupUsers.add(gu);
        }
        repository.saveAll(groupUsers);

        return true;
    }

    public boolean findUsers(UUID groupId) {
        long userExist = repository.countByGroupId(groupId);

        return userExist > 0;
    }

    public int deleteByGroupId(UUID groupId) {
        return repository.deleteByGroupId(groupId);
    }

    public void assignUsersToGroups(UserGroupDto userGroupDtos, LoggedInUserDto loggedInUser) {
        var userGroups = userGroupDtos.getUserIds().stream().map(userId -> {
            var groupUser = new GroupUsers();
            groupUser.setGroupId(userGroupDtos.getGroupId());
            groupUser.setUserId(userId);
            groupUser.setId(UUID.randomUUID());
            groupUser.setCreatedBy(loggedInUser.getUserId());
            groupUser.setCreatedOn(new Date());
            return groupUser;
        }).collect(Collectors.toList());
        repository.saveAllAndFlush(userGroups);
    }

    public void assignGroupsToUser(List<UUID> groupIds, UUID userId, LoggedInUserDto loggedInUser) {
        var userGroups = groupIds.stream().map(groupId -> {
            var groupUser = new GroupUsers();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(userId);
            groupUser.setId(UUID.randomUUID());
            groupUser.setCreatedBy(loggedInUser.getUserId());
            groupUser.setCreatedOn(new Date());
            return groupUser;
        }).collect(Collectors.toList());
        repository.saveAllAndFlush(userGroups);
    }
}
