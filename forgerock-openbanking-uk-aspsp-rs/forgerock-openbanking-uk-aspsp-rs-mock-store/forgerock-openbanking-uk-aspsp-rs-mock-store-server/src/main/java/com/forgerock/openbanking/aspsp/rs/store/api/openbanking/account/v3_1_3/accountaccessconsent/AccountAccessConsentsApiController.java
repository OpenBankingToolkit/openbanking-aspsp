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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.accountaccessconsent;

import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBReadConsent1;
import uk.org.openbanking.datamodel.account.OBReadConsentResponse1;
import uk.org.openbanking.datamodel.account.OBReadRequest1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.AccountsUtil.DUMMY_FINANCIAL_ID;

@Controller("AccountAccessConsentsApiV3.1.3")
@Slf4j
public class AccountAccessConsentsApiController implements AccountAccessConsentsApi {

    // composition rather than inheritance to avoid "Ambiguous handler methods" error (since Spring doesn't know which interface to support)
    private final com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_2.accountaccessconsents.AccountAccessConsentApiController previousVersionController;

    public AccountAccessConsentsApiController(
            @Qualifier("AccountAccessConsentsApiV3.1.2") com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_2.accountaccessconsents.AccountAccessConsentApiController previousVersionController
    ) {
        this.previousVersionController = previousVersionController;
    }

    @Override
    public ResponseEntity<OBReadConsentResponse1> createAccountAccessConsent(@Valid OBReadConsent1 body,
                                                                             String authorization,
                                                                             String xJwsSignature,
                                                                             DateTime xFapiAuthDate,
                                                                             String xFapiCustomerIpAddress,
                                                                             String xFapiInteractionId,
                                                                             String xCustomerUserAgent,
                                                                             String aispId,
                                                                             HttpServletRequest request) throws OBErrorResponseException {
        OBReadRequest1 obReadRequest = (new OBReadRequest1())
                .data(body.getData())
                .risk(body.getRisk());

        return previousVersionController.createAccountAccessConsent(
                obReadRequest,
                DUMMY_FINANCIAL_ID,
                authorization,
                xJwsSignature,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                aispId,
                request);
    }

    @Override
    public ResponseEntity deleteAccountAccessConsent(String consentId,
                                                     String authorization,
                                                     DateTime xFapiAuthDate,
                                                     String xFapiCustomerIpAddress,
                                                     String xFapiInteractionId,
                                                     String xCustomerUserAgent
    ) throws OBErrorResponseException {
        return previousVersionController.deleteAccountConsent(consentId, authorization, DUMMY_FINANCIAL_ID, xCustomerUserAgent);
    }

    @Override
    public ResponseEntity<OBReadConsentResponse1> getAccountAccessConsent(String consentId,
                                                                          String authorization,
                                                                          DateTime xFapiAuthDate,
                                                                          String xFapiCustomerIpAddress,
                                                                          String xFapiInteractionId,
                                                                          String xCustomerUserAgent
    ) throws OBErrorResponseException {
        return previousVersionController.getAccountConsent(consentId, DUMMY_FINANCIAL_ID, authorization, xFapiAuthDate, xFapiCustomerIpAddress, xFapiInteractionId, xCustomerUserAgent);
    }
}
