package org.core.backend.ticketapp.ticket.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.GenericResponse;
import org.core.backend.ticketapp.common.PagedMapperUtil;
import org.core.backend.ticketapp.common.PagedResponse;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.ScannedQrCodeResponse;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.core.backend.ticketapp.ticket.service.QrCodeService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = "/api/v1/qrcode")
@RestController
@AllArgsConstructor
public class QrCodeController implements ICrudController {
    private final QrCodeService qrCodeService;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<QrCode>> create(final @RequestBody QrCodeCreateRequestDTO request) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var data = qrCodeService.create(request);
        return ResponseEntity.ok(new GenericResponse<>("00", "QrCode created successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<QrCode>> getById(final @PathVariable UUID id) {
        final var data = qrCodeService.getById(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<?>> delete(final @PathVariable UUID id) {
        qrCodeService.delete(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "Deleted successfully", null));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<PagedResponse<?>>> getAll(final FilterTicketRequestDTO requestDTO, final Pageable pageable) {
        final var records = PagedMapperUtil.map(qrCodeService.getAll(requestDTO, pageable));
        return ResponseEntity.ok(new GenericResponse<>("00", "Successfully fetched records", records));
    }

    @RequestMapping(value = "/scan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<ScannedQrCodeResponse>> scanQrCode(@PathVariable UUID id) {
        final var record = qrCodeService.scanQr(id);
        return ResponseEntity.ok(new GenericResponse<>("00", "QrCode Scanned successfully!", record));
    }
}
