package org.core.backend.ticketapp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
@EnableFeignClients
public class TicketApp {

    public static void main(String[] args) {
        SpringApplication.run(TicketApp.class, args);
    }

    //prototype , singleton , request , session , custom, application, websocket
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setSkipNullEnabled(true).setPropertyCondition(Conditions.isNotNull())
//                .setMatchingStrategy(MatchingStrategies.STRICT);
//        return modelMapper;
//    }
}