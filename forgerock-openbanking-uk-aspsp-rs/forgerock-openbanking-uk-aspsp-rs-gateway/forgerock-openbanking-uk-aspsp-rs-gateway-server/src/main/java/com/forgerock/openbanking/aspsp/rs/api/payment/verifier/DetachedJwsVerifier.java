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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import com.forgerock.openbanking.aspsp.rs.filter.MultiReadHttpServletRequest;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.model.version.OBVersion.v3_1_3;
import static com.forgerock.openbanking.common.model.version.OBVersion.v3_1_4;
import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;

@Component
@Slf4j
public class DetachedJwsVerifier {

    private static final Pattern JWS_DETACHED_SIGNATURE_PATTERN = Pattern.compile("(.*\\.)(\\..*)");

    private final TppStoreService tppStoreService;
    private final CryptoApiClient cryptoApiClient;

    public DetachedJwsVerifier(TppStoreService tppStoreService, CryptoApiClient cryptoApiClient) {
        this.tppStoreService = tppStoreService;
        this.cryptoApiClient = cryptoApiClient;
    }

    public void verifyDetachedJws(String detachedJws, OBVersion obVersion, HttpServletRequest request, Principal principal) throws OBErrorException {
        if (StringUtils.isEmpty(detachedJws)) {
            log.warn("Detached signature not provided");
            throw new OBErrorException(OBRIErrorType.DETACHED_JWS_INVALID, detachedJws, "Not provided");
        }
        try {
            MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(request);
            String body = multiReadRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            log.debug("Verify detached signature {} with payload {}", detachedJws, body);

            // obVersion is only set from 3.1.3 onwards
            if ((obVersion == null || obVersion.isBeforeVersion(v3_1_4)) && !isB64ClaimHeaderSetToFalse(detachedJws, body)) {
                log.warn("Invalid detached signature {}", detachedJws, "b64 claim header not set to false in version: " + obVersion);
                throw new OBErrorException(OBRIErrorType.DETACHED_JWS_INVALID, detachedJws, "b64 claim header not set to false");
            }
            if (obVersion != null && obVersion.isAfterVersion(v3_1_3) && isB64ClaimHeaderPresent(detachedJws, body)) {
                log.warn("Invalid detached signature {}", detachedJws, "b64 claim header must not be present in version: " + obVersion);
                throw new OBErrorException(OBRIErrorType.DETACHED_JWS_INVALID, detachedJws, "b64 claim header must not be present");
            }
            UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();
            Tpp tpp = tppStoreService.findByClientId(currentUser.getUsername()).get();
            if (tpp.getRegistrationResponse().getJwks() != null) {
                cryptoApiClient.validateDetachedJWSWithJWK(detachedJws, body, null, tpp.getClientId(), tpp.getRegistrationResponse().getJwks().getKeys().get(0));
            } else {
                cryptoApiClient.validateDetachedJWS(detachedJws, body, null, tpp.getClientId(), tpp.getRegistrationResponse().getJwks_uri());
            }
        } catch (InvalidTokenException e) {
            log.warn("Invalid detached signature {}", detachedJws, e);
            throw new OBErrorException(OBRIErrorType.DETACHED_JWS_INVALID, detachedJws, e.getMessage());
        } catch (IOException e) {
            log.error("Can't get the request body", e);
            throw new OBErrorException(OBRIErrorType.DETACHED_JWS_UN_ACCESSIBLE);
        } catch (ParseException e) {
            log.error("Can't parse JWS", e);
            throw new OBErrorException(SERVER_ERROR);
        }
    }

    private boolean isB64ClaimHeaderSetToFalse(String jwsDetachedSignature, String payload) throws OBErrorException, ParseException {
        String jwsSerialized = rebuildJWS(jwsDetachedSignature, payload);
        log.debug("The reconstructed JWS from the detached signature: {}", jwsSerialized);
        SignedJWT jws = (SignedJWT) JWTParser.parse(jwsSerialized);
        if (jws.getHeader().getCustomParam("b64") != null) {
            log.debug("B64 claim header in the detached signature: {}", jws.getHeader().getCustomParam("b64"));
            Object b64Param = jws.getHeader().getCustomParam("b64");
            if (!(b64Param instanceof Boolean)) {
                return false;
            }
            return ((Boolean)b64Param) == false;
        }
        return false;
    }

    private boolean isB64ClaimHeaderPresent(String jwsDetachedSignature, String payload) throws OBErrorException, ParseException {
        String jwsSerialized = rebuildJWS(jwsDetachedSignature, payload);
        log.debug("The reconstructed JWS from the detached signature: {}", jwsSerialized);
        SignedJWT jws = (SignedJWT) JWTParser.parse(jwsSerialized);
        return jws.getHeader().getCustomParam("b64") != null;
    }

    private String rebuildJWS(String jwsDetachedSignature, String bodySerialised) throws OBErrorException {
        String jwtPayloadEncoded = new String(java.util.Base64.getEncoder().encode(bodySerialised.getBytes()));
        jwtPayloadEncoded = jwtPayloadEncoded.replace("=", "");
        Matcher jwsDetachedSignatureMatcher = JWS_DETACHED_SIGNATURE_PATTERN.matcher(jwsDetachedSignature);
        if (!jwsDetachedSignatureMatcher.find()) {
            log.warn("{} is not a detached signature", jwsDetachedSignature);
            throw new OBErrorException(OBRIErrorType.DETACHED_JWS_INVALID, jwsDetachedSignature, "is not a detached signature");
        }
        return jwsDetachedSignatureMatcher.group(1) + jwtPayloadEncoded + jwsDetachedSignatureMatcher.group(2);
    }
}
