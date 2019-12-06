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
package com.forgerock.openbanking.aspsp.as.service;

import com.forgerock.openbanking.aspsp.as.moddel.CdrScope;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.model.Tpp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.org.openbanking.datamodel.account.*;

import java.util.Collections;
import java.util.List;

@Service
public class AccountRequestService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${rs-store.base-url}")
    private String rsStoreRoot;

    public OBReadConsentResponse1 registerConsent(List<String> jwtScopes, String clientId) {
        OBReadData1 obReadData1 = new OBReadData1();

        if (jwtScopes.contains(CdrScope.BANK_ACCOUNT_DETAILS_READ)) {
            obReadData1.addPermissionsItem(OBExternalPermissions1Code.READACCOUNTSDETAIL);
            obReadData1.addPermissionsItem(OBExternalPermissions1Code.READBALANCES);
        } else if (jwtScopes.contains(CdrScope.BANK_ACCOUNT_BASIC_READ)) {
            obReadData1.addPermissionsItem(OBExternalPermissions1Code.READACCOUNTSBASIC);
            obReadData1.addPermissionsItem(OBExternalPermissions1Code.READBALANCES);
        }

        if (jwtScopes.contains(CdrScope.BANK_TRANSACTIONS_READ)) {
            obReadData1
                    .addPermissionsItem(OBExternalPermissions1Code.READTRANSACTIONSCREDITS)
                    .addPermissionsItem(OBExternalPermissions1Code.READTRANSACTIONSDEBITS)
                    .addPermissionsItem(OBExternalPermissions1Code.READTRANSACTIONSDETAIL);
        }
        if (jwtScopes.contains(CdrScope.BANK_PAYEE_READ)) {
            obReadData1
                    .addPermissionsItem(OBExternalPermissions1Code.READBENEFICIARIESDETAIL);
        }
        if (jwtScopes.contains(CdrScope.BANK_REGULAR_PAYMENT_READ)) {
            obReadData1
                    .addPermissionsItem(OBExternalPermissions1Code.READSCHEDULEDPAYMENTSDETAIL)
                    .addPermissionsItem(OBExternalPermissions1Code.READDIRECTDEBITS);
        }
        if (jwtScopes.contains(CdrScope.COMMON_CUSTOMER_BASIC_READ)) {
            obReadData1
                    .addPermissionsItem(OBExternalPermissions1Code.READPARTY)
                    .addPermissionsItem(OBExternalPermissions1Code.READPARTYPSU);
        }
        if (jwtScopes.contains(CdrScope.COMMON_CUSTOMER_DETAILS_READ)) {
            obReadData1
                    .addPermissionsItem(OBExternalPermissions1Code.READPARTY)
                    .addPermissionsItem(OBExternalPermissions1Code.READPARTYPSU);
        }

        OBReadConsent1 obReadConsent1 = new OBReadConsent1();
        obReadConsent1.setRisk(new OBRisk2());
        obReadConsent1.setData(obReadData1);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("x-fapi-financial-id", "");
        httpHeaders.set("Authorization", "");
        httpHeaders.set("x-ob-aisp_id", clientId);


        HttpEntity<OBReadConsent1> request = new HttpEntity<>(obReadConsent1, httpHeaders);
        return restTemplate.postForObject(rsStoreRoot + "/open-banking/v3.0/aisp/account-access-consents", request, OBReadConsentResponse1.class);
    }
}
