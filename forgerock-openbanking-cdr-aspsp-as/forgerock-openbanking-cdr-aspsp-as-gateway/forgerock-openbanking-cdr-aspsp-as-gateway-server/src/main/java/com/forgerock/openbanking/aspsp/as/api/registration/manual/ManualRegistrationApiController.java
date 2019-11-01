/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.api.registration.manual;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.cert.psd2.Psd2Role;
import com.forgerock.openbanking.aspsp.as.service.SSAService;
import com.forgerock.openbanking.aspsp.as.service.TppRegistrationService;
import com.forgerock.openbanking.authentication.model.authentication.X509Authentication;
import com.forgerock.openbanking.common.model.onboarding.ManualRegistrationRequest;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.SoftwareStatementRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationRequest;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forgerock.openbanking.aspsp.as.api.registration.dynamic.DynamicRegistrationApiController.ORIGIN_ID_EIDAS;

@Controller
@Slf4j
public class ManualRegistrationApiController implements ManualRegistrationApi {

    public static final String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
    public static final String END_CERT = "-----END CERTIFICATE-----";

    @Autowired
    private TppStoreService tppStoreService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TppRegistrationService tppRegistrationService;
    @Autowired
    private SSAService ssaService;

    @Value("${manual-onboarding.registration-request-base}")
    private Resource registrationRequestFile;

    private String registrationRequestFileContent = null;

    @Override
    public ResponseEntity<OIDCRegistrationResponse> registerApplication(
            @ApiParam(value = "Registration request", required = true)
            @Valid
            @RequestBody ManualRegistrationRequest manualRegistrationRequest,

            Principal principal
    ) throws OBErrorException {

        X509Authentication authentication = (X509Authentication) principal;
        User currentUser = (User) authentication.getPrincipal();
        log.debug("User detail: username {} and authorities {}", currentUser.getUsername(), currentUser.getAuthorities());
        try {
            log.debug("Received a manual onboarding registration request {}", manualRegistrationRequest);

            //Prepare the request
            String registrationRequestJson = getRegistrationRequest();
            OIDCRegistrationRequest oidcRegistrationRequest = objectMapper.readValue(registrationRequestJson, OIDCRegistrationRequest.class);
            oidcRegistrationRequest.setRedirectUris(manualRegistrationRequest.getRedirectUris());

            String directoryId;
            String ssaSerialised;
            if (manualRegistrationRequest.getQsealPem() == null
                    || "".equals(manualRegistrationRequest.getQsealPem())) {
                directoryId = tppRegistrationService.verifySSA(manualRegistrationRequest.getSoftwareStatementAssertion());
                ssaSerialised = manualRegistrationRequest.getSoftwareStatementAssertion();
                oidcRegistrationRequest.setSoftwareStatement(ssaSerialised);
            } else {
                JWK jwk = JWK.parse(parseCertificate(manualRegistrationRequest.getQsealPem()));
                ssaSerialised = ssaService.generateSSAForEIDAS(
                        manualRegistrationRequest.getAppId(),
                        manualRegistrationRequest.getOrganisationId(),
                        Stream.of( manualRegistrationRequest.getPsd2Roles().split(","))
                                .map (r -> Psd2Role.valueOf(r))
                                .collect(Collectors.toList()),
                        jwk,
                        manualRegistrationRequest.getRedirectUris());
                directoryId = ORIGIN_ID_EIDAS;
            }

            SignedJWT ssaJws = SignedJWT.parse(ssaSerialised);

            //Convert in json for conveniency
            JWTClaimsSet ssaClaims = ssaJws.getJWTClaimsSet();
            //Verify the SSA
            JSONObject ssaJwsJson = ssaClaims.toJSONObject();

            Set<SoftwareStatementRole> types = tppRegistrationService.prepareRegistrationRequestWithSSA(ssaClaims, oidcRegistrationRequest, authentication);

            log.debug("The SSA was verified successfully");

            log.debug("The OIDC registration request we are going to send to AM {}", oidcRegistrationRequest);

            //Register the TPP
            Tpp tpp = tppRegistrationService.registerTpp(tppRegistrationService.getCNFromSSA(directoryId, ssaClaims),
                    registrationRequestJson, ssaClaims,
                    ssaJwsJson, oidcRegistrationRequest, directoryId, types);
            log.debug("Successfully onboard! the tpp resulting: {}", tpp);

            return ResponseEntity.status(HttpStatus.CREATED).body(tpp.getRegistrationResponse());
        } catch (HttpClientErrorException e) {
            log.error("An error happened in the AS '{}'", e.getResponseBodyAsString(), e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_OIDC_CLIENT_REGISTRATION_ISSUE, e.getMessage());
        } catch (ParseException | IOException e) {
            log.error("Couldn't parse registration request", e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
        } catch (JOSEException e) {
            log.error("QSEAL pem is not in the right format", e);
            throw new OBErrorException(OBRIErrorType.TPP_REGISTRATION_REQUEST_INVALID_FORMAT);
        }
    }

    @Override
    public ResponseEntity<Boolean> unregisterApplication(
            @ApiParam(value = "Unregister application", required = true)
            @Valid
            @PathVariable(value = "clientId") String clientId,

            Principal principal
    ) {
        Optional<Tpp> isTpp = tppStoreService.findByClientId(clientId);
        if (isTpp.isPresent()) {
            tppRegistrationService.unregisterTpp(isTpp.get().getRegistrationResponse().getRegistrationAccessToken(), isTpp.get());
        }
        return ResponseEntity.ok(true);
    }

    public String getRegistrationRequest() {
        if (registrationRequestFileContent == null) {
            try {
                registrationRequestFileContent =  StreamUtils.copyToString(registrationRequestFile.getInputStream(), Charset.defaultCharset());
            } catch (IOException e) {
                log.error("Can't read registration request resource", e);
                throw new RuntimeException(e);
            }
        }
        return registrationRequestFileContent;
    }

    private X509Certificate parseCertificate(String certStr) {
        //before decoding we need to get rod off the prefix and suffix
        log.debug("Client certificate as PEM format: \n {}", certStr);

        try {

            byte [] decoded = Base64.getDecoder()
                    .decode(
                            certStr
                                    .replaceAll("\n", "")
                                    .replaceAll(BEGIN_CERT, "")
                                    .replaceAll(END_CERT, ""));
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(decoded));
        } catch (CertificateException e) {
            log.error("Can't initialise certificate factory", e);
        }
        return null;
    }
}
