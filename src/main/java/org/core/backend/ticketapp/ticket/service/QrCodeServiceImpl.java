package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.core.backend.ticketapp.common.enums.UserType;
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
import org.core.backend.ticketapp.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class QrCodeServiceImpl implements QrCodeService {
    private final QrCodeRepository qrCodeRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final TicketRepository ticketRepository;
    private final AppConfigs appConfigs;

    @Override
    public QrCode create(final QrCodeCreateRequestDTO requestDTO) {
        final var ticket = ticketRepository.findById(requestDTO.ticketId())
                .orElseThrow(() -> new ApplicationException(404, "not_found", "Ticket not found!"));
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
    public Page<QrCode> getAll(final FilterTicketRequestDTO requestDTO, final Pageable pageable) {
        final var userType = jwtTokenUtil.getUser().getUserType();
        final var userId = jwtTokenUtil.getUser().getUserId();
        final var eventId = requestDTO.eventId();
        var tenantId = jwtTokenUtil.getUser().getTenantId();
        if (requestDTO.tenantId() != null) {
            UserUtils.containsActionName("event_view_qr");
            tenantId = requestDTO.tenantId();
            return Objects.nonNull(requestDTO.eventId()) ?
                    qrCodeRepository.findByEventIdAndTenantId(eventId, tenantId, pageable) :
                    qrCodeRepository.findByTenantId(tenantId, pageable);
        } else if (Objects.nonNull(userType) && userType.equals(UserType.SELLER)) {
            return Objects.nonNull(requestDTO.userId()) ?
                    qrCodeRepository.findByTenantIdAndUserId(tenantId, requestDTO.userId(), pageable) :
                    qrCodeRepository.findByTenantId(tenantId, pageable);
        } else if (Objects.nonNull(userType) && userType.equals(UserType.BUYER)) {
            return Objects.nonNull(requestDTO.eventId()) ?
                    qrCodeRepository.findByEventIdAndTenantIdAndUserId(eventId, tenantId, userId, pageable) :
                    qrCodeRepository.findByTenantIdAndUserId(tenantId, userId, pageable);
        }
        return qrCodeRepository.findByUserId(ObjectUtils.defaultIfNull(requestDTO.userId(), userId), pageable);
    }

    @Override
    public ScannedQrCodeResponse scanQr(UUID id) {
        return null;
    }
}
