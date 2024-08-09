package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.ScannedQrCodeResponse;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.core.backend.ticketapp.ticket.repository.QrCodeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    private final QrCodeRepository qrCodeRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final TicketService ticketService;
    private final AppConfigs appConfigs;

    @Override
    public QrCode create(final QrCodeCreateRequestDTO requestDTO) {
        final var ticket = ticketService.getById(requestDTO.ticketId());
        final var id = UUID.randomUUID();
        final var qrcode = QrCode.builder()
                .id(id).ticketId(requestDTO.ticketId())
                .eventId(ticket.getEventId()).build();
        qrcode.setCode(id.toString().replace("-", "").toUpperCase());
        qrcode.setUserId(ticket.getUserId());
        qrcode.setTenantId(ticket.getTenantId());
        qrCodeRepository.save(qrcode);
        qrcode.setLink(String.format(appConfigs.baseUrl, qrcode.getCode()));
        return qrcode;

    }

    @Override
    public QrCode getById(UUID id) {
        final var tenantId = jwtTokenUtil.getUser().getTenantId();
        return qrCodeRepository.getById(id, tenantId)
                .orElseThrow(() -> new ApplicationException(400, "not_found", "QrCode not found!"));
    }

    @Override
    public void delete(final UUID id) {
        final var tenantId = jwtTokenUtil.getUser().getTenantId();
        final var qrCode = qrCodeRepository.getById(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("QrCode not found with id", id.toString()));
        qrCode.setDeleted(true);
        qrCodeRepository.save(qrCode);
    }

    @Override
    public Page<QrCode> getAll(final FilterTicketRequestDTO requestDTO, final Pageable pageRequest) {
        final var loggedInUser = jwtTokenUtil.getUser();
        var tenantId = jwtTokenUtil.getUser().getTenantId();
        if (requestDTO.tenantId() != null && UserUtils.userHasRole(loggedInUser.getRoles(), AccountType.SUPER_ADMIN.getType())) {
            tenantId = requestDTO.tenantId();
        }
        if (requestDTO.eventId() != null) {
            return qrCodeRepository.findByEventIdAndTenantId(requestDTO.eventId(), tenantId, pageRequest);
        } else {
            return qrCodeRepository.findByTenantId(tenantId, pageRequest);
        }
    }

    @Override
    public ScannedQrCodeResponse scanQr(UUID id) {
        return null;
    }
}
