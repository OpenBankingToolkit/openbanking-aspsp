/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.api.accesstoken;

import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Api(tags = "AccessToken", description = "the access token")
@RequestMapping(
        value = "/oauth2/"
)
public interface AccessTokenApi {
    @ApiOperation(
            value = "OAuth2 access token",
            tags = {"AccessToken", "OAuth2"}
    )

    @RequestMapping(
            value = "/access_token",
            method = RequestMethod.POST
    )
    @PreAuthorize("hasAnyAuthority('ROLE_PISP', 'ROLE_AISP', 'ROLE_CBPII')")
    ResponseEntity getAccessToken(
            @RequestBody MultiValueMap paramMap,

            @ApiParam(value = "Authorization header")
            @RequestHeader(value = "Authorization", required = false) String authorization,

            Principal principal,

            HttpServletRequest request
    ) throws OBErrorResponseException, OBErrorException;
}
