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
package com.forgerock.openbanking.common.services.store.vrp;

import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DomesticVrpPaymentConsentService implements VrpPaymentServiceConsent<FRDomesticVRPConsent> {

    private static final String BASE_RESOURCE_PATH = "/api/domestic-vrp-payments/";

    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    private final RestTemplate restTemplate;

    public DomesticVrpPaymentConsentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public FRDomesticVRPConsent getVrpPaymentConsent(String consentId) {
        log.debug("Getting VRP consent for {}", consentId);
        return restTemplate.getForObject(rsStoreRoot + BASE_RESOURCE_PATH + consentId,
                FRDomesticVRPConsent.class);
    }

    @Override
    public void updateVrpPaymentConsent(FRDomesticVRPConsent consent) {
        log.debug("Update the VRP consent in the store. {}", consent);
        restTemplate.put(rsStoreRoot + BASE_RESOURCE_PATH, consent);
    }
}
