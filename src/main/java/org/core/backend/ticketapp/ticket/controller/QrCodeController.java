package org.core.backend.ticketapp.ticket.controller;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.controller.ICrudController;
import org.core.backend.ticketapp.common.dto.GenericApiResponse;
import org.core.backend.ticketapp.common.dto.Page;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeStatsDTO;
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
    private final AppConfigs appConfigs;
    private final QrCodeService qrCodeService;
    private final JwtTokenUtil jwtTokenUtil;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<QrCode>> create(final @RequestBody QrCodeCreateRequestDTO request) {
        UserUtils.assertUserHasRole(jwtTokenUtil.getUser().getRoles(), AccountType.SUPER_ADMIN.getType());
        final var data = qrCodeService.create(request);
        data.setLink(String.format(appConfigs.qrCodeBaseUrl, data.getCode()));
        return ResponseEntity.ok(new GenericApiResponse<>("00", "QrCode created successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<QrCode>> getById(final @PathVariable UUID id) {
        final var data = qrCodeService.getById(id);
        data.setLink(String.format(appConfigs.qrCodeBaseUrl, data.getCode()));
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Fetched successfully", data));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<?>> delete(final @PathVariable UUID id) {
        qrCodeService.delete(id);
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Deleted successfully", null));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<Page<?>>> getAll(final FilterTicketRequestDTO requestDTO, final Pageable pageable) {
        final var records = qrCodeService.getAllV2(requestDTO, pageable);
        records.getContent().forEach(qrCode -> qrCode.setLink(String.format(appConfigs.qrCodeBaseUrl, qrCode.getCode())));
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Successfully fetched records", records));
    }

    @RequestMapping(value = "/scan/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<ScannedQrCodeResponse>> scanQrCode(@PathVariable String code) {
        final var record = qrCodeService.scanQr(code);
        return ResponseEntity.ok(new GenericApiResponse<>("00", "QrCode Scanned successfully!", record));
    }

    @RequestMapping(value = "/stats/{eventId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericApiResponse<QrCodeStatsDTO>> stats(@PathVariable UUID eventId) {
        final var user = jwtTokenUtil.getUser();
        if (!(user.getUserType().isSeller() || jwtTokenUtil.getUser().isAdmin())) {
            throw new ApplicationException(403, "forbidden", "Oops! Access not allowed");
        }
        final var record = qrCodeService.getStats(eventId);
        return ResponseEntity.ok(new GenericApiResponse<>("00", "Fetched successfully!", record));
    }
}
