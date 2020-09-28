/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.common.utils;

import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.model.claim.Claims;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Map;

/**
 * Utility method(s) for manipulating a JWT's claims
 */
public class ClaimsUtils {

    /**
     * getClaims extracts the claims from a JWT
     *
     * @param signedJWT the JWT from which the claims are to be returned
     * @return a {@code Claims} object containing the claims extracted from {@code signedJWT}
     * @throws ParseException
     */
    public static Claims getClaims(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claimSet = signedJWT.getJWTClaimsSet();
        Map<String, Object> jsonClaimSet = claimSet.getJSONObjectClaim(OIDCConstants.OIDCClaim.CLAIMS);
        Claims claims = Claims.parseClaims(new JSONObject(jsonClaimSet));
        return claims;
    }
}
