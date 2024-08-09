package org.core.backend.ticketapp.ticket.service;

import lombok.AllArgsConstructor;
import org.core.backend.ticketapp.common.enums.AccountType;
import org.core.backend.ticketapp.common.enums.UserType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.common.exceptions.ResourceNotFoundException;
import org.core.backend.ticketapp.event.dao.EventDao;
import org.core.backend.ticketapp.event.entity.EventSeatSection;
import org.core.backend.ticketapp.event.repository.EventRepository;
import org.core.backend.ticketapp.event.repository.EventSeatSectionRepository;
import org.core.backend.ticketapp.passport.dtos.core.LoggedInUserDto;
import org.core.backend.ticketapp.passport.dtos.core.UserDto;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.passport.util.PasswordUtil;
import org.core.backend.ticketapp.passport.util.UserUtils;
import org.core.backend.ticketapp.ticket.dto.FilterTicketRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.dto.TicketUpdateRequestDTO;
import org.core.backend.ticketapp.ticket.entity.Ticket;
import org.core.backend.ticketapp.ticket.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final CoreUserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final EventSeatSectionRepository eventSeatSectionRepository;
    private EventDao eventDao;

    @Override
    public Ticket create(final TicketCreateRequestDTO ticketRequestDTO) {
        final var event = eventRepository.findById(ticketRequestDTO.getEventId())
                .orElseThrow(() -> new ApplicationException(400, "not_found", "Event does not exists!"));
        final var eventStats = eventDao.getEventsStats(event.getId(), event.getTenantId());
        if (eventStats.getTotalAvailableTickets() > 0) {
            EventSeatSection seatSection = null;

            if (!ticketRequestDTO.getSeatSectionId().toString().isEmpty()) {
                seatSection = eventSeatSectionRepository.findById(ticketRequestDTO.getSeatSectionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Event seat section does not exist",
                                ticketRequestDTO.getSeatSectionId().toString()));
                if (seatSection.getCapacity() == 0)
                    throw new IllegalStateException("No available ticket for seat section");
            }

            try {
                final var ticket = new Ticket();
                ticket.setSeatSectionId(ticketRequestDTO.getSeatSectionId());
                ticket.setEventId(ticketRequestDTO.getEventId());
                assert seatSection != null;
                ticket.setPrice(seatSection.getPrice());

                if (Objects.isNull(jwtTokenUtil.getUser().getUserId())) {
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
                } else {
                    final var loggedInUserId = jwtTokenUtil.getUser().getUserId();
                    ticket.setUserId(loggedInUserId);
                }
                ticket.setTenantId(event.getTenantId());
                ticket.setDateCreated(LocalDateTime.now());
                return ticketRepository.save(ticket);
            } catch (Exception e) {
                event.setTicketsAvailable(event.getTicketsAvailable() + 1);
                throw new ApplicationException(500, "server_error", e.getMessage());
            }
        }
        throw new ApplicationException(400, "bad_request", "Ticket not available for event");
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
    public Page<Ticket> getAll(final FilterTicketRequestDTO requestDTO, final PageRequest pageRequest) {
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
}
