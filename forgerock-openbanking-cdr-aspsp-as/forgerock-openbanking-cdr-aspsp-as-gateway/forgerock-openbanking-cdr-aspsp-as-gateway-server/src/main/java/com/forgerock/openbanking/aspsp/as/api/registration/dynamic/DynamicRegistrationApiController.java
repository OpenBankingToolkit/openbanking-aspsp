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
package com.forgerock.openbanking.aspsp.as.api.registration.dynamic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.eidas.EidasCertType;
import com.forgerock.cert.exception.InvalidEidasCertType;
import com.forgerock.cert.exception.InvalidPsd2EidasCertificate;
import com.forgerock.cert.exception.NoSuchRDNInField;
import com.forgerock.cert.utils.CertificateUtils;
import com.forgerock.openbanking.aspsp.as.service.SSAService;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.utils.extractor.TokenExtractor;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dev.openbanking4.spring.security.multiauth.model.authentication.X509Authentication;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.forgerock.openbanking.constants.OpenBankingConstants.RegistrationTppRequestClaims;
import static com.forgerock.openbanking.constants.OpenBankingConstants.SSAClaims;

@Controller
@Slf4j
public class DynamicRegistrationApiController implements DynamicRegistrationApi {

    public static final String ORIGIN_ID_EIDAS = "EIDAS";
    @Autowired
    private TppStoreService tppStoreService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    private TppRegistrationService tppRegistrationService;
    @Value("${dynamic-registration.supported-token-endpoint-auth-method}")
    private List<String> supportedAuthMethod;
    @Autowired
    private SSAService ssaService;

    @Override
    public ResponseEntity<Void> unregister(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=false) String authorization,

