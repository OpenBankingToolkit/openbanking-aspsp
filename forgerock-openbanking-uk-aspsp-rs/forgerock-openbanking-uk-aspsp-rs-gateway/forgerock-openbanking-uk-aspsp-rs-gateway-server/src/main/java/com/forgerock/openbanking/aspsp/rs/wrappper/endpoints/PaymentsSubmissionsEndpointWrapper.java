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
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRPaymentRisk;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.PaymentConsent;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

public class PaymentsSubmissionsEndpointWrapper extends RSEndpointWrapper<PaymentsSubmissionsEndpointWrapper, PaymentsSubmissionsEndpointWrapper.PaymentRestEndpointContent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsSubmissionsEndpointWrapper.class);

    private PaymentConsent payment;

    public PaymentsSubmissionsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                              TppStoreService tppStoreService) {
        super(RSEndpointWrapperService, tppStoreService);
    }

    public PaymentConsent getPayment() {
        return payment;
    }

    public PaymentsSubmissionsEndpointWrapper payment(PaymentConsent payment) {
        this.payment = payment;
        return this;
    }

    @Override
    protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
        return main.run(oAuth2ClientId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS),
                Arrays.asList(
                        OIDCConstants.GrantType.AUTHORIZATION_CODE,
                        OIDCConstants.GrantType.HEADLESS_AUTH
                )
        );

        verifyMatlsFromAccessToken();

    }

    public void verifyPaymentIdWithAccessToken() throws OBErrorException {
        try {
            String paymentIdFromAccessToken = rsEndpointWrapperService.accessTokenService.getIntentId(accessToken);

            LOGGER.info("Payment id {} associated with the access token", payment.getId());
            if (!payment.getId().equals(paymentIdFromAccessToken)) {
                LOGGER.error("Payment id {} associated with the access token is not the same than the payment id {} " +
                        "associated with the payment submission", paymentIdFromAccessToken, payment.getId());
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_PAYMENT_ID,
                        paymentIdFromAccessToken, payment.getId()
                );
            }
        } catch (ParseException | IOException e) {
            LOGGER.error("Can't retrieve claims from access token: {}", accessToken);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_FORMAT);
        }
    }

    public void verifyPaymentStatus() throws OBErrorException {
        switch (payment.getStatus()) {
            case ACCEPTEDTECHNICALVALIDATION:
                LOGGER.warn("Payment ({}) not consented by PSU yet.", payment.getId());
                throw new OBErrorException(OBRIErrorType.PAYMENT_WAITING_PSU_CONSENT,
                        payment.getStatus()
                );
            case PENDING:
                LOGGER.warn("Payment ({}) still pending.", payment.getId());
                throw new OBErrorException(OBRIErrorType.PAYMENT_STILL_PENDING,
                        payment.getStatus()
                );
            case REJECTED:
                LOGGER.warn("Payment ({}) was rejected.", payment.getId());
                throw new OBErrorException(OBRIErrorType.PAYMENT_REJECTED,
                        payment.getStatus()
                );
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
                // This may be an idempotent request for a submission that ios already accepted so valid here but will be verified in idempotency key check
            case ACCEPTEDCUSTOMERPROFILE:
            case AUTHORISED:
                //The customer has consent the payment, we can continue
        }
    }

    public void verifyRiskAndInitiation(Object initiation, FRPaymentRisk risk) throws OBErrorException {
        //Verify risk and initiation are equals to initial request
        verifyInitiation(initiation);
        verifyRisk(risk);
    }

    public void verifyRisk(FRPaymentRisk risk) throws OBErrorException {
        //Verify risk are equals to initial request
        if (!payment.getRisk().equals(risk)) {
            LOGGER.debug("Risk received doesn't match payment setup request. Received:'{}' , expected:'{}'",
                    risk, payment.getRisk());
            throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_RISK);
        }
    }

    public void verifyInitiation(Object initiation) throws OBErrorException {
        //Verify initiation are equals to initial request
        if (!payment.getInitiation().equals(initiation)) {
            LOGGER.debug("Initiation received doesn't match payment setup request. Received:'{}' , expected:'{}'",
                    initiation, payment.getInitiation());
            throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_INITIATION);
        }
    }

    public interface PaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
