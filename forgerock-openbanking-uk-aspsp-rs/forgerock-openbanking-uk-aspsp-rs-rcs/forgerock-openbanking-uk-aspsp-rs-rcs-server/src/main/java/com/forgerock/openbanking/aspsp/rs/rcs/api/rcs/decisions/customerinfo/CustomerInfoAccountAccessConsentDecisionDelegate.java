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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.customerinfo;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalRequestStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;

@Slf4j
class CustomerInfoAccountAccessConsentDecisionDelegate implements ConsentDecisionDelegate {

    private AccountRequest accountRequest;
    private AccountRequestStoreService accountRequestStoreService;

    CustomerInfoAccountAccessConsentDecisionDelegate(AccountRequestStoreService accountRequestStoreService,
                                                     AccountRequest accountRequest) {
        this.accountRequestStoreService = accountRequestStoreService;
        this.accountRequest = accountRequest;
    }

    @Override
    public String getTppIdBehindConsent() {
        return accountRequest.getAispId();
    }

    @Override
    public String getUserIDBehindConsent() {
        return accountRequest.getUserId();
    }

    @Override
    public void consentDecision(String consentDecisionSerialised, boolean decision) throws IOException, OBErrorException {
        FRExternalRequestStatusCode decisionStatusCode = decision ? FRExternalRequestStatusCode.AUTHORISED : FRExternalRequestStatusCode.REJECTED;
        log.debug("The customer info account access consent request {} has been {}", accountRequest.getId(), decisionStatusCode.getValue());
        accountRequest.setStatus(decisionStatusCode);
        accountRequest.setStatusUpdateDateTime(DateTime.now());
        accountRequestStoreService.save(accountRequest);
    }

    @Override
    public void autoaccept(List<FRAccount> accounts, String username) {
        log.debug("The customer info account access consent request {} has been autoaccept and {}",
                accountRequest.getId(), FRExternalRequestStatusCode.AUTHORISED.getValue());
        accountRequest.setUserId(username);
        accountRequest.setStatus(FRExternalRequestStatusCode.AUTHORISED);
        accountRequest.setStatusUpdateDateTime(DateTime.now());
        accountRequestStoreService.save(accountRequest);
    }
}
