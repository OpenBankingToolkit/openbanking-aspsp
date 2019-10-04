/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_0;

import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.events.CallbackUrlsRepository;
import com.forgerock.openbanking.common.model.openbanking.v3_0.event.FRCallbackUrl1;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.Tpp;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.org.openbanking.datamodel.event.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("CallbackUrlsApiV3.0")
@Slf4j
public class CallbackUrlsApiController implements CallbackUrlsApi {

    private CallbackUrlsRepository callbackUrlsRepository;
    private TppRepository tppRepository;

    public CallbackUrlsApiController(CallbackUrlsRepository callbackUrlsRepository, TppRepository tppRepository) {
        this.callbackUrlsRepository = callbackUrlsRepository;
        this.tppRepository = tppRepository;
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

        // Check if callback URL already exists for TPP
        // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/645367055/Event+Notification+API+Specification+-+v3.0#EventNotificationAPISpecification-v3.0-POST/callback-urls
        final Optional<Tpp> isTpp = Optional.ofNullable(tppRepository.findByClientId(clientId));

        if (!isTpp.isPresent()) {
            log.warn("No TPP found for client id '{}'", clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("TPP not found");
        }


        final Collection<FRCallbackUrl1> byClientId = callbackUrlsRepository.findByTppId(isTpp.get().getId());
        final boolean urlExists = byClientId.stream()
                .anyMatch(existingCallbackUrl -> obCallbackUrl1Param.getData().getUrl().equals(existingCallbackUrl.getObCallbackUrl().getData().getUrl()));
        if (urlExists) {
            log.debug("This callback URL: '{}' already exists for this TPP client id: '{}'", obCallbackUrl1Param.getData().getUrl(), clientId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Callback URL already exists");
        }

        FRCallbackUrl1 frCallbackUrl1 = FRCallbackUrl1.builder()
                .id(UUID.randomUUID().toString())
                .tppId(isTpp.get().getId())
                .obCallbackUrl(obCallbackUrl1Param)
                .build();
        callbackUrlsRepository.save(frCallbackUrl1);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(packageResponse(frCallbackUrl1));
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
    )  {
        return Optional.ofNullable(tppRepository.findByClientId(clientId))
                .map(Tpp::getId)
                .map(id -> callbackUrlsRepository.findByTppId(id))
                .map(urls -> ResponseEntity.ok(packageResponse(urls)))
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
    ) throws OBErrorResponseException {
        final Optional<FRCallbackUrl1> byId = callbackUrlsRepository.findById(callbackUrlId);

        if (byId.isPresent()) {
            FRCallbackUrl1 frCallbackUrl1 = byId.get();
            frCallbackUrl1.setObCallbackUrl(obCallbackUrl1Param);
            callbackUrlsRepository.save(frCallbackUrl1);
            return ResponseEntity.ok(packageResponse(frCallbackUrl1));
        } else {
            // Spec isn't clear on if we should
            // 1. Reject a PUT for a resource id that does not exist
            // 2. Create a new resource for a PUT for resource id that does not exist
            // Option 2 is more restful but the examples in spec only use PUT for amending urls so currently I am implementing option 1.
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
    ) throws OBErrorResponseException {
        final Optional<FRCallbackUrl1> byId = callbackUrlsRepository.findById(callbackUrlId);
        if (byId.isPresent()) {
            log.debug("Deleting callback url: {}", byId.get());
            callbackUrlsRepository.deleteById(callbackUrlId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Callback URL: '" + callbackUrlId + "' can't be found");
        }
    }

    private OBCallbackUrlsResponse1 packageResponse(final Collection<FRCallbackUrl1> frCallbackUrls) {
        final List<OBCallbackUrlResponseData1> callbackUrls =
                frCallbackUrls.stream()
                        .map(this::toOBCallbackUrlResponseData1)
                        .collect(Collectors.toList());
        return new OBCallbackUrlsResponse1()
                .data(new OBCallbackUrlsResponseData1()
                        .callbackUrl(callbackUrls)
                );
    }

    private OBCallbackUrlResponse1 packageResponse(FRCallbackUrl1 frCallbackUrl) {
        return new OBCallbackUrlResponse1()
                .data(
                        toOBCallbackUrlResponseData1(frCallbackUrl)
                );
    }


    private OBCallbackUrlResponseData1 toOBCallbackUrlResponseData1(FRCallbackUrl1 frCallbackUrl) {
        final OBCallbackUrlData1 data = frCallbackUrl.getObCallbackUrl().getData();
        return new OBCallbackUrlResponseData1()
                .callbackUrlId(frCallbackUrl.getId())
                .url(data.getUrl())
                .version(data.getVersion());
    }
}
