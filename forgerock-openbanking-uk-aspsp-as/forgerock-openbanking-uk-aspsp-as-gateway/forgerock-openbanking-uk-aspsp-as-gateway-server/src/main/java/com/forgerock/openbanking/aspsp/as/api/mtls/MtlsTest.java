/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.api.mtls;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Api(tags = "MtlsTest", description = "Test your MTLS setup")
public class MtlsTest {

    public static class MtlsTestResponse {
        public String issuerId;
        public Set<String> authorities;
    }

    @ApiOperation(
            value = "Test your MATLS setup",
            notes = "The idea is that you attach your client certificates to the request, and this endpoint will tell you who you are, based on this certificate",
            response = MtlsTestResponse.class,
            tags = {"MtlsTest",}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Your identity based on the client certificate received",
                    response = MtlsTestResponse.class),
    })

    @RequestMapping(
            value = "/open-banking/mtlsTest",
            produces = {"application/json; charset=utf-8"},
            method = RequestMethod.GET
    )
    public ResponseEntity<MtlsTestResponse> mtlsTest(Principal principal) {

        MtlsTestResponse response = new MtlsTestResponse();
        if (principal == null) {
            return ResponseEntity.ok(response);
        }

        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
        response.issuerId = currentUser.getUsername();
        response.authorities = currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return ResponseEntity.ok(response);
    }
}
