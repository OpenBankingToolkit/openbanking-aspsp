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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_3;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmation1;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller("FundsConfirmationsApiV3.1.3")
public class FundsConfirmationsApiController implements FundsConfirmationsApi {

    private static final String DUMMY_FINANCIAL_ID = "REDUNDANT_ID";

    private final com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_2.FundsConfirmationsApiController previousVersionController;

    public FundsConfirmationsApiController(@Qualifier("FundsConfirmationsApiV3.1.2") com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_2.FundsConfirmationsApiController previousVersionController) {
        this.previousVersionController = previousVersionController;
    }

    @Override
    public ResponseEntity createFundsConfirmations(@Valid OBFundsConfirmation1 obFundsConfirmation1,
                                                   String authorization,
                                                   DateTime xFapiAuthDate,
                                                   String xFapiCustomerIpAddress,
                                                   String xFapiInteractionId,
                                                   String xCustomerUserAgent,
                                                   HttpServletRequest request,
                                                   Principal principal) {
        return previousVersionController.createFundsConfirmation(
                obFundsConfirmation1,
                DUMMY_FINANCIAL_ID,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }

    @Override
    public ResponseEntity getFundsConfirmationId(String fundsConfirmationId,
                                                 String authorization,
                                                 DateTime xFapiAuthDate,
                                                 String xFapiCustomerIpAddress,
                                                 String xFapiInteractionId,
                                                 String xCustomerUserAgent,
                                                 HttpServletRequest request,
                                                 Principal principal) {
        return previousVersionController.getFundsConfirmationId(
                fundsConfirmationId,
                DUMMY_FINANCIAL_ID,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }
}
