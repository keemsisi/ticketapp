package org.core.backend.ticketapp.transaction.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.core.backend.ticketapp.common.enums.PricingPlanType;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.entity.User;
import org.core.backend.ticketapp.passport.service.core.CoreUserService;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyResponseDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.repository.TransactionRepository;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.core.backend.ticketapp.common.util.Constants.*;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CoreUserService userService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paystack.secret.key}")
    private String secretKey;

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    public String initializePayment(TransactionInitializeRequestDTO initializePaymentDto) {
        ResponseEntity<String> response = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + secretKey);
            headers.set("Content-Type", "application/json");

            String requestJson = String.format("{\"email\":\"%s\",\"amount\":%d}", initializePaymentDto.getEmail(), initializePaymentDto.getAmount());
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            response = restTemplate.exchange(PAYSTACK_INITIALIZE_PAY, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode().isError()) {
                throw new Exception("Unable to initialize payment at the moment");
            }
        } catch(Throwable e) {
            e.printStackTrace();
        }

        assert response != null;
        return response.getBody();
    }

    @Override
    @Transactional
    public boolean verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception {
        ResponseEntity<String> response = null;
        Transaction transaction = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + secretKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);
            response = restTemplate.exchange(PAYSTACK_VERIFY + verifyRequestDTO.getReference(), HttpMethod.GET, entity, String.class);

            if (response.getStatusCode().isError()) {
                throw new Exception("Unable to verify payment at the moment");
            } else {
                User user = userService.getUserById(verifyRequestDTO.getUserId()).orElseThrow(() -> new ApplicationException(404, "not_found", "User not found!"));
                PricingPlanType pricingPlanType = PricingPlanType.valueOf(String.valueOf(verifyRequestDTO.getPricingPlan()).toUpperCase());

                transaction = Transaction.builder()
                        .id(UUID.randomUUID())
                        .userId(user)
                        .reference(verifyRequestDTO.getReference())
                        .amount(verifyRequestDTO.getAmount())
                        .orderId(verifyRequestDTO.getOrderId())
                        .gatewayResponse(verifyRequestDTO.getGatewayResponse())
                        .paidAt(verifyRequestDTO.getPaidAt())
                        .createdAt(verifyRequestDTO.getPaidAt())
                        .channel(verifyRequestDTO.getChannel())
                        .currency(verifyRequestDTO.getCurrency())
                        .ipAddress(verifyRequestDTO.getIpAddress())
                        .pricingPlan(pricingPlanType)
                        .paymentDate(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not verify payment. Please try again:\n" + e.getMessage());
        }

        transactionRepository.save(transaction);
        return response.getBody().contains("\"status\":\"success\"");
    }
}
