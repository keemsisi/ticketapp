package org.core.backend.ticketapp.transaction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.backend.ticketapp.common.enums.Gender;
import org.core.backend.ticketapp.common.enums.OrderStatus;
import org.core.backend.ticketapp.common.enums.Status;
import org.core.backend.ticketapp.common.enums.SubscriptionStatus;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.event.service.EventSeatSectionService;
import org.core.backend.ticketapp.event.service.EventService;
import org.core.backend.ticketapp.order.entity.Order;
import org.core.backend.ticketapp.order.service.OrderService;
import org.core.backend.ticketapp.passport.entity.Tenant;
import org.core.backend.ticketapp.passport.service.TenantService;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.passport.util.ActivityLogPublisherUtil;
import org.core.backend.ticketapp.passport.util.JwtTokenUtil;
import org.core.backend.ticketapp.plan.service.PlanService;
import org.core.backend.ticketapp.ticket.dto.TicketCreateRequestDTO;
import org.core.backend.ticketapp.ticket.service.TicketService;
import org.core.backend.ticketapp.transaction.dto.InitTransactionRequestDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentInitResponseDTO;
import org.core.backend.ticketapp.transaction.dto.PaymentVerificationResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.entity.PaymentGatewayMeta;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.repository.TransactionRepository;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_INITIALIZE_PAY;
import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_VERIFY;

