package org.core.backend.ticketapp.plan.service;

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
import org.core.backend.ticketapp.plan.dto.PlanCreateRequestDTO;
import org.core.backend.ticketapp.plan.dto.PlanCreateResponseDTO;
import org.core.backend.ticketapp.plan.dto.PlanDTO;
import org.core.backend.ticketapp.plan.entity.Plan;
import org.core.backend.ticketapp.plan.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.core.backend.ticketapp.common.util.Constants.PAYSTACK_INIT;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    PlanRepository planRepository;

    @Value("${paystack.secret.key}")
    private String secretKey;

    @Override
    public List<Plan> getAllPlans() {
        List<Plan> allPlans = planRepository.findAll();
        return new ArrayList<>(allPlans);
    }

    @Override
    public Plan createPlan(PlanCreateRequestDTO createPlanDto) throws Exception {
        PlanCreateResponseDTO planResponse = null;

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
            planResponse = mapper.readValue(result.toString(), PlanCreateResponseDTO.class);
        } catch(Throwable ex) {
            ex.printStackTrace();
        }

        if (planResponse != null) {
            Plan newPlan = new Plan();
            newPlan.setPlanId(planResponse.data().id());
            newPlan.setName(planResponse.data().name());
            newPlan.setPlanCode(planResponse.data().planCode());
            newPlan.setInterval(planResponse.data().interval());
            newPlan.setAmount((new BigDecimal(planResponse.data().amount())));
            return planRepository.save(newPlan);
        }

        return null;
    }

    @Override
    public PlanDTO fetchPlans() throws Exception {
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

}
