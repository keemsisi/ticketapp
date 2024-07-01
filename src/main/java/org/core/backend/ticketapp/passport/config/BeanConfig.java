package org.core.backend.ticketapp.passport.config;


import org.core.backend.ticketapp.passport.util.SharedEnvironment;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class BeanConfig {

    private static final String CLASS_PATH = "classpath:db/migration";
    SharedEnvironment sharedEnvironment;
    @Autowired
    private Environment env;
    @Value(value = "${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value(value = "${spring.datasource.url}")
    private String databaseUrl;
    @Value(value = "${spring.datasource.username}")
    private String username;
    @Value(value = "${spring.datasource.password}")
    private String password;
    @Value(value = "${spring.flyway.enabled}")
    private boolean enabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(databaseUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public Flyway flyway() {
        final Flyway flyway = Flyway.configure().dataSource(dataSource()).locations(CLASS_PATH).baselineOnMigrate(true).load();
        if (enabled) {
            flyway.repair();
            flyway.migrate();
        }
        return flyway;
    }
}
