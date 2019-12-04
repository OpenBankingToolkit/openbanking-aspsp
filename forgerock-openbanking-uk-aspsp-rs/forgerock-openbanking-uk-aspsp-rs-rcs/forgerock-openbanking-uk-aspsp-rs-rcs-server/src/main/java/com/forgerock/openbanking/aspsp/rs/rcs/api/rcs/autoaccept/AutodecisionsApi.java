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
