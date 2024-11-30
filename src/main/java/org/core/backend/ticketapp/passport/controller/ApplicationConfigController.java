package org.core.backend.ticketapp.passport.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.dto.GenericResponse;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.passport.entity.ApplicationConfig;
import org.core.backend.ticketapp.passport.service.core.apconfig.ApplicationConfigService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/app-configs")
public class ApplicationConfigController {
    private final JwtTokenUtil jwtTokenUtil;
    private final ApplicationConfigService applicationConfigService;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAll(final Pageable pageable) throws Exception {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.name());
        return new ResponseEntity<>(new GenericResponse<>("00", "Resource fetched successfully",
                applicationConfigService.getAll(pageable)), HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@Validated @RequestBody ApplicationConfig request) throws Exception {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.name());
        return new ResponseEntity<>(new GenericResponse<>("00", "Resource created successfully",
                applicationConfigService.create(request)), HttpStatus.CREATED);
    }
}
