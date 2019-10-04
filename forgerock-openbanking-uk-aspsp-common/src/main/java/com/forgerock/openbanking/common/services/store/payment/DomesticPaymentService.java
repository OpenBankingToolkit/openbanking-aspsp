/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class DomesticPaymentService implements PaymentService<FRDomesticConsent2> {
    private static final String BASE_RESOURCE_PATH = "/api/domestic-payments/";

    private String rsStoreRoot;
    private RestTemplate restTemplate;

    @Autowired
    public DomesticPaymentService(RestTemplate restTemplate,
                                  @Value("${rs-store.base-url}") String rsStoreRoot) {
        this.restTemplate = restTemplate;
        this.rsStoreRoot = rsStoreRoot;
    }

    public FRDomesticConsent2 getPayment(String consentId) {
        log.debug("Getting consent for {}", consentId);
        return restTemplate.getForObject(rsStoreRoot + BASE_RESOURCE_PATH + consentId,
                FRDomesticConsent2.class);
    }

    public Collection<FRDomesticConsent2> getAllPaymentsInProcess() {
        log.debug("Read all the payments");
        ParameterizedTypeReference<List<FRDomesticConsent2>> ptr =
                new ParameterizedTypeReference<List<FRDomesticConsent2>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + BASE_RESOURCE_PATH + "search/findByStatus"
        );
        builder.queryParam("status", ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRDomesticConsent2>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);

        return entity.getBody();
    }

    public void updatePayment(FRDomesticConsent2 consent) {
        log.debug("Update the consent in the store. {}", consent);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH, consent);
    }
}
