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
package com.forgerock.openbanking.common.services.onboarding.apiclient;

import com.forgerock.cert.Psd2CertInfo;
import com.forgerock.cert.eidas.EidasCertType;
import com.forgerock.cert.exception.InvalidEidasCertType;
import com.forgerock.openbanking.common.error.exception.oauth2.OAuth2InvalidClientException;
import com.forgerock.spring.security.multiauth.model.authentication.PSD2Authentication;
import com.forgerock.spring.security.multiauth.model.authentication.X509Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.security.auth.x500.X500Principal;
import java.security.Principal;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApiClientIdentityFactory {

    private final String OBIE_ISSUER_NAME = "CN=OpenBanking Pre-Production Issuing CA,O=OpenBanking,C=GB";
    private final String FORGEROCK_ISSUER_NAME = "CN=obri-external-ca,OU=forgerock.financial,O=ForgeRock,L=Bristol," +
            "ST=Avon,C=UK";

    public ApiClientIdentity getApiClientIdentity(Principal principal) throws ApiClientException, OAuth2InvalidClientException {
        if(principal == null){
            log.info("getApiClientIdentity() - No principal supplied.");
            throw new ApiClientException("No principal supplied. No way to identitify ApiClient");
        }

        ApiClientIdentity apiClientIdentity = null;
        if (principal instanceof PSD2Authentication) {
            PSD2Authentication authentication = (PSD2Authentication) principal;
            Psd2CertInfo certInfo = authentication.getPsd2CertInfo();
            if(certInfo.isPsd2Cert()){
                ApiClientCertificateType certType = getApiClientCertificateTypeFromPSD2(authentication);
                switch (certType) {
                    case FR_TRANSPORT:
                        apiClientIdentity = new ApiClientIdentityFRTransport(authentication);
                        break;
                    case OBWAC:
                        apiClientIdentity = new ApiClientIdentityOBWac(authentication);
                        break;
                    case QWAC:
                        apiClientIdentity = new ApiClientIdentityQWac(authentication);
                        break;
                    default:
                        String errorString = "Client presented an invalid Certificate " +
                                "Type for use as a Transport certificate. Type presented ': " + certType + "'";
                        log.info("getApiClientIdentity() {}", errorString);
                        throw new ApiClientException(errorString);

                }
            } else {
                log.info("ApiClient presented a deprecated OBTransport certificate.");
                throw new OAuth2InvalidClientException("Onboarding must be done with a PSD2 eIDAS certificate. " +
                        "OBTransport certificates have been depricated");
            }
        } else if (principal instanceof X509Authentication){
            X509Authentication authentication = (X509Authentication) principal;
            apiClientIdentity = createOBTransportIdentity(authentication);
        } else {
            log.info("getApiClientIdentity() Principal is not of recognised type. Class name is '{}'",
                    principal.getClass().getName());
            throw new ApiClientException("Unrecognised Principal type. Was expecting a PSDAuthentication or a " +
                    "X509Authentication");
        }
        return apiClientIdentity;
    }

    private ApiClientIdentity createOBTransportIdentity(X509Authentication authentication)
            throws OAuth2InvalidClientException, ApiClientException {
        ApiClientCertificateType certType = getApiClientCertificateTypeFromX509(authentication);
        if (certType == ApiClientCertificateType.OB_TRANSPORT) {
            return new ApiClientIdentityOBTransport(authentication);
        }
        String errorString = "Client presented an invalid Certificate " +
                "Type for use as a Transport certificate. Type presented ': " + certType + "'";
        log.info("getApiClientIdentity() {}", errorString);
        throw new ApiClientException(errorString);
    }

    private ApiClientCertificateType getApiClientCertificateTypeFromX509(X509Authentication authentication)
            throws ApiClientException {
        String methodName = "getApiClientcertificateFromX509()";
        log.debug("{}}, authentication is '{}'", methodName, authentication.toString());
        ApiClientCertificateType type;
        X509Certificate[] certChain = authentication.getCertificateChain();
        String issuer = getTransportCertificateIssuer(certChain);
        log.debug("{} certificate issuer is '{}'", methodName, issuer);
        if(issuer.equalsIgnoreCase(OBIE_ISSUER_NAME)){
            List<String> certUsage = getTransportCertificateUsage(certChain);
            if(certUsage.contains(CertificateUsageOIDs.SIGNER_OF_DOCUMENTS)){
                type = ApiClientCertificateType.OB_SIGNING;
            } else if (certUsage.contains(CertificateUsageOIDs.CLIENT_AUTH)){
                type = ApiClientCertificateType.OB_TRANSPORT;
            } else {
                String errorString = "OBIE issued (legacy) X509Certificate did not contain expected extended " +
                        "certificate usage purposes. Contained " + certUsage + ". Expected " +
                        CertificateUsageOIDs.CLIENT_AUTH + " or " + CertificateUsageOIDs.SIGNER_OF_DOCUMENTS;
                log.info("{} {}; authentication; {}", methodName, errorString, authentication);
                throw new ApiClientException(errorString);
            }
        } else {
            String errorMessage = "Non eIDAS/PSD2 certificate presented, but it is not an OB issued certificate.";
            log.info("{} {}", methodName, errorMessage);
            throw new ApiClientException(errorMessage);
        }
        return type;
    }


    private ApiClientCertificateType getApiClientCertificateTypeFromPSD2(PSD2Authentication authentication)
            throws ApiClientException {
        String methodName = "getApiClientCertificateTypeFromPSD2()";
        log.debug("{} called, authentication; '{}'", methodName, authentication);

        ApiClientCertificateType type;

        X509Certificate[] certChain = authentication.getCertificateChain();
        String issuer = getTransportCertificateIssuer(certChain);
        log.debug("{} certificate issuer is '{}'",methodName, issuer);

        Psd2CertInfo certInfo = authentication.getPsd2CertInfo();
        EidasCertType eidasCertType = getEidasCertType(certInfo);

        if(issuer.equalsIgnoreCase(FORGEROCK_ISSUER_NAME)){
            switch (eidasCertType){
                case ESEAL:
                    type = ApiClientCertificateType.FR_SIGNING;
                    break;
                case WEB:
                    type = ApiClientCertificateType.FR_TRANSPORT;
                    break;
                case ESIGN: // ESIGN certificates are meant as electronic replacements for signatures for natural people
                default:
                    String errorMessage = "Unrecognised ForgeRock eidas certificate type: " + eidasCertType + ". Etsi" +
                            " qcStatements must include field 0.4.0.1862.1.6 indicating qc type.";
                    log.info("{} {}", methodName, errorMessage);
                    throw new ApiClientException(errorMessage);
            }
        } else if (issuer.equalsIgnoreCase(OBIE_ISSUER_NAME)){
            switch (eidasCertType){
                case ESEAL:
                    type =  ApiClientCertificateType.OBSEAL;
                    break;
                case WEB:
                    type = ApiClientCertificateType.OBWAC;
                    break;
                case ESIGN:
                default:
                    String errorMessage = "Unrecognised OBIE eidas certificate type: " + eidasCertType + ". Etsi " +
                        "qcStatements must include field 0.4.0.1862.1.6 indicating qc type.";
                    log.info("{} {}", methodName, errorMessage);
                    throw new ApiClientException(errorMessage);
            }
        } else {
            // Must be a QTSP issued eidas certificate??
            switch (eidasCertType){
                case ESEAL:
                    type =  ApiClientCertificateType.QSEAL;
                    break;
                case WEB:
                    type = ApiClientCertificateType.QWAC;
                    break;
                case ESIGN:
                default:
                    String errorMessage = "Unrecognised QTSP issued eidas certificate type: " + eidasCertType + ". " +
                            "Etsi qcStatements must include field 0.4.0.1862.1.6 indicating qc type.";
                    log.info("{} {}", methodName, errorMessage);
                    throw new ApiClientException(errorMessage);
            }
        }
        log.debug("{} type is '{}'", methodName, type);
        return type;
    }

    private String getTransportCertificateIssuer(X509Certificate[] certChain) throws ApiClientException {
        X509Certificate clientCertificate = getTransportCertificate(certChain);
        X500Principal transportCertPrincipal = clientCertificate.getIssuerX500Principal();
        return transportCertPrincipal.getName();
    }

    private List<String> getTransportCertificateUsage(X509Certificate[] certChain) throws ApiClientException {
        String methodName = "getTransportCertificateUsage()";
        X509Certificate transportCert = getTransportCertificate(certChain);
        try {
            List<String> extendedKeyUsage = transportCert.getExtendedKeyUsage();
            log.debug("{} Key usage is '{}'", methodName, extendedKeyUsage);
            return extendedKeyUsage;
        } catch (CertificateParsingException cpe){
            String errorMessage =
                    "Could not obtain extendedKeyUsage from certificate. '" + transportCert + "'";
            log.info("{} {}",methodName, errorMessage);
            throw new ApiClientException(errorMessage, cpe);
        }
    }

    private X509Certificate getTransportCertificate(X509Certificate[] certChain) throws ApiClientException {
        if(certChain == null || certChain.length < 1){
            log.info("getTransportCertificate() No certificate chain available from the security principal");
            throw new ApiClientException("No certificate chain available from the security Principal");
        }
        return certChain[0];
    }


    private EidasCertType getEidasCertType(Psd2CertInfo certInfo) throws ApiClientException {
        String methodName = "getEidasCertType()";
        log.debug("{}; certInfo; '{}'", methodName, certInfo);
        Optional<EidasCertType> certTypeOptional;
        try {
            certTypeOptional = certInfo.getEidasCertType();
            if(certTypeOptional.isEmpty()){
                String errorMessage = "No certificate type available in PSD2 eIDAS certificate";
                log.info("{}, {}. certInfo; '{}'", methodName, errorMessage, certInfo);
                throw new InvalidEidasCertType(errorMessage);
            } else {
                return certTypeOptional.get();
            }
        } catch (InvalidEidasCertType invalidEidasCertType) {
            log.info("{} Could not get Eidas certificate from PSD2 eIDAS certificate; {}", methodName,
                    invalidEidasCertType.getMessage());
            throw new ApiClientException("Could not determine PSD2 eIDAS certificate type; "
                    + invalidEidasCertType.getMessage());
        }
    }

}
