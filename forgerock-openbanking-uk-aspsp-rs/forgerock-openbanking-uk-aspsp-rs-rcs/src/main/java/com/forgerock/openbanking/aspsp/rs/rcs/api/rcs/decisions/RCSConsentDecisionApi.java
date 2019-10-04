/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.forgerock.openbanking.commons.model.rcs.RedirectionAction;
import com.forgerock.openbanking.exceptions.OBErrorException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "RCSconsent decision API", description = "the RCS consent Decision API")
public interface RCSConsentDecisionApi {

    @ApiOperation(
            value = "Send consent response",
            notes = "Send the PSU consent decision for a corresponding consent request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A redirection response, which indicates the URI the PSU needs to be redirected too",
                    response = RedirectionAction.class)
    })
    @RequestMapping(value = "/api/rcs/consent/decision/*" ,
            consumes = { "application/json; charset=utf-8" },
            produces = { "application/json; charset=utf-8" },
            method = RequestMethod.POST)
    ResponseEntity<RedirectionAction> decisionAccountSharing(
            @RequestBody String consentDecisionSerialised,

            @ApiParam(value = "Cookie containing the user session", required = true)
            @CookieValue(value = "${am.cookie.name}") String ssoToken) throws OBErrorException;
}
