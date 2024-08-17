package org.core.backend.ticketapp.plan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.core.backend.ticketapp.common.exceptions.ApplicationException;
import org.core.backend.ticketapp.passport.service.core.AppConfigs;
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.plan.entity.Plan;
import org.core.backend.ticketapp.plan.repository.PlanRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Objects;

import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_PLAN_BASE_URL;

@Slf4j
@Service
@AllArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;
    private final ObjectMapper mapper;
    private final AppConfigs appConfigs;
    private final RestTemplate restTemplate;

    private static @NotNull Plan getPlan(ResponseEntity<PlanCreateResponseDTO> response) {
        if (response.getStatusCode().isError()) {
            throw new ApplicationException(400, "req_failed", "Failed to init payment");
        }
        final var planResponse = Objects.requireNonNull(response.getBody());
        final var newPlan = new Plan();
        newPlan.setPlanId(planResponse.data().id());
        newPlan.setName(planResponse.data().name());
        newPlan.setPlanCode(planResponse.data().planCode());
        newPlan.setInterval(planResponse.data().interval());
        newPlan.setAmount((new BigDecimal(planResponse.data().amount())));
        return newPlan;
    }

    @Override
    public Page<Plan> getAllPlans(final Pageable pageable) {
        return planRepository.findAll(pageable);
    }

    @Override
    public Plan createPlan(final PlanCreateRequestDTO createPlanDto) throws Exception {
        try {
            System.out.println(mapper.writeValueAsString(createPlanDto));
            final var headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + appConfigs.secretKey);
            headers.set("Content-Type", "application/json");
            final var entity = new HttpEntity<>(createPlanDto, headers);
            final var response = restTemplate.exchange(PAYSTACK_PLAN_BASE_URL, HttpMethod.POST, entity, PlanCreateResponseDTO.class);
            final var newPlan = getPlan(response);
            return planRepository.save(newPlan);
        } catch (Exception ex) {
            log.error(">>> An Exception occurred : ", ex);
        }
        throw new ApplicationException(400, "req_failed", "Failed to create plan");
    }

    @Override
    public PlanDTO fetchPlans() throws Exception {
        PlanDTO allPlans = null;
        try {
            final var client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(PAYSTACK_PLAN_BASE_URL);
            httpGet.setHeader("Authorization", "Bearer " + appConfigs.secretKey);
            StringBuilder result = new StringBuilder();
            HttpResponse response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                ObjectMapper mapper = new ObjectMapper();
                allPlans = mapper.readValue(result.toString(), PlanDTO.class);
            }
            throw new ApplicationException(400, "req_failed", "Could not fetch plans");
        } catch (final Exception e) {
            log.error(">>> Exception Occurred while processing fetch plans ", e);
        }
        return allPlans;
    }

}
