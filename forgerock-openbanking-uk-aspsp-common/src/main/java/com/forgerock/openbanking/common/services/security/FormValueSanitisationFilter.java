/**
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
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FormValueSanitisationFilter implements Filter {
    private static final String ERROR_MESSAGE = "This request form contained forbidden HTML content for field: '%s' and was rejected";
    private static final String SUPPORTED_MEDIA_TYPE = MediaType.APPLICATION_FORM_URLENCODED_VALUE;

    private final ErrorHandler errorHandler;
    private final Tracer tracer;

    public FormValueSanitisationFilter(ErrorHandler errorHandler, Tracer tracer) {
        this.errorHandler = errorHandler;
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = ((HttpServletRequest) servletRequest);

        final String contentType =  httpRequest.getContentType();
        if (contentType != null && contentType.contains(SUPPORTED_MEDIA_TYPE) && httpRequest.getParameterMap() !=null) {
            final Optional<Map.Entry<String, String[]>> isFieldWithHtml = httpRequest
                    .getParameterMap().entrySet()
                    .stream()
                    .filter(TextSanitiserService::hasHtmlContent)
                    .findFirst();
            if (isFieldWithHtml.isPresent()) {
                errorHandler.setHttpResponseToError(
                        (HttpServletResponse) servletResponse,
                        getErrorResponse(tracer, String.format(ERROR_MESSAGE, isFieldWithHtml.get().getKey()), httpRequest.getServletPath()),
                        HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
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