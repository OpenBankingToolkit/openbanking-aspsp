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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.data.FRCustomerInfo;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccountAccessConsent;
import com.forgerock.openbanking.common.model.rcs.consentdetails.CustomerInfoConsentDetails;
import com.forgerock.openbanking.common.repositories.customerinfo.FRCustomerInfoRepository;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class RCSCustomerInfoDetailsApi implements RCSDetailsApi {

    private final RCSErrorService rcsErrorService;
    private final TppStoreService tppStoreService;
    private final AccountRequestStoreService accountRequestStoreService;
    private final FRCustomerInfoRepository customerInfoRepository;

    public RCSCustomerInfoDetailsApi(RCSErrorService rcsErrorService, TppStoreService tppStoreService, AccountRequestStoreService accountRequestStoreService, FRCustomerInfoRepository customerInfoRepository) {
        this.rcsErrorService = rcsErrorService;
        this.tppStoreService = tppStoreService;
        this.accountRequestStoreService = accountRequestStoreService;
        this.customerInfoRepository = customerInfoRepository;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<AccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a Customer info account consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The Customer info account consent id '{}'", consentId);

        Optional<AccountRequest> isCustomerInfoConsent = accountRequestStoreService.get(consentId);
        if (!isCustomerInfoConsent.isPresent()) {
            log.error("The AISP '{}' is referencing an customer info account request {} that doesn't exist", clientId,
                    consentId);
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_UNKNOWN_ACCOUNT_REQUEST,
                    clientId, consentId);
        }
        FRAccountAccessConsent customerInfoAccountConsent = (FRAccountAccessConsent) isCustomerInfoConsent.get();

        //Verify the aisp is the same than the one that created this customer info accountRequest ^
        if (!clientId.equals(customerInfoAccountConsent.getClientId())) {
            log.error("The AISP '{}' created the customer info account request '{}' but it's AISP '{}' that is " +
                    "trying to get consent for it.", customerInfoAccountConsent.getClientId(), consentId, clientId);
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_CONSENT,
                    customerInfoAccountConsent.getClientId(), clientId, consentId);
        }


        Optional<Tpp> isTpp = tppStoreService.findById(customerInfoAccountConsent.getAispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this customer info account consent id '{}' " +
                    "doesn't exist anymore.", customerInfoAccountConsent.getAispId(), clientId, customerInfoAccountConsent.getId());
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, customerInfoAccountConsent.getId());
        }
        Tpp tpp = isTpp.get();

        log.debug("Populate the customer info model with details data");
        customerInfoAccountConsent.setUserId(username);
        accountRequestStoreService.save(customerInfoAccountConsent);

        log.debug("Populate the model with the customer info and consent data");
        log.debug("get the customer info to add it in account consent data.");
        FRCustomerInfo customerInfo = customerInfoRepository.findByUserID(username);
        log.debug("customer info data {}", customerInfo);
        if (customerInfo == null) {
            return rcsErrorService.invalidConsentError(remoteConsentRequest, new OBErrorException(OBRIErrorType.CUSTOMER_INFO_NOT_FOUND));
        }

        customerInfoAccountConsent.setCustomerInfo(customerInfo);
        log.debug("customer info to added in account consent data {}", consentId);

        return ok(CustomerInfoConsentDetails.builder()
                .username(username)
                .merchantName(customerInfoAccountConsent.getAispName())
                .logo(tpp.getLogo())
                .clientId(clientId)
                .customerInfo(customerInfoAccountConsent.getCustomerInfo())
                .build());
    }
}
