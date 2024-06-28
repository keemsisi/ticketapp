package org.core.backend.ticketapp.passport.service.core;


import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.group.CreateGroupDTO;
import org.core.backend.ticketapp.passport.entity.Group;
import org.core.backend.ticketapp.passport.repository.GroupActionRepository;
import org.core.backend.ticketapp.passport.repository.GroupRepository;
import org.core.backend.ticketapp.passport.repository.GroupUserRepository;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService extends BaseRepoService<Group> {

    @Autowired
    private GroupRepository repository;

    @Autowired
    private GroupActionRepository groupActionRepository;

    @Autowired
    private GroupUserRepository groupUserRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public GroupService(GroupRepository repository) {
        super(repository);
    }

    public Optional<Group> getByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Group> getByUUID(UUID id) {
        return repository.findByUUID(id);
    }

    public Group createGroup(CreateGroupDTO createGroupDTO, LoggedInUserDto user) {
        Group group = new Group();
        group.setId(UUID.randomUUID());
        group.setName(createGroupDTO.getName());
        group.setNormalizedName(StringUtil.normalizeString(createGroupDTO.getName()));
        group.setDescription(createGroupDTO.getDescription());
        group.setCode(StringUtil.normalizeWithUnderscore(createGroupDTO.getName()));
        group.setCreatedOn(DateTime.now().toDate());
        group.setCreatedBy(user.getUserId());
        Group response = repository.saveAndFlush(group);
        return response;
    }

    public Optional<Group> findById(UUID groupId) {
        Optional<Group> group = repository.findById(groupId);

        return group;
    }

    public void deleteByUUID(UUID groupId) {
        groupUserRepository.deleteByGroupId(groupId);
        groupActionRepository.deleteByGroupId(groupId);
        repository.deleteByUUID(groupId);
    }

    public Page<Group> getAll(String name, Pageable pageable) throws ParseException {

        Page<Group> page = repository.getAll(name, pageable);

        List<Group> modifiedList = new ArrayList<>(page.getContent());

        modifiedList.forEach(this::resolveFields);

        return new PageImpl<>(modifiedList, pageable, (page.getTotalElements() - (page.getTotalElements() - modifiedList.size())));
    }

    public Optional<Group> getById(UUID id) {

        return repository.findById(id);
    }


}
