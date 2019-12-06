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
package com.forgerock.openbanking.common.services.security;

import brave.Tracer;
import com.forgerock.openbanking.common.error.ErrorHandler;
import com.forgerock.openbanking.common.http.MultiReadHttpRequestWrapper;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
public class JsonRequestSanitisiationFilter implements Filter {
    private static final String ERROR_MESSAGE = "This request body contained forbidden HTML content and was rejected";
    private final ErrorHandler errorHandler;
    private final Tracer tracer;

    public JsonRequestSanitisiationFilter(ErrorHandler errorHandler, Tracer tracer) {
        this.errorHandler = errorHandler;
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = ((HttpServletRequest) servletRequest);

        // Only sanitise POST and PUT
        HttpMethod method = HttpMethod.valueOf(httpRequest.getMethod());
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            String contentType =  httpRequest.getContentType();
            // Only sanitise JSON content type
            if (contentType == null || contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                httpRequest = new MultiReadHttpRequestWrapper(httpRequest); // Required to avoid 'read once only' error on standard requests
                final String unsanitisedRequestBody = CharStreams.toString(httpRequest.getReader());
                if (TextSanitiserService.hasHtmlContent(unsanitisedRequestBody)) {
                    errorHandler.setHttpResponseToError(
                            (HttpServletResponse) servletResponse,
                            getErrorResponse(tracer, ERROR_MESSAGE, httpRequest.getServletPath()),
                            HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }
        }
        filterChain.doFilter(httpRequest, servletResponse);
    }

    private static OBErrorResponse1 getErrorResponse(Tracer tracer, String errorMsg, String path) {
        return new OBErrorResponse1()
                .id(tracer.currentSpan().context().traceIdString())
                .code(OBRIErrorResponseCategory.REQUEST_FILTER.getId())
                .message(OBRIErrorResponseCategory.REQUEST_FILTER.getDescription())
                .errors(Collections.singletonList(
                        OBRIErrorType.REQUEST_OBJECT_INVALID
                                .toOBError1(errorMsg)
                                .path(path)
                ));
    }
}