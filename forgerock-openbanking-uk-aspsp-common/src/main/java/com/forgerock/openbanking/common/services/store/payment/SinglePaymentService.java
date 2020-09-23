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
package com.forgerock.openbanking.common.services.store.payment;

import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRPaymentSetup;
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
    public FRPaymentSetup getPayment(String paymentId) {
        LOGGER.debug("Read the payment '{}' from the store", paymentId);
        // TODO change payment api
        return restTemplate.exchange(rsStoreRoot + "/api/payments/" + paymentId, HttpMethod.GET, null, FRPaymentSetup.class).getBody();
    }

    public Collection<FRPaymentSetup> getAllPaymentsInProcess() {
        LOGGER.debug("Read all the payments");
        ParameterizedTypeReference<List<FRPaymentSetup>> ptr =
                new ParameterizedTypeReference<List<FRPaymentSetup>>() {};
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/payments/search/findByStatus"
        );
        builder.queryParam("status", ConsentStatusCode.ACCEPTEDSETTLEMENTINPROCESS);
        URI uri = builder.build().encode().toUri();
        ResponseEntity<List<FRPaymentSetup>> entity = restTemplate.exchange(uri, HttpMethod.GET, null, ptr);

        return entity.getBody();
    }

    /**
     * update payment
     *
     * @param payment a payment
     */
    public void updatePayment(FRPaymentSetup payment) {
        LOGGER.debug("Update the payment in the store. FRPaymentConsent={}", payment);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                rsStoreRoot + "/api/payments/"
        );
        URI uri = builder.build().encode().toUri();
        HttpEntity<FRPaymentSetup> request = new HttpEntity<>(payment, new HttpHeaders());

        restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);
    }
}
