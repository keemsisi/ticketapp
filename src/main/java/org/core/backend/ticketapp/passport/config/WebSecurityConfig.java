package org.core.backend.ticketapp.passport.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] AUTH_WHITELIST = {
            "/actuator/**",
            "/v3/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger**/**",
            "/.well-known/jwks.json",
            "/api/v1/users/password/reset/email",
            "/api/v1/users/password/reset",
            "/api/v1/users/password/renew",
            "/api/v1/users/validate-2fa-auth",
            "/api/v1/users/onboard",
            "/api/v1/users/auth",
            "/api/v1/events/filter-search",
    };
    @Autowired
    private AuthenticationProviderConfig authenticationProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, AUTH_WHITELIST);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.addFilterBefore(new SimpleCORSFilter(), ChannelProcessingFilter.class).csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/events/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/plans").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/tenants/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/form-data/by-code/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/sponsored-offers").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/sponsored-offers/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/event-promotions").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/event-promotions/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/social-media-links/advertisements").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/social-media-links/advertisements/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobs/advertisements").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobs/advertisements/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobs/advertisements/application/*").permitAll()
                .antMatchers(HttpMethod.PATCH, "/api/v1/jobs/advertisements/application").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/transactions/verify/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/orders").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/customer-form-data/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/jobs/advertisements/application").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/tenants/bank-account-details").authenticated()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();
    }
}
