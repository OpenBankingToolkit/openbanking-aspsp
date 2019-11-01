/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.filter;


import com.forgerock.openbanking.common.conf.discovery.ControllerEndpointBlacklist;
import com.forgerock.openbanking.common.conf.discovery.RSAPIsConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class DisabledEndpointInterceptor implements HandlerInterceptor {
    private final ControllerEndpointBlacklist controllerEndpointBlacklist;

    @Autowired
    public DisabledEndpointInterceptor(RSAPIsConfigurationProperties rsConfiguration) {
        controllerEndpointBlacklist = rsConfiguration.getControllerEndpointBlacklist();
    }

    /**
     * Finds the method that this request will be mapped to (if any) and block it with 404 if the method is disabled in RS configuration.
     *
     *This ensures that any annotated method in open-banking APIs that is not shown in RS discovery  will not be available.
     * @param request
     * @param response
     * @param handler
     * @return
     */
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (controllerEndpointBlacklist.contains(handlerMethod.getBeanType(), handlerMethod.getMethod())) {
                    log.warn("Request URI {} was BLOCKED due to RS configuration settings. handler method: {}", request.getRequestURI(), handlerMethod.getMethod());
                    try {
                        response.sendError(HttpStatus.NOT_FOUND.value());
                    } catch (IOException e) {
                        log.error("Failed to write error response: {}", "", e);
                    }
                    return false;
                }
        }

        log.debug("Request URI {} is not disabled in RS config. Allowed to proceed.", request.getRequestURI());
        return true;
    }

}
