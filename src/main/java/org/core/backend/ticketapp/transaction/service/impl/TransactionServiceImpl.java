package org.core.backend.ticketapp.transaction.service.impl;

import lombok.AllArgsConstructor;
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
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionInitializeResponseDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyRequestDTO;
import org.core.backend.ticketapp.transaction.dto.TransactionVerifyResponseDTO;
import org.core.backend.ticketapp.transaction.entity.Transaction;
import org.core.backend.ticketapp.transaction.repository.TransactionRepository;
import org.core.backend.ticketapp.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.core.backend.ticketapp.common.util.Constants.*;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final CoreUserService userService;

    @Value("${paystack.secret.key}")
    private String secretKey;

    @Override
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @Override
    public PlanDTO getAllPlans() throws Exception {
        PlanDTO allPlans = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(PAYSTACK_INIT);
            httpGet.setHeader("Authorization", "Bearer " + secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;

                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Could not fetch plans");
            }

            ObjectMapper mapper = new ObjectMapper();
            allPlans = mapper.readValue(result.toString(), PlanDTO.class);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return allPlans;
    }

    @Override
    public PlanCreateResponseDTO createPlan(PlanCreateRequestDTO createPlanDto) throws Exception {
        PlanCreateResponseDTO createPlanResponse = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(createPlanDto);

            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            entity.setContentType("application/json");

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(PAYSTACK_INIT);
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(httpPost);

            System.out.println(response.getStatusLine().getStatusCode() + "\n\n\n\n----0\n\n" + requestBody);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception ("Unable to process request at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            createPlanResponse = mapper.readValue(result.toString(), PlanCreateResponseDTO.class);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }
        return createPlanResponse;
    }

    @Override
    public TransactionInitializeResponseDTO initializePayment(TransactionInitializeRequestDTO initializePaymentDto) {
        TransactionInitializeResponseDTO initializePaymentResponse = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(initializePaymentDto);

            StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
            entity.setContentType("application/json");

            HttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(PAYSTACK_INITIALIZE_PAY);
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Unable to initialize payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            initializePaymentResponse = mapper.readValue(result.toString(), TransactionInitializeResponseDTO.class);
        } catch(Throwable e) {
            e.printStackTrace();
        }
        return initializePaymentResponse;
    }

    @Override
    @Transactional
    public TransactionVerifyResponseDTO verifyPayment(TransactionVerifyRequestDTO verifyRequestDTO) throws Exception {
        TransactionVerifyResponseDTO paymentVerificationResponse = null;
        Transaction transaction = null;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(PAYSTACK_VERIFY + verifyRequestDTO.getReference());
            request.addHeader("Content-type", "application/json");
            request.setHeader("Authorization", "Bearer " + secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;

                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
            } else {
                throw new Exception("Unable to verify payment at the moment");
            }

            ObjectMapper mapper = new ObjectMapper();
            paymentVerificationResponse = mapper.readValue(result.toString(), TransactionVerifyResponseDTO.class);

            if (!paymentVerificationResponse.getStatus()) {
                throw new Exception("Payment verification could not be completed");
            } else {

                User user = userService.getUserById(verifyRequestDTO.getUserId()).orElseThrow(() -> new ApplicationException(404, "not_found", "User not found!"));
                PricingPlanType pricingPlanType = PricingPlanType.valueOf(String.valueOf(verifyRequestDTO.getPricingPlan()).toUpperCase());

                transaction = Transaction.builder()
                        .id(UUID.randomUUID())
                        .userId(user)
                        .reference(paymentVerificationResponse.getData().getReference())
                        .amount(paymentVerificationResponse.getData().getAmount())
                        .orderId(verifyRequestDTO.getOrderId())
                        .gatewayResponse(paymentVerificationResponse.getData().getGatewayResponse())
                        .paidAt(paymentVerificationResponse.getData().getPaidAt())
                        .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                        .channel(paymentVerificationResponse.getData().getChannel())
                        .currency(paymentVerificationResponse.getData().getCurrency())
                        .ipAddress(paymentVerificationResponse.getData().getIpAddress())
                        .pricingPlan(pricingPlanType)
                        .createdAt(paymentVerificationResponse.getData().getCreatedAt())
                        .paymentDate(new Timestamp(System.currentTimeMillis()))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not verify payment. Please try again:\n" + e.getMessage());
        }

        transactionRepository.save(transaction);
        return paymentVerificationResponse;
    }
}
