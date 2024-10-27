package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.common.enums.UserType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dao.EventDao;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.repository.OrderRepository;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.core.UserDto;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.PasswordUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.QrCodeCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketUpdateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.QrCode;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.core.backend.ticketapp.ticket.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final CoreUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final EventSeatSectionRepository eventSeatSectionRepository;
    private final OrderRepository orderRepository;
    private final QrCodeService qrCodeService;

    private EventDao eventDao;


    //call this create when the payment is successful
    @Override
    @Transactional
    public Ticket create(final TicketCreateRequestDTO ticketRequestDTO, final Order order) {
        if (Objects.isNull(order.getEventId())) {
            throw new ApplicationException(400, "not_allowed", "Can't create ticket for an order without eventId");
        }
        final var event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ApplicationException(400, "not_found", "Event does not exists!"));
        final var eventTicketStats = eventDao.getEventTicketStats(order.getSeatSectionId());
        if (eventTicketStats.getTotalAvailableTickets() > 0) {
            EventSeatSection seatSection = null;

            if (Objects.nonNull(ticketRequestDTO.getSeatSectionId())) {
                seatSection = eventSeatSectionRepository.findById(ticketRequestDTO.getSeatSectionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Event seat section does not exist",
                                ticketRequestDTO.getSeatSectionId().toString()));
                if (seatSection.getCapacity() == 0)
                    throw new IllegalStateException("No available ticket for seat section");
            }

            try {
                var ticket = new Ticket();
                ticket.setSeatSectionId(ticketRequestDTO.getSeatSectionId());
                ticket.setEventId(ticketRequestDTO.getEventId());
                ticket.setPrice(Objects.requireNonNull(seatSection).getPrice());
                ticket.setStatus(Status.ACTIVE);

                if (!jwtTokenUtil.isLoggedIn()) {
                    final var orderExists = orderRepository.existsById(order.getId());
                    if (orderExists) {
                        final var gender = ticketRequestDTO.getGender();
                        final var userDto = new UserDto();
                        userDto.setEmail(ticketRequestDTO.getEmail());
                        userDto.setFirstName(ticketRequestDTO.getFirstName());
                        userDto.setLastName(ticketRequestDTO.getLastName());
                        userDto.setPhone(ticketRequestDTO.getPhoneNumber());
                        userDto.setGender(Objects.isNull(gender) ? null : gender.toString());
                        userDto.setUserType(UserType.BUYER);
                        userDto.setAccountType(AccountType.INDIVIDUAL);
                        userDto.setPassword(PasswordUtil.generatePassword());
                        final var user = userService.createUser(userDto, new LoggedInUserDto());
                        user.setFirstTimeLogin(true);
                        user.setPasswordExpiryDate(LocalDateTime.now().plusMinutes(1));
                        user.setModifiedOn(new Date());
                        user.setModifiedBy(user.getId());
                        userService.save(user);
                        ticket.setUserId(user.getId());
                    }
                    throw new ApplicationException(400, "not_found", "Order does not exists!");
                } else {
                    final var loggedInUserId = jwtTokenUtil.getUser().getUserId();
                    ticket.setUserId(loggedInUserId);
                }
                ticket.setTenantId(event.getTenantId());
                ticket.setDateCreated(LocalDateTime.now());
                ticket.setId(UUID.randomUUID());
                ticket = ticketRepository.save(ticket);
                final var qrCode = createTicketQrCodeAndSendEmail(ticket);
                log.info(">>> Successfully created ticketId: {} qrCodeId: {} and orderId: {}  processed",
                        ticket.getId(), qrCode.getId(), order.getId());
                ticket.setQrCode(qrCode);
                return ticket;
            } catch (Exception e) {
                event.setTicketsAvailable(event.getTicketsAvailable() + 1);
                log.error(">>> Exception occurred while creating ticket ", e);
                throw new ApplicationException(500, "server_error", e.getMessage());
            }
        }
        throw new ApplicationException(403, "forbidden", "Oops! No tickets are available for this event again!");
    }

    @Override
    public Ticket getById(UUID id) {
        final var tenantId = jwtTokenUtil.getUser().getTenantId();
        return ticketRepository.getById(id, tenantId)
                .orElseThrow(() -> new ApplicationException(400, "not_found", "Ticket not found!"));
    }

    @Override
    public Ticket update(final TicketUpdateRequestDTO ticketDTO) {
        final var tenantId = jwtTokenUtil.getUser().getTenantId();
        final var ticket = ticketRepository.getById(ticketDTO.ticketId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", ticketDTO.ticketId().toString()));
        eventRepository.findById(ticketDTO.eventId(), tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id", ticketDTO.eventId().toString()));
        ticket.setSeatSectionId(ticketDTO.seatSectionId());
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(final UUID id) {
        final var tenantId = jwtTokenUtil.getUser().getTenantId();
        final var ticket = ticketRepository.getById(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id", id.toString()));
        ticket.setDeleted(true);
        ticketRepository.save(ticket);
    }

    @Override
    public Page<Ticket> getAll(final FilterTicketRequestDTO requestDTO, final Pageable pageRequest) {
        final var loggedInUser = jwtTokenUtil.getUser();
        var tenantId = jwtTokenUtil.getUser().getTenantId();
        if (requestDTO.tenantId() != null && UserUtils.userHasRole(loggedInUser.getRoles(), AccountType.SUPER_ADMIN.getType())) {
            tenantId = requestDTO.tenantId();
        }
        if (requestDTO.eventId() != null) {
            return ticketRepository.findByEventIdAndTenantId(requestDTO.eventId(), tenantId, pageRequest);
        } else {
            return ticketRepository.findByTenantId(tenantId, pageRequest);
        }
    }

    @Transactional
    public QrCode createTicketQrCodeAndSendEmail(final Ticket ticket) {
        final var qrCodeRequest = new QrCodeCreateRequestDTO(ticket.getId());
        CompletableFuture.runAsync(() -> {
            log.info(">>> Send Email to customer after successful qrcode generation with ticket {} ", ticket);
        });
        return qrCodeService.create(qrCodeRequest);
    }
}
