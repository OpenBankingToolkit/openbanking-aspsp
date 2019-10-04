/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.autoaccept;

import com.forgerock.openbanking.common.model.rcs.RedirectionAction;
import com.forgerock.openbanking.common.model.rcs.consentdetails.ConsentDetails;
import com.forgerock.openbanking.exceptions.OBErrorException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "RCS auto-decisions API", description = "the RCS auto-accepting consents")
@RequestMapping("/api/rcs/consent/auto-accept/*")
public interface AutodecisionsApi {

    @ApiOperation(value = "Auto-accept consent", notes = "Auto-accept consent")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Consent details",
                    response = ConsentDetails.class)
    })
    @RequestMapping(value = "/",
            consumes = {"application/jwt; charset=utf-8"},
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.POST)
    ResponseEntity<RedirectionAction> autoAccept(
            @ApiParam(value = "Consent request JWT received by AM", required = true)
            @RequestBody String consentRequestJwt,

            @ApiParam(value = "Cookie containing the user session", required = true)
            @CookieValue(value = "${am.cookie.name}") String ssoToken) throws OBErrorException;
}
