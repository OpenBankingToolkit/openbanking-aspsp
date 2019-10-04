/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.model.error.ErrorResponseTmp;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiFunction;

@Service
@Slf4j
public class ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ErrorHandler(ObjectMapper jacksonObjectMapper) {
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    public ErrorResponseTmp load(String jsonInString) {
        try {
            return jacksonObjectMapper.readValue(jsonInString, ErrorResponseTmp.class);
        } catch (IOException e) {
            LOGGER.error("Could not load error response {}", jsonInString);
            return ErrorResponseTmp.code(-1).message("Could not load error response");
        }
    }

    /**
     * Helper method to set a bad response
     * @param servletResponse HTTP Response
     * @param obErrorResponse Error to return
     * @throws IOException Failed to write to HTTP response
     */
    public void setHttpResponseToError(final HttpServletResponse servletResponse, final OBErrorResponse1 obErrorResponse, int httpErrorCode) throws IOException {
        Preconditions.checkArgument(httpErrorCode >= 400); // Disallow 1xx, 2xx and 3xx for error response
        servletResponse.resetBuffer();
        servletResponse.getOutputStream()
                .write(ErrorToJson
                        .apply(jacksonObjectMapper, obErrorResponse)
                        .getBytes()
        );
        servletResponse.setStatus(httpErrorCode);
    }

    public static final BiFunction<ObjectMapper, OBErrorResponse1, String> ErrorToJson = (om, error) -> {
        try {
            return om.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            log.error("Failed to create json error. Returning simple string error as fallback.", e);
            return (error.getErrors().isEmpty()) ? error.getMessage() : error.getErrors().get(0).getMessage();
        }
    };

}
