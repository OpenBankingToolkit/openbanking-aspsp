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
package com.forgerock.openbanking.aspsp.as.service;

import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.cert.exception.NoSuchRDNInField;
import com.forgerock.cert.psd2.Psd2QcStatement;
import com.forgerock.cert.psd2.Psd2Role;
import com.forgerock.cert.psd2.RolesOfPsp;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import dev.openbanking4.spring.security.multiauth.model.authentication.PSD2Authentication;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.cert.CertificateEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SSAService {
    @Autowired
    private CryptoApiClient cryptoApiClient;

    public String generateSSAForEIDAS(Authentication currentUser, JWK jwk, List<String> redirectUris) throws OBErrorException, NoSuchRDNInField, CertificateEncodingException {
        // read the psd2info and convert it into an SSA
        if (!(currentUser instanceof PSD2Authentication)) {
            log.error("Could not obtain PSD2 information from the user's eIDAS certificate");
            throw new OBErrorException(OBRIErrorType.PRINCIPAL_MISSING_PSD2_INFORMATION);
        }
        PSD2Authentication psd2Authentication = (PSD2Authentication) currentUser;
        try {
            Psd2QcStatement psd2QcStatement = psd2Authentication.getPsd2CertInfo().getPsd2QCStatement().get();
            RolesOfPsp rolesPSD2 = psd2QcStatement.getRoles();
            List<Psd2Role> psd2Roles = rolesPSD2.getRolesOfPsp().stream().map(r -> r.getRole()).collect(Collectors.toList());
            return generateSSAForEIDAS( psd2Authentication.getPsd2CertInfo().getApplicationId(), psd2Authentication.getPsd2CertInfo().getOrganizationId().get(), psd2Roles, jwk, redirectUris);
        } catch (InvalidPsd2EidasCertificate invalidPsd2EidasCertificate) {
            log.debug("Certificate received is not detected as a PSD2 certificates");
            throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_NOT_PSD2);
        }
    }


    public String generateSSAForEIDAS(String applicationId, String organisationId, List<Psd2Role> psd2Roles, JWK jwk, List<String> redirectUris) throws OBErrorException {
        String ssaSerialised;
        log.debug("Current user has presented an eIDAS certificate.");

        // Create an SSA from the EidasInformation
        String issuerId = "AS-API-EIDAS";



        List<SoftwareStatementRole> roles =
                psd2Roles.stream().map(c -> SoftwareStatementRole.getSSRole(c).get()).collect(Collectors.toList());

        JWTClaimsSet.Builder ssaClaims  = new JWTClaimsSet.Builder()
                .issuer(issuerId)
                .expirationTime(DateTime.now().plusMinutes(5).toDate())
                .claim(OpenBankingConstants.SSAClaims.SOFTWARE_MODE, "TEST")
                .claim(OpenBankingConstants.SSAClaims.ORG_STATUS, "Active")
                .claim(OpenBankingConstants.SSAClaims.SOFTWARE_ID, applicationId)
                .claim(OpenBankingConstants.SSAClaims.SOFTWARE_CLIENT_NAME, applicationId)
                .claim(OpenBankingConstants.SSAClaims.ORG_ID, organisationId)
                .claim(OpenBankingConstants.SSAClaims.SOFTWARE_ROLES, roles)
                .claim(OpenBankingConstants.SSAClaims.SOFTWARE_REDIRECT_URIS, redirectUris);

        try {
            ssaClaims.claim(OpenBankingConstants.SSAClaims.SOFTWARE_SIGINING_JWK, jwk.toJSONString());
            ssaSerialised = cryptoApiClient.signClaims(issuerId, ssaClaims.build(), false);

            //verify the QSEAL certificate matches the QWAC
            Psd2CertInfo signingCertPSD2Info = new Psd2CertInfo(jwk.getParsedX509CertChain());
            if (!organisationId.equals(signingCertPSD2Info.getOrganizationId().get())) {
                log.debug("The organisation ID {} from the QWAC certificate is not matching the organisation ID {} from the QSEAL.",
                        organisationId,
                        signingCertPSD2Info.getOrganizationId());
                throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_QSEAL_NOT_MATCHING_QWAC,
                        organisationId,
                        signingCertPSD2Info.getOrganizationId());
            }

        } catch (InvalidPsd2EidasCertificate e) {
            log.debug("Couldn't read PSD2 certificate", e);
            throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_EIDAS_CERTIFICATE_READ_ISSUE);
        }
        return ssaSerialised;
    }
}
