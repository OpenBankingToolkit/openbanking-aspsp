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
