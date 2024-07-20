package org.core.backend.ticketapp.passport.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.thecarisma.FatalObjCopierException;
import io.github.thecarisma.ObjCopier;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.util.ConstantUtil;
import org.core.backend.ticketapp.passport.dtos.core.TenantAccountSettingsDto;
import org.core.backend.ticketapp.passport.dtos.core.TenantDto;
import org.core.backend.ticketapp.passport.entity.Tenant;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.repository.SystemAlertRepository;
import org.core.backend.ticketapp.passport.service.TenantService;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.ActivityLogProcessorUtils;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/tenants")
@AllArgsConstructor
public class TenantController {
    private final TenantService service;
    private final CoreUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;
    private final ActivityLogProcessorUtils activityLogProcessorUtils;
    private final SystemAlertRepository systemAlertRepository;
    private final TenantService tenantService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTenant(@Validated @RequestBody TenantDto tenantDto) throws JsonProcessingException {
        final var loggedInUserDto = jwtTokenUtil.getUser();
        if (!loggedInUserDto.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""), HttpStatus.UNAUTHORIZED);
        }
        final var user = new User();
        BeanUtils.copyProperties(loggedInUserDto, user);
        user.setId(loggedInUserDto.getUserId());
        final var tenant = tenantService.create(tenantDto, user, tenantDto.getOwnerId());
        return new ResponseEntity<>(new GenericResponse<>("00", "", tenant), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTenants(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization) {
        var tenant = service.getAll(Pageable.unpaged());
        return new ResponseEntity<>(new GenericResponse<>("00", "", tenant), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@Valid @RequestBody TenantDto tenantDto) throws FatalObjCopierException, JsonProcessingException {
        Tenant newTenant;
        String oldDataJSON;
        var user = jwtTokenUtil.getUser();
        Assert.notNull(tenantDto.getId());
        Optional<Tenant> _tenant = service.getByTenantId(tenantDto.getId());

        if (_tenant.isEmpty()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "The tenant does not exist", ""), HttpStatus.NOT_FOUND);
        }

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""), HttpStatus.UNAUTHORIZED);
        }
        oldDataJSON = objectMapper.writeValueAsString(_tenant.get());
        var tenant = _tenant.get();
        ObjCopier.copyFields(tenant, tenantDto);
        newTenant = service.save(tenant);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Tenant.class.getTypeName(), objectMapper.writeValueAsString(oldDataJSON), objectMapper.writeValueAsString(newTenant), "Initiated a request to update a tenant");

        return new ResponseEntity<>(new GenericResponse<>("00", "Successfully updated tenant's details", tenant), HttpStatus.OK);

    }


    @RequestMapping(value = "/logo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLogo(@RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization, @RequestPart("logo") MultipartFile file) throws IOException, URISyntaxException {
        Tenant newTenant;
        String oldDataJSON;
        var user = jwtTokenUtil.getUser();
        Optional<Tenant> tenant = service.getByTenantId(user.getTenantId());

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""), HttpStatus.NOT_FOUND);
        }
        if (tenant.isEmpty())
            return new ResponseEntity<>(new GenericResponse<>("01", "Tenant with the given identity does not exists", tenant), HttpStatus.BAD_REQUEST);
        oldDataJSON = objectMapper.writeValueAsString(tenant.get());
        newTenant = service.updateLogo(tenant.get(), file);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Tenant.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(newTenant), "Initiated a request to update tenant logo");
        return new ResponseEntity<>(new GenericResponse<>("00", "", newTenant), HttpStatus.OK);
    }


    @RequestMapping(value = "/{tenantId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPolicySetting(@PathVariable("tenantId") UUID tenantId) {
        var user = jwtTokenUtil.getUser();

        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""), HttpStatus.NOT_FOUND);
        }
        var tenant = service.getByTenantId(tenantId);

        return new ResponseEntity<>(new GenericResponse<>("00", "", tenant.get()), HttpStatus.OK);
    }


    @RequestMapping(value = "/settings", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePolicySetting(@RequestBody TenantAccountSettingsDto settings, @RequestHeader(name = "Authorization", defaultValue = "Bearer ", required = true) String authorization) throws FatalObjCopierException, JsonProcessingException {
        Tenant newTenant;
        String oldDataJSON;
        var user = jwtTokenUtil.getUser();
        if (!user.getRoles().contains(ConstantUtil.SUPER_ADMIN)) {
            return new ResponseEntity<>(new GenericResponse<>("01", "Action denied because you are not a SUPER ADMIN.", ""), HttpStatus.FORBIDDEN);
        }

        var _tenant = service.getByTenantId(settings.getTenantId());
        if (!_tenant.isPresent()) {
            return new ResponseEntity<>(new GenericResponse<>("01", "No tenant exists with the provided details.", ""), HttpStatus.NOT_FOUND);
        }
        oldDataJSON = objectMapper.writeValueAsString(_tenant.get());
        var tenant = _tenant.get();
        BeanUtils.copyProperties(settings, tenant);
        tenant.setTwoFaEnabled(settings.getIsTwoFaEnabled());
        newTenant = service.save(tenant);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Tenant.class.getTypeName(), oldDataJSON, objectMapper.writeValueAsString(newTenant), "Initiated a request to update a tenant policy settings");
        return new ResponseEntity<>(new GenericResponse<>("00", "Settings updated successfully", null), HttpStatus.OK);
    }


    @RequestMapping(value = "/users/bulk", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadUsers(@RequestHeader("Authorization") String authorization, @Valid @RequestPart("file") MultipartFile file) throws IOException, ParseException {
        List<User> users;
        final var loggedInUserDto = jwtTokenUtil.getUser();
        UserUtils.assertUserHasRole(loggedInUserDto.getRoles(), ConstantUtil.SUPER_ADMIN);
        users = userService.createUserFromExcel(loggedInUserDto, file);
        activityLogProcessorUtils.processActivityLog(jwtTokenUtil.getUser().getUserId(), Tenant.class.getTypeName(), null, objectMapper.writeValueAsString(users), "Initiated a request to create new users from uploaded excel sheet");
        return new ResponseEntity<>(new GenericResponse<>("00", "Settings updated successfully", null), HttpStatus.CREATED);
    }
}