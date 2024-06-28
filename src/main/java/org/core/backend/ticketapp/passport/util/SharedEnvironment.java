package org.core.backend.ticketapp.passport.util;

import org.core.backend.ticketapp.passport.config.TicketAppConfig;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SharedEnvironment {

    public String adminRegisterKey;

    public String noReplayMailAddress;

    public String smptUserName;

    public String supportEmailAddress;

    public String iconUrl;

    public String fineName;

    public String externalResourceLocation;

    public String blobPath;

    public String blobContainer;

    public SharedEnvironment(Environment env) {
        adminRegisterKey = env == null ? null : env.getProperty("app.admin-register-key");
        if (adminRegisterKey == null && TicketAppConfig.userManagementProperties != null) {
            adminRegisterKey = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("app.admin-register-key", ""));
        }

        noReplayMailAddress = env == null ? null : env.getProperty("app.email.no-reply");
        if (noReplayMailAddress == null && TicketAppConfig.userManagementProperties != null) {
            noReplayMailAddress = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("app.email.no-reply", ""));
        }

        smptUserName = env == null ? null : env.getProperty("spring.mail.username");
        if (smptUserName == null && TicketAppConfig.userManagementProperties != null) {
            smptUserName = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("spring.mail.username", ""));
        }
        if (smptUserName == null) {
            smptUserName = "no-reply@ticketapp.com";
        }
        if ((!smptUserName.contains("@") && !smptUserName.contains("."))) {
            smptUserName += smptUserName + "@ticketapp.com";
        }

        supportEmailAddress = env == null ? null : env.getProperty("app.email.support");
        if (supportEmailAddress == null && TicketAppConfig.userManagementProperties != null) {
            supportEmailAddress = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("app.email.support", ""));
        }

        iconUrl = env == null ? null : env.getProperty("app.icon-url");
        if (iconUrl == null && TicketAppConfig.userManagementProperties != null) {
            iconUrl = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("app.icon-url", ""));
        }

        fineName = env == null ? null : env.getProperty("app.fine-name");
        if (fineName == null && TicketAppConfig.userManagementProperties != null) {
            fineName = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("app.fine-name", ""));
        }

        externalResourceLocation = env == null ? null : env.getProperty("external-resource-location");
        if (externalResourceLocation == null && TicketAppConfig.userManagementProperties != null) {
            externalResourceLocation = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("external-resource-location", ""));
        }

        blobPath = env == null ? null : env.getProperty("blob.path");
        if (blobPath == null && TicketAppConfig.userManagementProperties != null) {
            blobPath = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("blob.path", ""));
        }

        blobContainer = env == null ? null : env.getProperty("blob.container");
        if (blobContainer == null && TicketAppConfig.userManagementProperties != null) {
            blobContainer = TicketAppConfig.resolveOptionalEnvFromSystem(
                    TicketAppConfig.userManagementProperties.getString("blob.container", ""));
        }
    }

}
