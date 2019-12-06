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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.rcs.consentdecision.AccountConsentDecision;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class AccountAccessConsentDecisionDelegate implements ConsentDecisionDelegate {

    private FRAccountRequest accountRequest;
    private AccountStoreService accountsService;
    private ObjectMapper objectMapper;
    private AccountRequestStoreService accountRequestStoreService;

    AccountAccessConsentDecisionDelegate(AccountStoreService accountsService,
                                         ObjectMapper objectMapper,
                                         AccountRequestStoreService accountRequestStoreService,
                                         FRAccountRequest accountRequest) {
        this.accountsService = accountsService;
        this.objectMapper = objectMapper;
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
        AccountConsentDecision accountConsentDecision = objectMapper.readValue(consentDecisionSerialised, AccountConsentDecision.class);

        if (decision) {
            List<FRAccount2> accounts = accountsService.get(accountRequest.getUserId());
            List<String> accountsId = accounts.stream().map(FRAccount::getId).collect(Collectors.toList());
            if (!accountsId.containsAll(accountConsentDecision.getSharedAccounts())) {
                log.error("The PSU {} is trying to share an account '{}' he doesn't own. List of his accounts '{}'",
                        accountRequest.getUserId(),
                        accountsId, accountConsentDecision.getSharedAccounts());
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_DECISION_INVALID_ACCOUNT,
                        accountRequest.getUserId(),
                        accountsId, accountConsentDecision.getSharedAccounts());
            }
            accountRequest.setAccountIds(accountConsentDecision.getSharedAccounts());
            accountRequest.setStatus(OBExternalRequestStatus1Code.AUTHORISED);
            accountRequestStoreService.save(accountRequest);
        } else {
            log.debug("The account request {} has been deny", accountRequest.getId());
            accountRequest.setStatus(OBExternalRequestStatus1Code.REJECTED);
            accountRequestStoreService.save(accountRequest);
        }
    }

    @Override
    public void autoaccept(List<FRAccount2> accounts, String username) {
        accountRequest.setUserId(username);
        accountRequest.setAccountIds(accounts.stream().map(FRAccount2::getId).collect(Collectors.toList()));
        accountRequest.setStatus(OBExternalRequestStatus1Code.AUTHORISED);
        accountRequestStoreService.save(accountRequest);
    }
}
