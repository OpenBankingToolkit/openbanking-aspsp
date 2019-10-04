/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.common.error.exception.PermissionDenyException;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccessTokenService {

    @Autowired
    private ObjectMapper objectMapper;

    public String getIntentId(SignedJWT accessToken) throws ParseException, IOException {
        String claims = accessToken.getJWTClaimsSet().getStringClaim(OpenBankingConstants.AMAccessTokenClaim.CLAIMS);
        JsonNode jsonClaims = objectMapper.readTree(claims);
        JsonNode idTokenClaims = jsonClaims.get(OpenBankingConstants.AMAccessTokenClaim.ID_TOKEN);
        JsonNode intentClaims = idTokenClaims.get(OpenBankingConstants.AMAccessTokenClaim.INTENT_ID);
        return intentClaims.path("value").asText();
    }

    public List<OBExternalPermissions1Code> isAllowed(FRAccountRequest accountRequest,
                                                      List<OBExternalPermissions1Code> permissions) throws PermissionDenyException {

        List<OBExternalPermissions1Code> allowedPermissions = new ArrayList<>();
        for (OBExternalPermissions1Code permission : permissions) {
            if (accountRequest.getPermissions().contains(permission)) {
                allowedPermissions.add(permission);
            }
        }
        if (allowedPermissions.isEmpty()) {
            throw new PermissionDenyException("Not allowed to do the following actions: " + permissions);
        }
        return allowedPermissions;
    }
}
