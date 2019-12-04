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
package com.forgerock.openbanking.aspsp.rs.api.jwk;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.ParseException;

@Api(tags = "jwk", description = "the RS JWK API")
@RequestMapping("/api/jwk/*")
public interface JwkUriApi {

    @ApiOperation(value = "Get JWK URI", notes = "Get the public keys in JWK format")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Public JWKs", response = String.class)
    })
    @RequestMapping(value = "jwk_uri", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<String> jwksUri() throws ParseException;

}
