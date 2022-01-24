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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomerInfoAccountAccessConsentDecisionFactory {

    private final AccountRequestStoreService accountRequestStoreService;

    public CustomerInfoAccountAccessConsentDecisionFactory(AccountRequestStoreService accountRequestStoreService) {
        this.accountRequestStoreService = accountRequestStoreService;
    }

    public ConsentDecisionDelegate create(final String intentId) throws OBErrorException {
        AccountRequest accountRequest = getAccountRequest(intentId);
        return new CustomerInfoAccountAccessConsentDecisionDelegate(accountRequestStoreService, accountRequest);
    }

    private AccountRequest getAccountRequest(String intentId) throws OBErrorException {
        Optional<AccountRequest> isAccountRequest = accountRequestStoreService.get(intentId);
        if (!isAccountRequest.isPresent()) {
            log.error("The AISP is referencing an customer info account access request {} that doesn't exist", intentId);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_UNKNOWN_ACCOUNT_REQUEST, intentId);
        }
        return isAccountRequest.get();
    }

}
