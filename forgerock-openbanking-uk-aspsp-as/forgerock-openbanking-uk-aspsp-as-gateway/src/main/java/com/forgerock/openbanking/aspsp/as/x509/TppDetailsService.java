/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.x509;

import com.forgerock.openbanking.commons.auth.x509.ForgeRockAppMATLService;
import com.forgerock.openbanking.commons.configuration.auth.MATLSConfigurationProperties;
import com.forgerock.openbanking.commons.services.directory.DirectoryService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.model.OBRIRole;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.UserContext;
import com.forgerock.openbanking.ssl.services.keystore.KeyStoreService;
import com.nimbusds.jose.jwk.JWK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TppDetailsService extends ForgeRockAppMATLService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TppDetailsService.class);

    public TppDetailsService(
            @Value("${certificates.selfsigned.forgerock.root}") Resource forgerockSelfSignedRootCertificatePem,
            @Value("${gateway.client-jwk-header}") String clientJwkHeader,
            DirectoryService directoryService,
            KeyStoreService keyStoreService,
            MATLSConfigurationProperties matlsConfigurationProperties,
            TppStoreService tppStoreService
    ){
        super(forgerockSelfSignedRootCertificatePem,
                clientJwkHeader,
                directoryService,
                keyStoreService,
                matlsConfigurationProperties);
        this.tppStoreService = tppStoreService;
    }

    private TppStoreService tppStoreService;

    @Override
    protected UserContext getUserDetails(JWK jwk, X509Certificate[] certificatesChain) {
        UserContext userDetails = super.getUserDetails(jwk, certificatesChain);
        if (userDetails.getAuthorities().contains(OBRIRole.ROLE_SOFTWARE_STATEMENT)) {
            Optional<Tpp> optionalTpp = tppStoreService.findByCn(userDetails.getUsername());
            if (!optionalTpp.isPresent()) {
                LOGGER.debug("TPP not found. This TPP {} is not on board yet", userDetails.getUsername());
                return UserContext.create(userDetails.getUsername(), Collections.singletonList(OBRIRole.UNREGISTERED_TPP), UserContext.UserType.SOFTWARE_STATEMENT, certificatesChain);
            } else {
                List<GrantedAuthority> authorities = optionalTpp.get().getTypes().stream().map(OBRIRole::fromSoftwareStatementType).collect(Collectors.toList());
                return UserContext.create(optionalTpp.get().getClientId(), authorities, UserContext.UserType.OIDC_CLIENT, certificatesChain);
            }
        }
        return userDetails;
    }
}
