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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_2.callbackurl;

import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.events.CallbackUrlsRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.model.openbanking.persistence.event.FRCallbackUrl;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Controller("CallbackUrlsApiV3.1.2")
@Slf4j
public class CallbackUrlsApiController implements CallbackUrlsApi {

    private CallbackUrlsRepository callbackUrlsRepository;
    private TppRepository tppRepository;
    private final ResourceLinkService resourceLinkService;

    public CallbackUrlsApiController(CallbackUrlsRepository callbackUrlsRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        this.callbackUrlsRepository = callbackUrlsRepository;
        this.tppRepository = tppRepository;
        this.resourceLinkService = resourceLinkService;
    }

    @Override
    public ResponseEntity createCallbackUrls(
            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBCallbackUrl1 obCallbackUrl1Param,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "Header containing a detached JWS signature of the body of the payload." ,required=true)
            @RequestHeader(value="x-jws-signature", required=false) String xJwsSignature,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        log.debug("Create new callback URL: {} for client: {}", obCallbackUrl1Param, clientId);

        // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/645367055/Event+Notification+API+Specification+-+v3.0#EventNotificationAPISpecification-v3.0-POST/callback-urls
        final Optional<Tpp> isTpp = Optional.ofNullable(tppRepository.findByClientId(clientId));

        if (isTpp.isEmpty()) {
            log.warn("No TPP found for client id '{}'", clientId);
            throw new OBErrorResponseException(
                    HttpStatus.NOT_FOUND,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.TPP_NOT_FOUND.toOBError1(clientId));
        }

        // Check if callback URL already exists for TPP
        final Collection<FRCallbackUrl> byClientId = callbackUrlsRepository.findByTppId(isTpp.get().getId());
        final boolean urlExists = byClientId.stream()
                .anyMatch(existingCallbackUrl -> obCallbackUrl1Param.getData().getUrl().equals(existingCallbackUrl.getObCallbackUrl().getData().getUrl()));
        if (urlExists) {
            log.debug("This callback URL: '{}' already exists for this TPP client id: '{}'", obCallbackUrl1Param.getData().getUrl(), clientId);
            throw new OBErrorResponseException(
                    HttpStatus.CONFLICT,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.CALLBACK_URL_ALREADY_EXISTS.toOBError1(obCallbackUrl1Param.getData().getUrl())
            );
        }

        FRCallbackUrl frCallbackUrl = FRCallbackUrl.builder()
                .id(UUID.randomUUID().toString())
                .tppId(isTpp.get().getId())
                .obCallbackUrl(obCallbackUrl1Param)
                .build();
        callbackUrlsRepository.save(frCallbackUrl);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(packageResponse(frCallbackUrl));
    }

    @Override
    public ResponseEntity readCallBackUrls(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = true) String clientId,

            HttpServletRequest request,

            Principal principal
    )  throws OBErrorResponseException {
        return Optional.ofNullable(tppRepository.findByClientId(clientId))
                .map(Tpp::getId)
                .map(id -> callbackUrlsRepository.findByTppId(id))
                .map(url -> ResponseEntity.ok(packageResponse(url)))
                .orElseThrow(() -> new OBErrorResponseException(
                                    HttpStatus.NOT_FOUND,
                                    OBRIErrorResponseCategory.REQUEST_INVALID,
                                    OBRIErrorType.TPP_NOT_FOUND.toOBError1(clientId)
                            )
                );
    }

    @Override
    public ResponseEntity updateCallbackUrl(
            @ApiParam(value = "CallbackUrlId", required = true)
            @PathVariable("CallbackUrlId") String callbackUrlId,

            @ApiParam(value = "Default", required = true)
            @Valid
            @RequestBody OBCallbackUrl1 obCallbackUrl1Param,

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
    ) throws OBErrorResponseException {
        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(callbackUrlId);

        if (byId.isPresent()) {
            FRCallbackUrl frCallbackUrl = byId.get();
            frCallbackUrl.setObCallbackUrl(obCallbackUrl1Param);
            callbackUrlsRepository.save(frCallbackUrl);
            return ResponseEntity.ok(packageResponse(frCallbackUrl));
        } else {
            // Spec isn't clear on if we should
            // 1. Reject a PUT for a resource id that does not exist
            // 2. Create a new resource for a PUT for resource id that does not exist
            // Option 2 is more restful but the examples in spec only use PUT for amending urls so currently I am implementing option 1.
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.CALLBACK_URL_NOT_FOUND.toOBError1(callbackUrlId)
            );
        }
    }

    @Override
    public ResponseEntity deleteCallbackUrl(
            @ApiParam(value = "CallbackUrlId", required = true)
            @PathVariable("CallbackUrlId") String callbackUrlId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "The PISP Client ID")
            @RequestHeader(value = "x-ob-client-id", required = false) String clientId,

            HttpServletRequest request,

            Principal principal
    ) throws OBErrorResponseException {
        final Optional<FRCallbackUrl> byId = callbackUrlsRepository.findById(callbackUrlId);
        if (byId.isPresent()) {
            log.debug("Deleting callback url: {}", byId.get());
            callbackUrlsRepository.deleteById(callbackUrlId);
            return ResponseEntity.noContent().build();
        } else {
            throw new OBErrorResponseException(
                    HttpStatus.BAD_REQUEST,
                    OBRIErrorResponseCategory.REQUEST_INVALID,
                    OBRIErrorType.CALLBACK_URL_NOT_FOUND.toOBError1(callbackUrlId)
            );
        }
    }


    private OBCallbackUrlsResponse1 packageResponse(Collection<FRCallbackUrl> url) {
        final Optional<FRCallbackUrl> hasUrl = url.stream().findFirst();
        if (hasUrl.isPresent()) {
            return new OBCallbackUrlsResponse1()
                    .data(toOBCallbackUrlsResponseData1(hasUrl.get()))
                    .meta(new Meta())
                    .links(resourceLinkService.toSelfLink(hasUrl.get(), discovery -> discovery.getV_3_1_2().getGetCallbackUrls()));
        } else {
            return new OBCallbackUrlsResponse1()
                    .data(new OBCallbackUrlsResponseData1().callbackUrl(Collections.emptyList()))
                    .meta(new Meta())
                    .links(new Links());
        }
    }

    private OBCallbackUrlResponse1 packageResponse(FRCallbackUrl frCallbackUrl) {
        return new OBCallbackUrlResponse1()
                .data(toOBCallbackUrlResponseData1(frCallbackUrl))
                .meta(new Meta())
                .links(resourceLinkService.toSelfLink(frCallbackUrl, discovery -> discovery.getV_3_1_2().getGetCallbackUrls()));
    }

    private OBCallbackUrlsResponseData1 toOBCallbackUrlsResponseData1(FRCallbackUrl frCallbackUrl) {
        final OBCallbackUrlData1 data = frCallbackUrl.getObCallbackUrl().getData();
        return new OBCallbackUrlsResponseData1()
                .callbackUrl(Collections.singletonList(
                        new OBCallbackUrlResponseData1()
                                .url(data.getUrl())
                                .callbackUrlId(frCallbackUrl.getId())
                                .version(data.getVersion())
                ));
    }

    private OBCallbackUrlResponseData1 toOBCallbackUrlResponseData1(FRCallbackUrl frCallbackUrl) {
        final OBCallbackUrlData1 data = frCallbackUrl.getObCallbackUrl().getData();
        return new OBCallbackUrlResponseData1()
                .url(data.getUrl())
                .callbackUrlId(frCallbackUrl.getId())
                .version(data.getVersion());
    }
}
