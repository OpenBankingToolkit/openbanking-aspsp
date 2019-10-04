/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.event.v3_1_2.aggregatedpolling;

import com.forgerock.openbanking.common.openbanking.OBGroupName;
import com.forgerock.openbanking.common.openbanking.OBReference;
import com.forgerock.openbanking.common.openbanking.OpenBankingAPI;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Api(value = "aggregated-polling", description = "the event notification aggregated polling API")
@OpenBankingAPI(
        obVersion = "3.1.2",
        obGroupName = OBGroupName.EVENT,
        obReference = OBReference.EVENTS
)
@RequestMapping(value = "/open-banking/v3.1.2/event")
public interface AggregatedPollingApi {

    @ApiOperation(value = "Poll events", nickname = "pollEvents", notes = "", response = OBEventPollingResponse1.class, authorizations = {
            @Authorization(value = "TPPOAuth2Security", scopes = {
                    @AuthorizationScope(scope = "accounts", description = "Accounts scope"),
                    @AuthorizationScope(scope = "payments", description = "Payments  scope"),
                    @AuthorizationScope(scope = "fundsconfirmations", description = "Funds Confirmations scope")
            })
    }, tags = {"event polling"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Events successfully polled", response = OBEventPollingResponse1.class),
            @ApiResponse(code = 400, message = "Bad request", response = OBErrorResponse1.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 405, message = "Method Not Allowed"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 415, message = "Unsupported Media Type"),
            @ApiResponse(code = 429, message = "Too Many Requests"),
            @ApiResponse(code = 500, message = "Internal Server Error", response = OBErrorResponse1.class)})
    @PreAuthorize("hasAnyAuthority('ROLE_AISP', 'ROLE_PISP')")
    @OpenBankingAPI(
            obReference = OBReference.EVENT_AGGREGATED_POLLING
    )
    @RequestMapping(
            produces = {"application/json; charset=utf-8"},
            consumes = {"application/json; charset=utf-8"},
            method = RequestMethod.POST)
    ResponseEntity<OBEventPollingResponse1> pollEvents(
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
    ) throws OBErrorResponseException;

}
