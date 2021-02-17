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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_2.aggregatedpolling;

import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.service.event.EventPollingService;
import com.forgerock.openbanking.common.model.openbanking.domain.event.FREventPolling;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;
import uk.org.openbanking.datamodel.event.OBEventPolling1;
import uk.org.openbanking.datamodel.event.OBEventPollingResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import static com.forgerock.openbanking.common.services.openbanking.converter.event.FREventPollingConverter.toFREventPolling;

@Controller("AggregatedPollingApi3.1.2")
@Slf4j
public class AggregatedPollingApiController implements AggregatedPollingApi {

    private final EventPollingService eventPollingService;
    private final TppRepository tppRepository;

    public AggregatedPollingApiController(EventPollingService eventPollingService, TppRepository tppRepository) {
        this.eventPollingService = eventPollingService;
        this.tppRepository = tppRepository;
    }

    @Override
    public ResponseEntity pollEvents(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBEventPolling1 obEventPolling,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        final Optional<Tpp> isTpp = Optional.ofNullable(tppRepository.findByClientId(clientId));
        if (isTpp.isEmpty()) {
            log.warn("No TPP found for client id '{}'", clientId);
            throw new OBErrorResponseException(
                    HttpStatus.NOT_FOUND,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.TPP_NOT_FOUND.toOBError1(clientId)
            );
        }
        final String tppId = isTpp.get().getId();

        FREventPolling frEventPolling = toFREventPolling(obEventPolling);
        log.debug("TPP '{}' sent aggregated polling request: {}", tppId, obEventPolling);
        eventPollingService.acknowledgeEvents(frEventPolling, tppId);
        eventPollingService.recordTppEventErrors(frEventPolling, tppId);
        Map<String, String> allEventNotifications = eventPollingService.fetchNewEvents(frEventPolling, tppId);

        // Apply limit on returned events
        Map<String, String> truncatedEventNotifications = eventPollingService.truncateEvents(obEventPolling.getMaxEvents(), allEventNotifications, tppId);
        boolean moreAvailable = truncatedEventNotifications.size() < allEventNotifications.size();

        ResponseEntity<OBEventPollingResponse1> response = ResponseEntity.ok(new OBEventPollingResponse1()
                .sets(truncatedEventNotifications)
                .moreAvailable((truncatedEventNotifications.isEmpty()) ? null : moreAvailable));
        log.debug("TPP '{}' aggregated polling response: {}", tppId, response.getBody());
        return response;
    }
}
