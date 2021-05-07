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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_2.eventsubscription;

import com.forgerock.openbanking.aspsp.rs.store.repository.events.EventSubscriptionsRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.domain.event.FREventSubscriptionData;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FREventSubscription;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.event.EventResponseUtil;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.repositories.TppRepository;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.forgerock.openbanking.common.services.openbanking.converter.event.FREventSubscriptionConverter.toFREventSubscriptionData;

@Controller("EventSubscriptionApiV3.1.2")
@Slf4j
public class EventSubscriptionApiController implements EventSubscriptionApi {

    private final EventSubscriptionsRepository eventSubscriptionsRepository;
    private final TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;
    private final EventResponseUtil eventResponseUtil;

    @Autowired
    public EventSubscriptionApiController(EventSubscriptionsRepository eventSubscriptionsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.eventSubscriptionsRepository = eventSubscriptionsRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.eventResponseUtil = new EventResponseUtil(resourceLinkService, OBVersion.v3_1_2, true);
    }

    public EventSubscriptionApiController(EventSubscriptionsRepository eventSubscriptionsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService, EventResponseUtil eventResponseUtil) {
        this.eventSubscriptionsRepository = eventSubscriptionsRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.eventResponseUtil = eventResponseUtil;
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
        final Collection<FREventSubscription> byClientId = eventSubscriptionsRepository.findByTppId(isTpp.get().getId());
        if (!byClientId.isEmpty()) {
            log.debug("An event subscription already exists for this TPP client id: '{}' for the version: {}", clientId, byClientId.stream().findFirst().get());
            throw new OBErrorResponseException(
                    HttpStatus.CONFLICT,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.EVENT_SUBSCRIPTION_ALREADY_EXISTS.toOBError1()
            );
        }

        // Persist the event subscription
        FREventSubscription frEventSubscription = FREventSubscription.builder()
                .id(UUID.randomUUID().toString())
                .tppId(isTpp.get().getId())
                .eventSubscription(toFREventSubscriptionData(obEventSubscription))
                .build();
        eventSubscriptionsRepository.save(frEventSubscription);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(packageResponse(frEventSubscription));
    }

    public ResponseEntity readEventSubscription(
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
        final Optional<Tpp> tppId = Optional.ofNullable(tppRepository.findByClientId(clientId));
        if (!tppId.isEmpty()) {
            // A TPP must not access a event-subscription on an older version, via the EventSubscriptionId for an event-subscription created in a newer version
            Collection<FREventSubscription> frEventSubscriptions = Optional.ofNullable(eventSubscriptionsRepository.findByTppId(tppId.get().id))
                    .orElseGet(Collections::emptyList)
                    .stream().filter(frEventSubs -> eventResponseUtil.isAccessToResourceAllowedFromApiVersion(frEventSubs.getEventSubscription().getVersion())).collect(Collectors.toList());
            if (!frEventSubscriptions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(packageResponse(frEventSubscriptions));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The event subscription can't be read via an older API version.");
            }
        } else {
            throw new OBErrorResponseException(
                    HttpStatus.NOT_FOUND,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.TPP_NOT_FOUND.toOBError1(clientId)
            );
        }
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
        final Optional<FREventSubscription> byId = eventSubscriptionsRepository.findById(eventSubscriptionId);
        if (byId.isPresent()) {
            FREventSubscription existingEventSubscription = byId.get();
            // A TPP must not update a event-subscription on an older version, via the EventSubscriptionId for an event-subscription created in a newer version
            if (eventResponseUtil.isAccessToResourceAllowedFromApiVersion(existingEventSubscription.getEventSubscription().getVersion())) {
                existingEventSubscription.setEventSubscription(toFREventSubscriptionData(updatedSubscription));
                eventSubscriptionsRepository.save(existingEventSubscription);
                return ResponseEntity.ok(packageResponse(existingEventSubscription));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The event subscription can't be update via an older API version.");
            }
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
        final Optional<FREventSubscription> byId = eventSubscriptionsRepository.findById(eventSubscriptionId);
        if (byId.isPresent()) {
            // A TPP must not delete a event-subscription on an older version, via the EventSubscriptionId for an event-subscription created in a newer version
            if (eventResponseUtil.isAccessToResourceAllowedFromApiVersion(byId.get().getEventSubscription().getVersion())) {
                log.debug("Deleting event subscriptions URL: {}", byId.get());
                eventSubscriptionsRepository.deleteById(eventSubscriptionId);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("The event subscription can't be delete via an older API version.");
            }
        } else {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.EVENT_SUBSCRIPTION_NOT_FOUND.toOBError1(eventSubscriptionId)
            );
        }
    }

    private OBEventSubscriptionsResponse1 packageResponse(Collection<FREventSubscription> eventSubs) {
        List<OBEventSubscriptionsResponse1DataEventSubscription> eventSubsByClient = eventSubs.stream()
                .map(e -> new OBEventSubscriptionsResponse1DataEventSubscription()
                        .callbackUrl(e.getEventSubscription().getCallbackUrl())
                        .eventSubscriptionId(e.getId())
                        .eventTypes(e.getEventSubscription().getEventTypes())
                        .version(e.getEventSubscription().getVersion())
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
                    .links(resourceLinkService.toSelfLink(eventResponseUtil.getUrlEventSubscriptionsFunction()));
        }
    }

    private OBEventSubscriptionResponse1 packageResponse(FREventSubscription frEventSubscription) {
        FREventSubscriptionData obEventSubs = frEventSubscription.getEventSubscription();
        return new OBEventSubscriptionResponse1()
                .data(new OBEventSubscriptionResponse1Data()
                        .callbackUrl(obEventSubs.getCallbackUrl())
                        .eventSubscriptionId(frEventSubscription.getId())
                        .eventTypes(obEventSubs.getEventTypes())
                        .version(obEventSubs.getVersion())
                )
                .links(resourceLinkService.toSelfLink(frEventSubscription, eventResponseUtil.getUrlEventSubscriptionsFunction()))
                .meta(new Meta());
    }

}
