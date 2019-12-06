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
package com.forgerock.openbanking.aspsp.rs.api.event.v3_1_2.eventsubscription;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.constants.OpenBankingHttpHeaders;
import com.forgerock.openbanking.common.services.openbanking.event.EventValidationService;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.event.OBEventSubscription1;
import uk.org.openbanking.datamodel.event.OBEventSubscriptionResponse1;
import uk.org.openbanking.datamodel.event.OBEventSubscriptionsResponse1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;

@Controller("EventSubscriptionApiV3.1.2")
@Slf4j
public class EventSubscriptionApiController implements EventSubscriptionApi {
    private RSEndpointWrapperService rsEndpointWrapperService;
    private RsStoreGateway rsStoreGateway;

    public EventSubscriptionApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
    }


    @Override
    public ResponseEntity<OBEventSubscriptionResponse1> createEventSubscription(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBEventSubscription1 obEventSubscription1,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload." ,required=true)
            @RequestHeader(value="x-jws-signature", required=false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.eventNotificationEndpoint()
                .authorization(authorization)
                .principal(principal)
                .filters(f ->
                        {
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                            EventValidationService.verifyValidCallbackUrl(obEventSubscription1);
                        }
                )
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add(OpenBankingHttpHeaders.X_OB_CLIENT_ID, tppId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBEventSubscriptionResponse1.class, obEventSubscription1);
                        }
                );
    }

    @Override
    public ResponseEntity<OBEventSubscriptionsResponse1> readEventSubscription(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.eventNotificationEndpoint()
                .authorization(authorization)
                .principal(principal)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add(OpenBankingHttpHeaders.X_OB_CLIENT_ID, tppId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBEventSubscriptionsResponse1.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBEventSubscriptionResponse1> updateEventSubscription(
            @ApiParam(value = "EventSubscriptionId", required = true)
            @PathVariable("EventSubscriptionId") String eventSubscriptionId,

            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBEventSubscriptionResponse1 obEventSubscriptionsParam,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.eventNotificationEndpoint()
                .authorization(authorization)
                .principal(principal)
                .filters(f ->
                        {
                            f.verifyJwsDetachedSignature(xJwsSignature, request);
                            EventValidationService.verifyValidCallbackUrl(obEventSubscriptionsParam);
                        }
                ).execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add(OpenBankingHttpHeaders.X_OB_CLIENT_ID, tppId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, Collections.emptyMap(), OBEventSubscriptionsResponse1.class, obEventSubscriptionsParam);
                        }
                );
    }

    @Override
    public ResponseEntity deleteEventSubscription(
            @ApiParam(value = "EventSubscriptionId", required = true)
            @PathVariable("EventSubscriptionId") String eventSubscriptionId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        return rsEndpointWrapperService.eventNotificationEndpoint()
                .authorization(authorization)
                .principal(principal)
                .execute(
                        (String tppId) -> {
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.add(OpenBankingHttpHeaders.X_OB_CLIENT_ID, tppId);
                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, String.class);
                        }
                );
    }
}
