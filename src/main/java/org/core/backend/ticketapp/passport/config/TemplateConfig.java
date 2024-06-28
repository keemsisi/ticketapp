package org.core.backend.ticketapp.passport.config;

import org.core.backend.ticketapp.passport.config.properties.TemplateProperties;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;


public class TemplateConfig {


    public FileTemplateResolver remoteTemplateResolver(TemplateProperties properties) {
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setOrder(1);
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        return resolver;
    }
}
