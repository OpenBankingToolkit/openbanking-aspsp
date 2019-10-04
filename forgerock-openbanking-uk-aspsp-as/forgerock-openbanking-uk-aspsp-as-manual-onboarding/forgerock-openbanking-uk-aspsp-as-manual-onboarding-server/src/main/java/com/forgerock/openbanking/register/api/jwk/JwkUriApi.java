/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.register.api.jwk;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;

@Api(tags = "jwk", description = "the RS Simulator JWK API")
@RequestMapping("/api/jwk/*")
public interface JwkUriApi {

    @ApiOperation(value = "Get JWK URI", notes = "Get the public keys in JWK format")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Public JWKs", response = String.class)
    })
    @RequestMapping(value = "jwk_uri", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<String> jwksUri() throws ParseException;

}
