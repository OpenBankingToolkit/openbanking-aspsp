/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.rs.ui.api.bank;

import com.nimbusds.jose.jwk.JWKSet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Api(
        tags = "bank",
        description = "Get the info from the bank"
)
@RequestMapping("/api/bank")
public interface BankApi {

    @ApiOperation(value = "Get the public keys of the bank, in a JWK format")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The bank jwks", response = JWKSet.class),

    })
    @RequestMapping(value = "/keys/jwk_uri", method = RequestMethod.GET)
    ResponseEntity<String> getJwkUri(
            Principal principal
    );

}
