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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_0;

import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.events.CallbackUrlsRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.event.EventResponseUtil;
import com.forgerock.openbanking.model.Tpp;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.event.OBCallbackUrl1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.forgerock.openbanking.common.services.openbanking.converter.event.FRCallbackUrlConverter.toFRCallbackUrlData;

@Controller("CallbackUrlsApiV3.0")
@Slf4j
public class CallbackUrlsApiController implements CallbackUrlsApi {

    private final CallbackUrlsRepository callbackUrlsRepository;
    private final TppRepository tppRepository;
    private final EventResponseUtil eventResponseUtil;
    private final ResourceLinkService resourceLinkService;

    @Autowired
    public CallbackUrlsApiController(CallbackUrlsRepository callbackUrlsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.callbackUrlsRepository = callbackUrlsRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.eventResponseUtil = new EventResponseUtil(this.resourceLinkService, OBVersion.v3_0, false);
    }

    public CallbackUrlsApiController(CallbackUrlsRepository callbackUrlsRepository, TppRepository tppRepository,ResourceLinkService resourceLinkService, EventResponseUtil eventResponseUtil) {
        this.callbackUrlsRepository = callbackUrlsRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
        this.eventResponseUtil = eventResponseUtil;
    }

    @Override
    public ResponseEntity createCallbackUrls(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBCallbackUrl1 obCallbackUrl1Param,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

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
    ) {
        log.debug("Create new callback URL: {} for client: {}", obCallbackUrl1Param, clientId);

        // Check if callback URL already exists for TPP
        // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/645367055/Event+Notification+API+Specification+-+v3.0#EventNotificationAPISpecification-v3.0-POST/callback-urls
        // A TPP must only create a callback-url on one version
        final Optional<Tpp> isTpp = Optional.ofNullable(tppRepository.findByClientId(clientId));

        if (!isTpp.isPresent()) {
            log.warn("No TPP found for client id '{}'", clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TPP not found");
        }

        final Collection<FRCallbackUrl> byClientId = callbackUrlsRepository.findByTppId(isTpp.get().getId());
        final boolean urlExists = byClientId.stream()
                .anyMatch(existingCallbackUrl -> obCallbackUrl1Param.getData().getUrl().equals(existingCallbackUrl.getCallbackUrl().getUrl()));
        if (urlExists) {
            log.debug("This callback URL: '{}' already exists for this TPP client id: '{}'", obCallbackUrl1Param.getData().getUrl(), clientId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Callback URL already exists");
        }

        FRCallbackUrl frCallbackUrl = FRCallbackUrl.builder()
                .id(UUID.randomUUID().toString())
                .tppId(isTpp.get().getId())
                .callbackUrl(toFRCallbackUrlData(obCallbackUrl1Param))
                .build();
        callbackUrlsRepository.save(frCallbackUrl);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventResponseUtil.packageResponse(frCallbackUrl));
    }

    @Override
    public ResponseEntity readCallBackUrls(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) {
        return Optional.ofNullable(tppRepository.findByClientId(clientId))
                .map(Tpp::getId)
                .map(id -> callbackUrlsRepository.findByTppId(id))
                // A TPP must only create a callback-url on one version and then there must be only one and we take only the first
                .map(urls -> {
                    if (urls.isEmpty()) {
                        log.warn("No CallbackURL found for client id '{}'", clientId);
                        return ResponseEntity.ok(eventResponseUtil.packageResponse(Collections.emptyList()));
                    }
                    return ResponseEntity.ok(eventResponseUtil.packageResponse(urls));
                })
                .orElseGet(() ->
                        {
                            log.warn("No TPP found for client id '{}'", clientId);
                            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                        }
                );
    }

    @Override
    public ResponseEntity updateCallbackUrl(
            @ApiParam(value = "CallbackUrlId", required = true)
            @PathVariable("CallbackUrlId") String callbackUrlId,

            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBCallbackUrl1 obCallbackUrl1Param,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload.", required = true)
            @RequestHeader(value = "x-jws-signature", required = false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = false) String clientId,

            HttpServletRequest request,

            Principal principal
    ) {
        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(callbackUrlId);

        if (byId.isPresent()) {
            FRCallbackUrl frCallbackUrl = byId.get();
            if(eventResponseUtil.isAccessToResourceAllowedFromApiVersion(frCallbackUrl.getCallbackUrl().getVersion())){
                frCallbackUrl.setCallbackUrl(toFRCallbackUrlData(obCallbackUrl1Param));
                callbackUrlsRepository.save(frCallbackUrl);
                return ResponseEntity.ok(eventResponseUtil.packageResponse(frCallbackUrl));
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Callback URL: '" + callbackUrlId + "' can't be update via an older API version.");
            }
        } else {
            // Spec isn't clear on if we should
            // 1. Reject a PUT for a resource id that does not exist
            // 2. Create a new resource for a PUT for resource id that does not exist
            // Option 2 is more restful but the examples in spec only use PUT for amending urls so I am implementing option 1.
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Callback URL: '" + callbackUrlId + "' can't be found");
        }
    }

    @Override
    public ResponseEntity deleteCallbackUrl(
            @ApiParam(value = "CallbackUrlId", required = true)
            @PathVariable("CallbackUrlId") String callbackUrlId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = false) String clientId,

            HttpServletRequest request,

            Principal principal
    ) {
        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(callbackUrlId);
        if (byId.isPresent()) {
            if(eventResponseUtil.isAccessToResourceAllowedFromApiVersion(byId.get().getCallbackUrl().getVersion())){
                log.debug("Deleting callback url: {}", byId.get());
                callbackUrlsRepository.deleteById(callbackUrlId);
                return ResponseEntity.noContent().build();
            }else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Callback URL: '" + callbackUrlId + "' can't be delete via an older API version.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Callback URL: '" + callbackUrlId + "' can't be found");
        }
    }
}
