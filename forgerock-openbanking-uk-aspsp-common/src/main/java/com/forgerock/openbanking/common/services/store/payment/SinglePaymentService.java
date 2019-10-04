/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.store.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Service
public class SinglePaymentService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SinglePaymentService.class);

    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    @Autowired
    private RestTemplate restTemplate;
    /**
     * Get payment
     *
     * @param paymentId the payment ID
     * @return the payment corresponding to the ID or null if not found
     */
    public FRPaymentSetup1 getPayment(String paymentId) {
        LOGGER.debug("Read the payment '{}' from the store", paymentId);
        // TODO change payment api
        return restTemplate.exchange(rsStoreRoot + "/api/payments/" + paymentId, HttpMethod.GET, null, FRPaymentSetup1.class).getBody();
    }

    public Collection<FRPaymentSetup1> getAllPaymentsInProcess() {
        LOGGER.debug("Read all the payments");
        ParameterizedTypeReference<List<FRPaymentSetup1>> ptr =
                new ParameterizedTypeReference<List<FRPaymentSetup1>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/payments/search/findByStatus"
        );
        builder.queryParam("status", ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRPaymentSetup1>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);

        return entity.getBody();
    }

    /**
     * update payment
     *
     * @param payment a payment
     */
    public void updatePayment(FRPaymentSetup1 payment) {
        LOGGER.debug("Update the payment in the store. FRPaymentConsent={}", payment);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/payments/"
        );
        URI uri = builder.build().encode().toUri();
        HttpEntity<FRPaymentSetup1> request = new HttpEntity<>(payment, new HttpHeaders());

        restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
    }
}
