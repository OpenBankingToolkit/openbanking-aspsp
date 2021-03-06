/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.filter;

import brave.Tracer;
import com.forgerock.openbanking.common.error.ErrorHandler;
import com.forgerock.openbanking.model.error.ErrorCode;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * This interceptor checks for missing Authorisation headers and returns a custom OBIE-compliant response
 * (Spring default validation returns a generic 400 but we need a 401 in OBIE)
 */
@Component
@Slf4j
public class AuthorisationHeaderInterceptor implements HandlerInterceptor {
    private ErrorHandler errorHandler;
    private Tracer tracer;
    private String docErrorsUrl;

    @Autowired
    public AuthorisationHeaderInterceptor(ErrorHandler errorHandler, Tracer tracer,  @Value("${doc.errors}") String docErrorsUrl) {
        this.errorHandler = errorHandler;
        this.tracer = tracer;
        this.docErrorsUrl = docErrorsUrl;
    }

    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        if (StringUtils.isEmpty(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            try {
                log.warn("The request was expected  to have an {} header but it was empty or missing", HttpHeaders.AUTHORIZATION);
                errorHandler.setHttpResponseToError(
                        response,
                        getErrorResponse(tracer, request.getServletPath()),
                        HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            } catch (IOException e) {
                log.error("Failed to set error response for missing authorization header", e);
            }
            return false;
        }
        log.trace("Authorisation header is present, allowed to proceed.");
        return true;
    }

    private OBErrorResponse1 getErrorResponse(Tracer tracer, String path) {
        return new OBErrorResponse1()
                .id(tracer.currentSpan().context().traceIdString())
                .code(OBRIErrorResponseCategory.REQUEST_FILTER.getId())
                .message(OBRIErrorResponseCategory.REQUEST_FILTER.getDescription())
                .errors(Collections.singletonList(
                        OBRIErrorType.ACCESS_TOKEN_INVALID
                                .toOBError1("Missing or empty 'Authorization' header")
                                .path(path)
                                .url(docErrorsUrl+"#"+ ErrorCode.OBRI_ACCESS_TOKEN_INVALID.getValue())
                ));
    }

}