            Principal principal) throws OBErrorResponseException {
        return unregister(null, authorization, principal);
    }

    @Override
    public ResponseEntity<Void> unregister(
            @ApiParam(value = "The client ID",required=false)
            @PathVariable("ClientId") String clientId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=false) String authorization,

            Principal principal) throws OBErrorResponseException {
        Tpp tpp = getTpp(principal);
        verifyClientIDMatchTPP(tpp, clientId);
        String accessToken = verifyAccessToken(tpp, authorization);

        tppRegistrationService.unregisterTpp(accessToken, tpp);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> getRegisterResult(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            Principal principal) throws OBErrorResponseException {
        return getRegisterResult(null, authorization, principal);
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> getRegisterResult(
            @ApiParam(value = "The client ID",required=false)
            @PathVariable("ClientId") String clientId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            Principal principal) throws OBErrorResponseException {
        Tpp tpp = getTpp(principal);
        verifyClientIDMatchTPP(tpp, clientId);
        String accessToken = verifyAccessToken(tpp, authorization);

        return ResponseEntity.ok(tppRegistrationService.getOIDCClient(accessToken, tpp));
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> updateClient(
            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorResponseException, OBErrorException {
        return updateClient(null, authorization, registrationRequestJwtSerialised, principal);
    }

    @Override
    public ResponseEntity<OIDCRegistrationResponse> updateClient(
            @ApiParam(value = "The client ID",required=false)
            @PathVariable("ClientId") String clientId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorResponseException, OBErrorException {
        X509Authentication currentUser = (X509Authentication) principal;

        Tpp tpp = getTpp(principal);
        verifyClientIDMatchTPP(tpp, clientId);
        String accessToken = verifyAccessToken(tpp, authorization);
        try {
            SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
            String ssaSerialised = registrationRequestJws.getJWTClaimsSet()
                    .getStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);

            //Convert in json for convenience
            String registrationRequestJson = registrationRequestJws.getJWTClaimsSet().toJSONObject().toJSONString();

            OIDCRegistrationRequest oidcRegistrationRequest = objectMapper.readValue(
                    registrationRequestJson, OIDCRegistrationRequest.class);

            //Override client ID
            oidcRegistrationRequest.setClientId(clientId);

            log.debug("TPP request json payload {}", registrationRequestJson);

            String directoryId;
            if (ssaSerialised == null && currentUser.getAuthorities().contains(OBRIRole.ROLE_EIDAS)) {
                ssaSerialised = generateSSAForEIDAS(currentUser, registrationRequestJws, oidcRegistrationRequest);
                directoryId = ORIGIN_ID_EIDAS;
            } else {
                directoryId = tppRegistrationService.verifySSA(ssaSerialised);
                if (directoryId == null) {
                    log.debug("None of the directories signed this SSA {}", ssaSerialised);
                    throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_SSA_INVALID);
                }
            }
            SignedJWT ssaJws = SignedJWT.parse(ssaSerialised);
            JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
            JSONObject ssaJwsJson = ssaClaims.toJSONObject();

            log.debug("SSA {}", ssaSerialised);
            log.debug("SSA json payload {}", ssaJwsJson.toJSONString());

            verifyRegistrationRequest(registrationRequestJwtSerialised,
                    ssaSerialised, ssaClaims, oidcRegistrationRequest, tpp.getCertificateCn());

            Set<SoftwareStatementRole> types = tppRegistrationService.prepareRegistrationRequestWithSSA(ssaClaims, oidcRegistrationRequest, currentUser);

            tpp = tppRegistrationService.updateTpp(tpp, accessToken, registrationRequestJson, ssaClaims, ssaJwsJson, oidcRegistrationRequest,
                    directoryId, types);

            return ResponseEntity.status(HttpStatus.OK).body(tpp.getRegistrationResponse());
        } catch (HttpClientErrorException e) {
            log.error("An error happened in the AS '{}'", e.getResponseBodyAsString(), e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_OIDC_CLIENT_REGISTRATION_ISSUE, e.getMessage());
        } catch (ParseException | IOException e) {
            log.error("Couldn't parse registration request", e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
        } catch (CertificateEncodingException | NoSuchRDNInField e) {
            log.error("Error obtaining information from the certificate", e);
            throw new OBErrorException(OBRIErrorType.PRINCIPAL_MISSING_PSD2_INFORMATION, e);
        }
    }

    public static class CDRScope {
        public static final String BANK_ACCOUNT_BASIC_READ = "bank:accounts.basic:read";
        public static final String BANK_ACCOUNT_DETAILS_READ = "bank:accounts.detail:read";
        public static final String BANK_TRANSACTIONS_READ = "bank:transactions:read";
        public static final String BANK_PAYEE_READ = "bank:payees:read";
        public static final String BANK_REGULAR_PAYMENT_READ = "bank:regular_payments:read";
        public static final String COMMON_CUSTOMER_BASIC_READ = "common:customer.basic:read";
        public static final String COMMON_CUSTOMER_DETAILS_READ = "common:customer.detail:read";

        public CDRScope() {
        }
    }


    @Override
    public ResponseEntity<OIDCRegistrationResponse> register(
            @ApiParam(value = "A request to register a Software Statement Assertion with an ASPSP"  )
            @Valid
            @RequestBody String registrationRequestJwtSerialised,

            Principal principal
    ) throws OBErrorResponseException {
        log.debug("register TPP: request {}", registrationRequestJwtSerialised);

        try {
            X509Authentication authentication = (X509Authentication) principal;
            User currentUser = (User) authentication.getPrincipal();
            log.debug("User detail: username {} and authorities {}", currentUser.getUsername(), currentUser.getAuthorities());
            if (currentUser.getAuthorities().contains(OBRIRole.ROLE_AISP)
                    || currentUser.getAuthorities().contains(OBRIRole.ROLE_PISP)
                    || currentUser.getAuthorities().contains(OBRIRole.ROLE_CBPII)) {
                throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_ALREADY_REGISTERED,
                        currentUser.getUsername()
                );
            }

            if (currentUser.getAuthorities().contains(OBRIRole.UNKNOWN_CERTIFICATE)) {
                throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_UNKNOWN_TRANSPORT_CERTIFICATE,
                        currentUser.getUsername()
                );
            }

            if (currentUser.getAuthorities().contains(OBRIRole.UNREGISTERED_TPP)) {
                try {
                    SignedJWT registrationRequestJws = SignedJWT.parse(registrationRequestJwtSerialised);
                    String ssaSerialised = registrationRequestJws.getJWTClaimsSet()
                            .getStringClaim(RegistrationTppRequestClaims.SOFTWARE_STATEMENT);

                    //Convert in json for convenience
                    String registrationRequestJson = registrationRequestJws.getJWTClaimsSet().toJSONObject().toJSONString();
                    OIDCRegistrationRequest oidcRegistrationRequest = objectMapper.readValue(
                            registrationRequestJson, OIDCRegistrationRequest.class);

                    String directoryId;
                    if (ssaSerialised == null && currentUser.getAuthorities().contains(OBRIRole.ROLE_EIDAS)) {
                        ssaSerialised = generateSSAForEIDAS(authentication, registrationRequestJws, oidcRegistrationRequest);
                        directoryId = ORIGIN_ID_EIDAS;
                    } else {
                        directoryId = tppRegistrationService.verifySSA(ssaSerialised);
                        if (directoryId == null) {
                            log.debug("None of the directories signed this SSA {}", ssaSerialised);
                            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_SSA_INVALID);
                        }
                    }
                    SignedJWT ssaJws = SignedJWT.parse(ssaSerialised);
                    JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
                    JSONObject ssaJwsJson = ssaClaims.toJSONObject();
                    //delete client ID
                    oidcRegistrationRequest.setClientId(null);

                    log.debug("TPP request json payload {}", registrationRequestJson);
                    log.debug("SSA {}", ssaSerialised);
                    log.debug("SSA json payload {}", ssaJwsJson.toJSONString());

                    verifyRegistrationRequest(registrationRequestJwtSerialised,
                            ssaSerialised, ssaClaims, oidcRegistrationRequest, authentication.getCertificateChain());

                    Set<SoftwareStatementRole> types = tppRegistrationService.prepareRegistrationRequestWithSSA(ssaClaims, oidcRegistrationRequest, authentication);

                    Tpp tpp = tppRegistrationService.registerTpp(authentication.getCertificateChain()[0].getSubjectDN().toString(), registrationRequestJson, ssaClaims, ssaJwsJson, oidcRegistrationRequest, directoryId, types);

                    return ResponseEntity.status(HttpStatus.CREATED).body(tpp.getRegistrationResponse());
                } catch (HttpClientErrorException e) {
                    log.error("An error happened in the AS '{}'", e.getResponseBodyAsString(), e);
                    throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_OIDC_CLIENT_REGISTRATION_ISSUE, e.getMessage());
                } catch (ParseException | IOException e) {
                    log.error("Couldn't parse registration request", e);
                    throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
                }  catch (CertificateEncodingException | NoSuchRDNInField e) {
                    log.error("Error obtaining information from the certificate", e);
                    throw new OBErrorException(OBRIErrorType.PRINCIPAL_MISSING_PSD2_INFORMATION, e);
                }
            }
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_UNKNOWN_TRANSPORT_CERTIFICATE,
                    currentUser.getUsername()
            );
        } catch (OBErrorException e) {
            throw new OBErrorResponseException(
                    e.getObriErrorType().getHttpStatus(),
                    OBRIErrorResponseCategory.TPP_REGISTRATION,
                    e.getOBError());
        }
    }

    private String generateSSAForEIDAS(X509Authentication currentUser, SignedJWT registrationRequestJws, OIDCRegistrationRequest oidcRegistrationRequest) throws OBErrorException, NoSuchRDNInField, CertificateEncodingException {
        try {
            JWK jwk;

            if (registrationRequestJws.getHeader().getJWK() != null) {
                jwk = registrationRequestJws.getHeader().getJWK();
            } else if (registrationRequestJws.getHeader().getX509CertChain() != null && !registrationRequestJws.getHeader().getX509CertChain().isEmpty()) {
                List<X509Certificate> x509Certificates = new ArrayList<>();
                for (Base64 c : registrationRequestJws.getHeader().getX509CertChain()) {
                    X509Certificate x509Certificate = CertificateUtils.decodeCertificate(c.decode());
                    x509Certificates.add(x509Certificate);
                }

                Psd2CertInfo signingCertPSD2Info = new Psd2CertInfo(x509Certificates);
                if (!signingCertPSD2Info.isPsd2Cert()) {
                    log.debug("Certificate received is not detected as a PSD2 certificates");
                    throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_NOT_PSD2);
                }
                if (signingCertPSD2Info.getEidasCertType().isPresent()) {
                    log.debug("Certificate is not detected as a known EIDAS certificate");
                    throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_NOT_KNOWN_EIDAS);
                }
                if (signingCertPSD2Info.getEidasCertType().get() != EidasCertType.ESEAL) {
                    log.debug("Certificate received is not a QSEAL, it's: {}", signingCertPSD2Info.getEidasCertType().get());
                    throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_NOT_QSEAL, signingCertPSD2Info.getEidasCertType().get().name());
                }
                jwk = JWK.parse(x509Certificates.get(0));

            } else {
                log.error("Failed to obtain the signing key from the registration request");
                throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_NO_SIGNING_KEYS);
            }
            String ssa = ssaService.generateSSAForEIDAS(currentUser, jwk, oidcRegistrationRequest.getRedirectUris());
            oidcRegistrationRequest.setJwks(new JWKSet(jwk));
            return ssa;
        } catch (CertificateException | InvalidPsd2EidasCertificate | InvalidEidasCertType e) {
            log.debug("Couldn't read PSD2 certificate", e);
            throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_EIDAS_CERTIFICATE_READ_ISSUE);
        } catch (JOSEException e) {
            log.error("Couldn't covert the PSD2 certs into a JWK", e);
            throw new OBErrorException(OBRIErrorType.OBRI_REGISTRATION_EIDAS_CONVERT_TO_JWK);
        }
    }

    private void verifyRegistrationRequest(
            String registrationRequestJwtSerialised,
            String ssaSerialised, JWTClaimsSet ssaClaims,
            OIDCRegistrationRequest oidcRegistrationRequest,
            String subject
    ) throws OBErrorException, ParseException {
        X500Name x500name = new X500Name(subject);
        RDN cnRDN = x500name.getRDNs(BCStyle.CN)[0];
        verifyRegistrationRequest(registrationRequestJwtSerialised, ssaSerialised, ssaClaims, oidcRegistrationRequest, cnRDN);

    }

    private void verifyRegistrationRequest(
            String registrationRequestJwtSerialised,
            String ssaSerialised, JWTClaimsSet ssaClaims,
            OIDCRegistrationRequest oidcRegistrationRequest,
            X509Certificate[] chain
    ) throws OBErrorException, ParseException, CertificateEncodingException {
        //Verify request
        X500Name x500name = new JcaX509CertificateHolder(chain[0]).getSubject();
        RDN cnRDN = x500name.getRDNs(BCStyle.CN)[0];

        verifyRegistrationRequest(registrationRequestJwtSerialised, ssaSerialised, ssaClaims, oidcRegistrationRequest, cnRDN);
    }

    private void verifyRegistrationRequest(
            String registrationRequestJwtSerialised,
            String ssaSerialised, JWTClaimsSet ssaClaims,
            OIDCRegistrationRequest oidcRegistrationRequest,
            RDN cnRDN
    ) throws OBErrorException, ParseException {
        //Verify request

        String cn = IETFUtils.valueToString(cnRDN.getFirst().getValue());
        String softwareId = ssaClaims.getStringClaim(SSAClaims.SOFTWARE_ID);

        tppRegistrationService.verifySSASoftwareIDAgainstTransportCert(softwareId, cn);

        String softwareClientId = ssaClaims.getStringClaim(SSAClaims.SOFTWARE_CLIENT_ID);
        tppRegistrationService.verifyTPPRegistrationRequestSignature(registrationRequestJwtSerialised, softwareClientId, ssaClaims);
        tppRegistrationService.verifyTPPRegistrationRequestAgainstSSA(oidcRegistrationRequest, ssaClaims);
        verifyAuthenticationMethodSupported(oidcRegistrationRequest);
    }

    private void verifyClientIDMatchTPP(Tpp tpp, String oidcClient) throws OBErrorResponseException {
        if (oidcClient != null && !tpp.getClientId().equals(oidcClient)) {
            throw new OBErrorResponseException(
                    OBRIErrorType.TPP_REGISTRATION_INVALID_OIDC_CLIENT.getHttpStatus(),
                    OBRIErrorResponseCategory.TPP_REGISTRATION,
                    OBRIErrorType.TPP_REGISTRATION_INVALID_OIDC_CLIENT.toOBError1(oidcClient, tpp.getClientId()));
        }
    }

    private void verifyAuthenticationMethodSupported(OIDCRegistrationRequest oidcRegistrationRequest) throws OBErrorException {
        if (!supportedAuthMethod.contains(oidcRegistrationRequest.getTokenEndpointAuthMethod())) {
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_INVALID_AUTH_METHOD,
                    oidcRegistrationRequest.getTokenEndpointAuthMethod()
            );
        }
    }

    private Tpp getTpp( Principal principal) throws OBErrorResponseException {
        Optional<Tpp> optionalTpp = tppStoreService.findByClientId(principal.getName());
        if (!optionalTpp.isPresent()) {
            throw new OBErrorResponseException(
                    OBRIErrorType.TPP_REGISTRATION_NOT_REGISTERED.getHttpStatus(),
                    OBRIErrorResponseCategory.TPP_REGISTRATION,
                    OBRIErrorType.TPP_REGISTRATION_NOT_REGISTERED.toOBError1(principal.getName()));
        }
        return optionalTpp.get();
    }

    private String verifyAccessToken(Tpp tpp, String authorization) throws OBErrorResponseException {
        String accessToken = tokenExtractor.extract(authorization);
        if (!tpp.getRegistrationResponse().getRegistrationAccessToken().equals(tokenExtractor.extract(authorization))) {
            throw new OBErrorResponseException(
                    OBRIErrorType.TPP_REGISTRATION_INVALID_ACCESS_TOKEN.getHttpStatus(),
                    OBRIErrorResponseCategory.TPP_REGISTRATION,
                    OBRIErrorType.TPP_REGISTRATION_INVALID_ACCESS_TOKEN.toOBError1());
        }
        return accessToken;
    }
}
