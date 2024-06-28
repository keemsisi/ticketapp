package org.core.backend.ticketapp.passport.config;

import io.github.thecarisma.Konfiger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class TicketAppConfig implements WebMvcConfigurer {
    public static Konfiger userManagementProperties;

    public static String resolveOptionalEnvFromSystem(String unprocessed) {
        if (unprocessed == null) {
            return unprocessed;
        }
        String processed = unprocessed;
        if (unprocessed.trim().startsWith("$")) {
            unprocessed = unprocessed.replace("${", "").replace("}", "");
            String[] arr = unprocessed.trim().split(":");
            StringBuilder fallback = new StringBuilder(arr.length > 1 ? arr[1] : "");
            if (arr.length > 2) {
                for (int i = 2; i < arr.length; ++i) {
                    fallback.append(":").append(arr[i]);
                }
            }
            processed = System.getenv().getOrDefault(arr.length > 0 ? arr[0] : "", fallback.toString());
        }
        return processed;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TicketAppMethodInterceptor());
    }
}
