package org.core.backend.ticketapp.passport.service.core;

import org.core.backend.ticketapp.passport.dao.ActionDao;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.core.UserActionDto;
import org.core.backend.ticketapp.passport.entity.Action;
import org.core.backend.ticketapp.passport.entity.UserAction;
import org.core.backend.ticketapp.passport.repository.ActionRepository;
import org.core.backend.ticketapp.passport.repository.UserActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ActionService extends BaseRepoService<Action> {

    @Autowired
    private ActionRepository repository;

    @Autowired
    private UserActionRepository userActionRepository;

    @Autowired
    private ActionDao actionDao;

    @Autowired
    public ActionService(ActionRepository repository) {
        super(repository);
    }

    public Object getAllActions(Pageable pageable) {
        if (pageable == null) {
            return repository.findAll();
        } else {
            return actionDao.getAll(pageable);
        }
    }

    public ArrayList<Action> getAll(UUID id) {
        return repository.findAllWithoutPaging(id);
    }

    public Optional<Action> getByName(String name) {
        return repository.findByName(name);
    }

    public Action save(Action action) {
        return super.save(action);
    }

    public void saveAllUserActions(List<UserActionDto> userActionDtos, LoggedInUserDto loggedInUserDto) {
        var userActions = userActionDtos.stream().map(x -> {
            var action = new UserAction();
            action.setActionId(x.getActionId());
            action.setUserId(x.getUserId());
            action.setId(UUID.randomUUID());
            action.setCreatedBy(loggedInUserDto.getUserId());
            action.setCreatedOn(new Date());
            return action;
        }).collect(Collectors.toList());
        userActionRepository.saveAll(userActions);
    }
}
