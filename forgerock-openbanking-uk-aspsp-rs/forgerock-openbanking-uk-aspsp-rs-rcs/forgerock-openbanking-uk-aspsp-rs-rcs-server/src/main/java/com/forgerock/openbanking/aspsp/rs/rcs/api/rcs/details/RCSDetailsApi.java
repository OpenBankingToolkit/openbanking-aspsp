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


import com.forgerock.openbanking.common.model.openbanking.forgerock.AccountWithBalance;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RCSDetailsApi {
    Logger log = LoggerFactory.getLogger(RCSDetailsApi.class);

    ResponseEntity consentDetails(String remoteConsentRequest, List<AccountWithBalance> accounts, String username,
                                  String consentId, String clientId) throws OBErrorException;

    /**
     * TPP making the consent request to the user should be same one that created the new payment consent in RS
     */
    default void verifyTppCreatedPayment(String clientIdRequestingConsent, String clientIdThatCreatedPayment, String consentId) throws OBErrorException {
        //Verify the pisp is the same than the one that created this payment
        log.debug("PISP that is requesting consent in RCS Details service => '{}'", clientIdRequestingConsent);
        log.debug("PISP that created the payment consent in RS-STORE => '{}'", clientIdThatCreatedPayment);
        if (!clientIdRequestingConsent.equals(clientIdThatCreatedPayment)) {
            log.error("The PISP with client id: '{}' created this payment request '{}' but it's a PISP with client id '{}' that is trying to get" +
                    " consent for it.", clientIdThatCreatedPayment, consentId, clientIdRequestingConsent);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_PAYMENT_REQUEST, clientIdThatCreatedPayment, consentId, clientIdRequestingConsent);
        }
    }
}
