package org.core.backend.ticketapp.passport.config;


import org.core.backend.ticketapp.passport.util.SharedEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class BeanConfig {

    SharedEnvironment sharedEnvironment;
    @Autowired
    private Environment env;
}
