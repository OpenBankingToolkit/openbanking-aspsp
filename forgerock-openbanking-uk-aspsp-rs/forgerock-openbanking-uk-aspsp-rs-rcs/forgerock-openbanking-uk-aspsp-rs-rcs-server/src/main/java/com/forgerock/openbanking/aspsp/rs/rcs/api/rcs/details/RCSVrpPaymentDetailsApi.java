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
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RCSVrpPaymentDetailsApi implements RCSDetailsApi {

    private final RCSErrorService rcsErrorService;

    public RCSVrpPaymentDetailsApi(RCSErrorService rcsErrorService) {
        this.rcsErrorService = rcsErrorService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<AccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a VRP consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The VRP payment consent id '{}'", consentId);

        log.debug("Populate the model with the VRP payment and consent data");

        return null;
    }

}