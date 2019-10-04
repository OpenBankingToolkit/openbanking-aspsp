/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.payment;

import com.forgerock.openbanking.common.model.openbanking.v3_1_1.payment.FRInternationalStandingOrderConsent3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class InternationalStandingOrderService implements PaymentService<FRInternationalStandingOrderConsent3> {
    private static final String BASE_RESOURCE_PATH = "/api/international-standing-orders/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    @Autowired
    public InternationalStandingOrderService(RestTemplate restTemplate,
                                             @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public void updatePayment(FRInternationalStandingOrderConsent3 consent) {
        log.debug("Update the consent in the store. {}", consent);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH, consent);
    }

    public FRInternationalStandingOrderConsent3 getPayment(String consentId) {
        log.debug("Getting consent for {}", consentId);
        return restTemplate.getForObject(rsStoreRoot + BASE_RESOURCE_PATH + consentId,
                FRInternationalStandingOrderConsent3.class);
    }
}
