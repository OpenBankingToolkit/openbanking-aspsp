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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_2.eventsubscription;

import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_2.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.event.FREventSubscription1;
import com.forgerock.openbanking.common.services.openbanking.event.EventValidationService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.account.Meta;
import uk.org.openbanking.datamodel.event.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller("EventSubscriptionApiV3.1.2")
@Slf4j
public class EventSubscriptionApiController implements EventSubscriptionApi {

    private final EventSubscriptionsRepository eventSubscriptionsRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;

    public EventSubscriptionApiController(EventSubscriptionsRepository eventSubscriptionsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.eventSubscriptionsRepository = eventSubscriptionsRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
    }

    public ResponseEntity createEventSubscription(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBEventSubscription1 obEventSubscription,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Create new event subscriptions: {} for client: {}", obEventSubscription, clientId);

        // Get the TPP from the header
        final Optional<Tpp> isTpp = Optional.ofNullable(tppRepository.findByClientId(clientId));
        if (isTpp.isEmpty()) {
            log.warn("No TPP found for client id '{}'", clientId);
            throw new OBErrorResponseException(
                    HttpStatus.NOT_FOUND,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.TPP_NOT_FOUND.toOBError1(clientId)
            );
        }

        // Check if an event already exists for this TPP
        final Collection<FREventSubscription1> byClientId = eventSubscriptionsRepository.findByTppId(isTpp.get().getId());
        if (!byClientId.isEmpty()) {
            log.debug("An event subscription already exists for this TPP client id: '{}' for the version: {}", clientId, byClientId.stream().findFirst().get());
            throw new OBErrorResponseException(
                    HttpStatus.CONFLICT,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.EVENT_SUBSCRIPTION_ALREADY_EXISTS.toOBError1()
            );
        }

        // Persist the event subscription
        FREventSubscription1 frEventSubscription1 = FREventSubscription1.builder()
                .id(UUID.randomUUID().toString())
                .tppId(isTpp.get().getId())
                .obEventSubscription1(obEventSubscription)
                .build();
        eventSubscriptionsRepository.save(frEventSubscription1);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(packageResponse(frEventSubscription1));
    }

    public ResponseEntity<OBEventSubscriptionsResponse1> readEventSubscription(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Read event subscription for client: {}", clientId);

        return Optional.ofNullable(tppRepository.findByClientId(clientId))
                .map(Tpp::getId)
                .map(eventSubscriptionsRepository::findByTppId)
                .map(eventSubs -> ResponseEntity.status(HttpStatus.OK).body(packageResponse(eventSubs)))
                .orElseThrow( () ->
                            new OBErrorResponseException(
                                HttpStatus.NOT_FOUND,
                                OBRIErrorResponseCategory.REQUEST_INVALID,
                                OBRIErrorType.TPP_NOT_FOUND.toOBError1(clientId)
                            )
                );
    }

    public ResponseEntity updateEventSubscription(
            @ApiParam(value = "EventSubscriptionId", required = true)
            @PathVariable("EventSubscriptionId") String eventSubscriptionId,

            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBEventSubscriptionResponse1 obEventSubscriptionParam,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {

        final OBEventSubscription1 updatedSubscription =
                new OBEventSubscription1().data(new OBEventSubscription1Data()
                        .callbackUrl(obEventSubscriptionParam.getData().getCallbackUrl())
                        .eventTypes(obEventSubscriptionParam.getData().getEventTypes())
                        .version(obEventSubscriptionParam.getData().getVersion())
                );
        final Optional<FREventSubscription1> byId = eventSubscriptionsRepository.findById(eventSubscriptionId);
        if (byId.isPresent()) {
            FREventSubscription1 existingEventSubscription = byId.get();
            EventValidationService.checkEqualOrNewerVersion(existingEventSubscription.getObEventSubscription1(), updatedSubscription);

            existingEventSubscription.setObEventSubscription1(updatedSubscription);
            eventSubscriptionsRepository.save(existingEventSubscription);
            return ResponseEntity.ok(packageResponse(Collections.singletonList(existingEventSubscription)));
        } else {
            // PUT is only used for amending existing subscriptions
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.EVENT_SUBSCRIPTION_NOT_FOUND.toOBError1(eventSubscriptionId)
            );
        }
    }

    public ResponseEntity deleteEventSubscription(
            @ApiParam(value = "EventSubscriptionId", required = true)
            @PathVariable("EventSubscriptionId") String eventSubscriptionId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        final Optional<FREventSubscription1> byId = eventSubscriptionsRepository.findById(eventSubscriptionId);
        if (byId.isPresent()) {
            log.debug("Deleting event subscriptions URL: {}", byId.get());
            eventSubscriptionsRepository.deleteById(eventSubscriptionId);
            return ResponseEntity.noContent().build();
        } else {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.EVENT_SUBSCRIPTION_NOT_FOUND.toOBError1(eventSubscriptionId)
            );
        }
    }

    private OBEventSubscriptionsResponse1 packageResponse(Collection<FREventSubscription1> eventSubs) {
        List<OBEventSubscriptionsResponse1DataEventSubscription> eventSubsByClient = eventSubs.stream()
                .map(e -> new OBEventSubscriptionsResponse1DataEventSubscription()
                        .callbackUrl(e.getObEventSubscription1().getData().getCallbackUrl())
                        .eventSubscriptionId(e.getId())
                        .eventTypes(e.getObEventSubscription1().getData().getEventTypes())
                        .version(e.getObEventSubscription1().getData().getVersion())
                ).collect(Collectors.toList());

        if (eventSubsByClient.isEmpty()) {
            return new OBEventSubscriptionsResponse1()
                    .data(new OBEventSubscriptionsResponse1Data().eventSubscription(Collections.emptyList()))
                    .meta(new Meta())
                    .links(new Links());
        } else {
            return new OBEventSubscriptionsResponse1()
                    .data(new OBEventSubscriptionsResponse1Data()
                            .eventSubscription(eventSubsByClient))
                    .meta(new Meta())
                    .links(resourceLinkService.toSelfLink(discovery -> discovery.getV_3_1_2().getGetCallbackUrls()));
        }
    }

    private OBEventSubscriptionResponse1 packageResponse(FREventSubscription1 frEventSubscription1) {
        OBEventSubscription1Data obEventSubs = frEventSubscription1.getObEventSubscription1().getData();
        return new OBEventSubscriptionResponse1()
                .data(new OBEventSubscriptionResponse1Data()
                        .callbackUrl(obEventSubs.getCallbackUrl())
                        .eventSubscriptionId(frEventSubscription1.getId())
                        .eventTypes(obEventSubs.getEventTypes())
                        .version(obEventSubs.getVersion())
                )
                .links(resourceLinkService.toSelfLink(frEventSubscription1, discovery -> discovery.getV_3_1_2().getGetCallbackUrls()))
                .meta(new Meta());
    }
}
