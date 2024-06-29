package org.core.backend.ticketapp.passport.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.passport.dao.ModuleDao;
import org.core.backend.ticketapp.passport.dao.UserDao;
import org.core.backend.ticketapp.passport.dtos.core.ModuleDto;
import org.core.backend.ticketapp.passport.dtos.core.UserModuleDto;
import org.core.backend.ticketapp.passport.entity.Module;
import org.core.backend.ticketapp.passport.entity.UserModule;
import org.core.backend.ticketapp.passport.repository.ActionRepository;
import org.core.backend.ticketapp.passport.repository.ModuleRepository;
import org.core.backend.ticketapp.passport.repository.UserModuleRepository;
import org.core.backend.ticketapp.passport.repository.UserRoleRepository;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private ModuleDao moduleDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityLogProcessorUtils activityLogProcessorUtils;
    @Autowired
    private UserModuleRepository userModuleRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll() {
        List<Module> modules = moduleRepository.findAll();
        return new ResponseEntity<>(new GenericResponse<>("00", "", modules), HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable(value = "id") UUID moduleId) {
        Optional<Module> module = moduleRepository.findById(moduleId);
        if (!module.isPresent()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "No module with the specified id found", ""), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GenericResponse<>("00", "", module.get()), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody ModuleDto moduleDto) throws JsonProcessingException {
        Module newModuleCreated = null;
        var userId = UUID.fromString((String) jwtTokenUtil.getClaimByKey("user_id"));

        Module module = new Module();
        BeanUtils.copyProperties(moduleDto, module);
        module.setId(UUID.randomUUID());
        module.setName(module.getName());
        module.setCreatedBy(userId);
        module.setCreatedOn(new Date());
        module.setCode(StringUtil.normalizeWithUnderscore(moduleDto.getName()).toLowerCase());
        module.setNormalizedName(StringUtil.normalizeString(moduleDto.getName()));
        newModuleCreated = moduleRepository.saveAndFlush(module);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Module.class.getTypeName(), null, objectMapper.writeValueAsString(newModuleCreated), "Initiated a request to create a module");
        return new ResponseEntity<>(new GenericResponse<>("00", "Successfully added the new module", null), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable(value = "id") UUID moduleId, @Valid @RequestBody ModuleDto moduleDto) throws JsonProcessingException {
        Module _module = null;
        String oldDataJSON = null;
        var userId = UUID.fromString((String) jwtTokenUtil.getClaimByKey("user_id"));

        Optional<Module> module = moduleRepository.findById(moduleId);
        if (!module.isPresent()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "The module does not exist", ""), HttpStatus.NOT_FOUND);
        }
        _module = module.get();
        oldDataJSON = objectMapper.writeValueAsString(_module);
        _module.setName(moduleDto.getName());
        _module.setModifiedBy(userId);
        _module.setModifiedOn(new Date());
        _module.setName(moduleDto.getName());
        _module.setCode(StringUtil.normalizeWithUnderscore(moduleDto.getName()));
        _module.setNormalizedName(StringUtil.normalizeString(moduleDto.getName()));
        moduleRepository.saveAndFlush(_module);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Module.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(_module), "Initiated a request to update an existing module details");
        return new ResponseEntity<>(new GenericResponse<>("00", "Successfully updated the module", _module), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") UUID moduleId) throws JsonProcessingException {
        Module oldModule = null;
        boolean isDeleted = false;
        Optional<Module> module = moduleRepository.findById(moduleId);
        if (!module.isPresent()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "No module with the specified id found", ""), HttpStatus.NOT_FOUND);
        }
        oldModule = module.get();
        moduleRepository.deleteById(moduleId);
        isDeleted = true;
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Module.class.getTypeName(), objectMapper.writeValueAsString(oldModule), String.format("{\"moduleId\": %s,\"isDeleted\": %s}", moduleId, isDeleted), "Initiated a request to update an existing module details");
        return new ResponseEntity<>(new GenericResponse<>("00", "Successfully deleted the role", module.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> assignUsersToModule(@Validated @RequestBody UserModuleDto moduleDto) throws JsonProcessingException {
        UserModule module = null;
        var userId = UUID.fromString((String) jwtTokenUtil.getClaimByKey("user_id"));

        module = new UserModule();
        BeanUtils.copyProperties(moduleDto, module);
        module.setId(UUID.randomUUID());
        module.setCreatedBy(userId);
        module.setCreatedOn(new Date());
        module = userModuleRepository.saveAndFlush(module);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Module.class.getTypeName(), null, objectMapper.writeValueAsString(module), "Initiated a request to assign users to a module");

        return new ResponseEntity<>(new GenericResponse<>("00", "Successfully added the new module", null), HttpStatus.OK);
    }

    @RequestMapping(value = "{moduleId}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> GetUsersUnderGivenModule(@PathVariable(value = "moduleId") UUID moduleId) {
        var userModules = moduleDao.getUserModulesByModuleId(moduleId);
        return new ResponseEntity<>(new GenericResponse<>("00", "User modules retrieved successfully.", userModules), HttpStatus.OK);
    }

}
