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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.OBConstants;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;

public class PaymentsRequestPaymentIdEndpointWrapper extends RSEndpointWrapper<PaymentsRequestPaymentIdEndpointWrapper, PaymentsRequestPaymentIdEndpointWrapper.PaymentRestEndpointContent> {

    private static final Logger log = LoggerFactory.getLogger(PaymentsRequestPaymentIdEndpointWrapper.class);
    private FRPaymentConsent payment;

    public PaymentsRequestPaymentIdEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    public FRPaymentConsent getPayment() {
        return payment;
    }

    public PaymentsRequestPaymentIdEndpointWrapper payment(FRPaymentConsent payment) {
        this.payment = payment;
        return this;
    }

    @Override
    protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
        return main.run(tppId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        //super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS),
                Arrays.asList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL
                )
        );

        verifyMatlsFromAccessToken();
    }

    public interface PaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
