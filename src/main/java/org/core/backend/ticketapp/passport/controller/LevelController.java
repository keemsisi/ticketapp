package org.core.backend.ticketapp.passport.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.FatalObjCopierException;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.passport.entity.Levels;
import org.core.backend.ticketapp.passport.service.core.LevelService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/levels")
public class LevelController {

    @Autowired
    LevelService levelService;

    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, Pageable pageable,
                                    @RequestParam(required = false, defaultValue = "") String name,
                                    @RequestParam(required = false, defaultValue = "0") UUID unitId) {
        Page<Levels> roles = levelService.getAll(name, unitId, pageable);
        return new ResponseEntity<>(new GenericResponse<>("00", "", roles), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @PathVariable(value = "id") UUID userId) {
        Optional<Levels> roleResult = levelService.getById(userId);
        if (!roleResult.isPresent()) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "No level with the specified id found", ""),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GenericResponse<>("00", "", roleResult.get()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @Valid @RequestBody Levels level) throws JsonProcessingException {
        level.setCode(StringUtil.normalizeWithUnderscore(level.getName()));
        level = levelService.save(level);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Levels.class.getTypeName(), null, objectMapper.writeValueAsString(level), "Initiated a request to create a new level");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Successfully added the new level",
                        level),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @PathVariable(value = "id") UUID Id, @Valid @RequestBody Levels level) throws FatalObjCopierException, JsonProcessingException {
        Levels oldLevel = null;
        Optional<Levels> levelResult = levelService.getByUUID(Id);
        if (!levelResult.isPresent()) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "The level does not exist", ""),
                    HttpStatus.NOT_FOUND);
        }

        Optional<Levels> old = levelService.getByUnitIdAndName(level.getUnitId(), level.getName().toLowerCase());
        if (old.isPresent() && old.get().getId() != Id) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "A level with same name already exist", ""),
                    HttpStatus.CONFLICT);
        }
        level.setCode(StringUtil.normalizeWithUnderscore(level.getName()));
        oldLevel = levelResult.get();
        level = levelService.update(oldLevel, level);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Levels.class.getTypeName(), objectMapper.writeValueAsString(oldLevel), objectMapper.writeValueAsString(level), "Initiated a request to update a level");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Successfully updated the level", level),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @PathVariable(value = "id") UUID Id) throws JsonProcessingException {
        Levels oldLevel = null;
        boolean isDeleted = false;
        Optional<Levels> levelResult = levelService.getById(Id);
        if (!levelResult.isPresent()) {
            return new ResponseEntity<>(
                    new GenericResponse<>("01", "No level with the specified id found", ""),
                    HttpStatus.NOT_FOUND);
        }
        oldLevel = levelResult.get();
        levelService.delete(Id);
        isDeleted = true;
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Levels.class.getTypeName(), objectMapper.writeValueAsString(oldLevel), String.format("{\"levelId\": %s,\"isDeleted\": %s}", Id, isDeleted), "Initiated a request to delete a level");
        return new ResponseEntity<>(
                new GenericResponse<>("00", "Successfully deleted the level", levelResult.get()),
                HttpStatus.OK);

    }
}
