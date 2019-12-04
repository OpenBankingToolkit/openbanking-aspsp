/**
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
package com.forgerock.openbanking.aspsp.rs.api.mtls;

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
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@Api(tags = "MtlsTest", description = "Test your MTLS setup")
public class MtlsTest {

    public static class MtlsTestResponse {
        public String issuerId;
        public Collection<String> authorities;
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
        response.authorities = currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
