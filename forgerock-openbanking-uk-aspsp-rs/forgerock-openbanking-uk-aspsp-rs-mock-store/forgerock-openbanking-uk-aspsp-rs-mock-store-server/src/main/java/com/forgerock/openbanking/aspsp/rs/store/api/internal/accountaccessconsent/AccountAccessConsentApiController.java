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
package com.forgerock.openbanking.aspsp.rs.store.api.internal.accountaccessconsent;

import com.forgerock.openbanking.analytics.model.entries.ConsentStatusEntry;
import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accountsaccessconsents.FRAccountAccessConsentRepository;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent;
import com.forgerock.openbanking.common.repositories.customerinfo.FRCustomerInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
@Slf4j
public class AccountAccessConsentApiController implements AccountAccessConsentApi {

    private final FRAccountAccessConsentRepository accountAccessConsent1Repository;
    private final ConsentMetricService consentMetricService;
    private final FRCustomerInfoRepository customerInfoRepository;

    public AccountAccessConsentApiController(FRAccountAccessConsentRepository accountAccessConsent1Repository, ConsentMetricService consentMetricService, FRCustomerInfoRepository customerInfoRepository) {
        this.accountAccessConsent1Repository = accountAccessConsent1Repository;
        this.consentMetricService = consentMetricService;
        this.customerInfoRepository = customerInfoRepository;
    }

    @Override
    public ResponseEntity<FRAccountAccessConsent> save(
            @RequestBody FRAccountAccessConsent accountAccessConsent1
    ) {
        log.debug("Create an account access consent {}", accountAccessConsent1);
        consentMetricService.sendConsentActivity(new ConsentStatusEntry(accountAccessConsent1.getConsentId(), accountAccessConsent1.getStatus().name()));
        return new ResponseEntity<>(accountAccessConsent1Repository.save(accountAccessConsent1), HttpStatus.OK);
    }

    @Override
    public ResponseEntity read(
            @PathVariable("consentId") String consentId
    ) {
        log.debug("get an account access consent {}", consentId);
        Optional<FRAccountAccessConsent> isConsent = accountAccessConsent1Repository.findById(consentId);
        if (isConsent.isPresent()) {
            return ResponseEntity.ok(isConsent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
