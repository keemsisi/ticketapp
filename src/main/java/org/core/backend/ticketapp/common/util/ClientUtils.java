package org.core.backend.ticketapp.common.util;

import org.springframework.util.StringUtils;

import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Jideobi.Onuora on 6/22/2016.
 */
public class ClientUtils {

    public static void validateRedirectUri(Set<String> registeredRedirectUri, String requestedRedirect) {
        if (Objects.isNull(registeredRedirectUri) ||
                !registeredRedirectUri.contains(requestedRedirect)) {
            throw new RedirectMismatchException("Invalid redirect: " + requestedRedirect
                    + " does not match one of the registered values");
        }
    }

    public static String validateRequestedRedirectUri(Set<String> registeredRedirectUri, String requestedRedirect) {
        for (String redirectUri : registeredRedirectUri) {
            if (requestedRedirect != null && redirectMatches(requestedRedirect, redirectUri)) {
                return requestedRedirect;
            }
        }

        throw new RedirectMismatchException("Invalid redirect: " + requestedRedirect
                + " does not match one of the registered values");
    }

    private static boolean redirectMatches(String requestedRedirect, String redirectUri) {
        try {
            URL requested = new URL(requestedRedirect);
            URL registered = new URL(redirectUri);

            if (registered.getProtocol().equals(requested.getProtocol()) &&
                    requested.getHost().endsWith(registered.getHost())) {
                return StringUtils.cleanPath(requested.getPath())
                        .startsWith(StringUtils.cleanPath(registered.getPath()));
            }
        }
        catch (MalformedURLException e) {
        }
        return requestedRedirect.equals(redirectUri);
    }
}
