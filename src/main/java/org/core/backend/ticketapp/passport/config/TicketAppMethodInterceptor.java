package org.core.backend.ticketapp.passport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.core.backend.ticketapp.common.util.Helpers;
import org.core.backend.ticketapp.passport.annotation.PreAuthorizePermission;
import org.core.backend.ticketapp.passport.entity.Role;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class TicketAppMethodInterceptor implements HandlerInterceptor {

    // JVM Type inference issue here too
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        HandlerMethod handlerMethod;

        try {
            handlerMethod = (HandlerMethod) handler;
        } catch (ClassCastException e) {
            return true;
        }

        Method method = handlerMethod.getMethod();

        PreAuthorizePermission preAuthorizePermission = null;

        if (method.isAnnotationPresent(PreAuthorizePermission.class)) {
            preAuthorizePermission = method.getDeclaredAnnotation(PreAuthorizePermission.class);
        } else if (method.getDeclaringClass().isAnnotationPresent(PreAuthorizePermission.class)) {
            preAuthorizePermission = method.getDeclaringClass().getDeclaredAnnotation(PreAuthorizePermission.class);
        }

        if (preAuthorizePermission != null) {
            List<String> values = Arrays.asList(preAuthorizePermission.value());

            ArrayList<Object> userRoles = (ArrayList<Object>) request.getAttribute("ROLES");

            ArrayList<Object> userPermissions = (ArrayList<Object>) request.getAttribute("PERMISSIONS");

            if (userRoles == null || userPermissions == null) {
                return false;
            }

            for (Object role_ : userRoles) {
                Role role = (role_ instanceof Role) ? (Role) role_ :
                        new ObjectMapper().convertValue(role_, Role.class);

                if (role.getName().equals("ADMIN")) {
                    return true;
                }
            }


            Helpers.WriteError(request, response, HttpStatus.UNAUTHORIZED,
                    "You don't have the required permission to perform this operation");
            return false;
        }

        return true;
    }

}