@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private final TransactionRepository transactionRepository;
    private final OrderService orderService;
    private final RestTemplate restTemplate;
    private final JwtTokenUtil jwtTokenUtil;
    private final AppConfigs appConfigs;
    private final ObjectMapper objectMapper;
    private final EventService eventService;
    private final EventSeatSectionService eventSeatSectionService;
    private final TicketService ticketService;
    private final CoreUserService coreUserService;
    private final PlanService planService;
    private final TenantService tenantService;
    private final ActivityLogPublisherUtil activityLogPublisherUtil;

    @Override
    public Page<Transaction> getAll(final Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Order initializePayment(final InitTransactionRequestDTO request) {
        if (ObjectUtils.isNotEmpty(request.getPlan()) && ObjectUtils.isEmpty(request.getEventId())) {
            final var plan = planService.getByCode(request.getPlan());
            request.setAmount(plan.getAmount().doubleValue());
        } else {
            final var event = eventService.getById(request.getEventId());
            if (event.isFreeEvent()) {
                return processFreeEvent(request);
            } else {
                final var eventSeatSection = eventSeatSectionService.getById(request.getSeatSectionId());
                request.setAmount(eventSeatSection.getPrice());
            }
        }
        ResponseEntity<PaymentInitResponseDTO> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + appConfigs.payStackApiKey);
            headers.set("Content-Type", "application/json");
            final var entity = new HttpEntity<>(request, headers);
            response = restTemplate.exchange(PAYSTACK_INITIALIZE_PAY, HttpMethod.POST, entity, PaymentInitResponseDTO.class);
            if (response.getStatusCode().isError()) {
                throw new ApplicationException(400, "init_payment_failed", "Failed to init payment");
            }
            return getOrder(request, Objects.requireNonNull(response.getBody()));
        } catch (Throwable e) {
            log.error(">>> Error Occurred while initiating transaction", e);
        }
        throw new ApplicationException(400, "init_payment_failed", "Failed to init payment with checkout link!");
    }

    private Order getOrder(final InitTransactionRequestDTO initRequest, final PaymentInitResponseDTO response) {
        final var data = response.getData();
        final var order = new Order();
        order.setEventId(initRequest.getEventId());
        order.setQuantity(ObjectUtils.defaultIfNull(initRequest.getQuantity(), 1));
        order.setUserId(jwtTokenUtil.getUser().getUserId());
        order.setAmount(new BigDecimal(String.valueOf(initRequest.getAmount())));
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentLink(data.getAuthorizationUrl());
        order.setCode(data.getAccessCode());
        order.setTenantId(jwtTokenUtil.getUser().getTenantId());
        order.setReference(data.getReference());
        return orderService.save(order);
    }

    @Override
    @Transactional
    public Transaction verifyPayment(final TransactionVerifyRequestDTO verifyRequestDTO) {
        final var order = orderService.getById(verifyRequestDTO.getOrderId());
        try {
            final var headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + appConfigs.payStackApiKey);
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            final var response = restTemplate.exchange(String.format(PAYSTACK_VERIFY, order.getReference()),
                    HttpMethod.GET, entity, PaymentVerificationResponseDTO.class);
            if (response.getStatusCode().isError()) {
                log.error(">>> Payment verification was not successful for request {} ", verifyRequestDTO);
                final var errorCode = System.nanoTime();
                throw new ApplicationException(400, "req_failed",
                        String.format("Failed to verify payment, please try again later or contact support with code: %s", errorCode));
            } else {
                final var data = Objects.requireNonNull(Objects.requireNonNull(response.getBody()).getData());
                final var gatewayResponse = objectMapper.writeValueAsString(response.getBody());
                final var meta = PaymentGatewayMeta.builder()
                        .paidAt(data.getPaidAt())
                        .createdAt(data.getCreatedAt())
                        .channel(data.getChannel())
                        .currency(data.getCurrency())
                        .gateway("PayStack")
                        .gatewayResponse(gatewayResponse)
                        .build();
                final var transaction = createOrGetExistingTransaction(order, meta);
                if (!transaction.getStatus().isCompleted()) {
                    if (ObjectUtils.isNotEmpty(data.getPlan())) {
                        CompletableFuture.runAsync(() -> {
                            log.info(">>> Processing plan upgrade with user : {} code : {} and amount {} ",
                                    transaction.getUserId(), data.getPlan(), data.getAmount());
                            updateUserSubscriptionPlan(transaction, order, data);
                        });
                        return transaction;
                    }
                    processCreateTicketAndQrCode(transaction, order);
                }
                return transaction;
            }
        } catch (Exception e) {
            log.error(">>> Error Occurred while initiating transaction", e);
            final var errorCode = System.nanoTime();
            throw new ApplicationException(400, "req_failed", String.format("Could not verify payment, please try again later or contact support with code: %s", errorCode));
        }
    }

    @Transactional
    public void updateUserSubscriptionPlan(final Transaction transaction, final Order order, final PaymentVerificationResponseDTO.Data data) {
        try {
            if (transaction.getStatus().isPaid()) {
                final var user = coreUserService.getUserById(transaction.getUserId())
                        .orElseThrow(() -> new ApplicationException(400, "not_found", "User not found!"));
                final var tenant = tenantService.getByTenantId(user.getTenantId()).
                        orElseThrow(() -> new ApplicationException(400, "not_found", "Tenant not found!"));
                if (Objects.nonNull(tenant.getPlanId())) {
                    final var oldTenantData = objectMapper.writeValueAsString(tenant);
                    final var oldPlan = planService.getById(tenant.getPlanId());
                    final var newPlan = planService.getByCode(data.getPlan());
                    log.info(">>> Upgrading plan for tenant {} tenantId: {} oldPlan: {} newPlanId: {} ",
                            tenant.getName(), tenant.getId(), oldPlan.getPlanCode(), newPlan.getPlanCode());
                    tenant.setPlanId(newPlan.getId());
                    tenant.setDateModified(LocalDateTime.now());
                    tenant.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
                    tenantService.save(tenant);
                    final var newTenantData = objectMapper.writeValueAsString(tenant);
                    log.info(">>> Tenant Plan successfully upgraded {} tenantId: {} newPlanId: {} ",
                            tenant.getName(), tenant.getId(), data.getPlan());
                    activityLogPublisherUtil.saveActivityLog(order.getUserId(), Tenant.class.getTypeName(), oldTenantData, newTenantData,
                            "Processed payment for account and subscription plan updated!");
                    transaction.setStatus(Status.COMPLETED);
                    transaction.setDateModified(LocalDateTime.now());
                    transaction.setComment(String.format("Tenant subscription was processed and transaction marked as completed on %s", new Date()));
                    transactionRepository.save(transaction);
                    //process the new roles and permissions to be added to the user if the new plan is not the same as the old plan
                    //--> if the new plan is > than the old plan, all the roles in the new plan should be skipped and the
                    //tenant owner will need to add the new roles / group
                    //==================================================================================================
                    //-> if the new plan < old plan all the roles / permission and group should be stripped of the owner account and all the
                    // tenant users
                    //the owner of the tenant will have the new roles / groups and permissions
                    //the tenant users will have to get the similar roles that they have before
                    // meaning that the tenant users will only have roles that exists only in the new plan if they have it before
                    //Example:
                    /**
                     * tenant user has ->>> old plan roles -> [event_role,payment_role,finance_role,dashboard_role]
                     * new plan roles -> [event_role,payment_role]
                     * ie tenant user will now have ->> [event_role,payment_role] because it has it before
                     * if it does not have it before, then the new roles for the user will be empty!
                     */
                    //==========================================================================================================
                }
            }
        } catch (final Exception e) {
            log.error(">>> Failed to process tenant subscription update with data {}", data, e);
        }
        throw new ApplicationException(400, "exception_occurred", "Failed to process user subscription update!");
    }

    @Transactional
    public Transaction createOrGetExistingTransaction(final Order order, final PaymentGatewayMeta meta) {
        final var reference = order.getReference();
        return transactionRepository.findByReference(reference).or(() -> {
            final var trx = Transaction.builder()
                    .userId(order.getUserId())
                    .reference(order.getReference())
                    .amount(order.getAmount())
                    .orderId(order.getId())
                    .status(Objects.isNull(meta.getPaidAt()) ? Status.PENDING : Status.PAID)
                    .gateWayMeta(meta)
                    .build();
            trx.setTenantId(order.getTenantId());
            transactionRepository.save(trx);
            return Optional.of(trx);
        }).orElseThrow(() -> new ApplicationException(400, "error", "Error while creating transaction"));
    }

    @Transactional
    public Order processFreeEvent(final InitTransactionRequestDTO request) {
        final var user = jwtTokenUtil.getUser();
        final var order = new Order();
        order.setEventId(request.getEventId());
        order.setQuantity(ObjectUtils.defaultIfNull(request.getQuantity(), 1));
        order.setUserId(jwtTokenUtil.getUser().getUserId());
        order.setAmount(BigDecimal.ZERO);
        order.setStatus(OrderStatus.SUCCESSFUL);
        order.setSeatSectionId(request.getSeatSectionId());
        order.setTenantId(jwtTokenUtil.getUser().getTenantId());
        final var ticketDto = new TicketCreateRequestDTO(
                order.getEventId(), order.getSeatSectionId(), order.getUserId(),
                request.getFirstName(), request.getLastName(),
                request.getEmail(), request.getPhoneNumber(),
                Objects.isNull(user.getUserId()) ? Gender.OTHERS : user.getGender()
        );
        final var ticket = ticketService.create(ticketDto, order);
        order.setTicketId(ticket.getId());
        return orderService.save(order);
    }

    @Transactional
    public void processCreateTicketAndQrCode(final Transaction transaction, final Order order) throws JsonProcessingException {
        final var user = coreUserService.getUserById(order.getUserId())
                .orElseThrow(() -> new ApplicationException(404, "not_not",
                        "Oops! User account not found to complete payment ticket and order process"));
        if (Objects.isNull(order.getEventId())) {
            return; //payment like subscription and others
        }
        final var ticketDto = new TicketCreateRequestDTO(
                order.getEventId(), order.getSeatSectionId(), order.getUserId(),
                user.getFirstName(), user.getLastName(),
                user.getEmail(), user.getPhone(), Gender.valueOf(
                StringUtils.defaultIfBlank(user.getGender(), Gender.OTHERS.name()))
        );
        final var ticket = ticketService.create(ticketDto, order);
        order.setTicketId(ticket.getId());
        orderService.save(order);

        transaction.setStatus(Status.COMPLETED);
        transaction.setDateModified(LocalDateTime.now());
        transaction.setComment(String.format("User event payment was processed and transaction marked as completed on %s", new Date()));
        transactionRepository.save(transaction);
        activityLogPublisherUtil.saveActivityLog(order.getUserId(), Order.class.getTypeName(),
                null, objectMapper.writeValueAsString(order),
                String.format("Event payment of NGN%s was processed successfully...ticket and QR Code created!", formatAmount(order.getAmount().doubleValue())));
        log.info("processCreateTicketAndQrCode competed successfully with order {} ", order);
    }

    private String formatAmount(final double amount) {
        final var nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(amount);
    }
}
