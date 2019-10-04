/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.funds;

import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class FundsConfirmationService {

    private static final String BASE_RESOURCE_PATH = "/api/funds-confirmations/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    @Autowired
    public FundsConfirmationService(RestTemplate restTemplate, @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public void updateConsent(FRFundsConfirmationConsent1 consent) {
        log.debug("Update the consent in the store. {}", consent);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH, consent);
    }

    public FRFundsConfirmationConsent1 getConsent(String consentId) {
        log.debug("Getting consent for {}", consentId);
        return restTemplate.getForObject(rsStoreRoot + BASE_RESOURCE_PATH + consentId,
                FRFundsConfirmationConsent1.class);
    }
}
