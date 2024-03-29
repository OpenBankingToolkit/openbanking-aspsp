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

import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.BalanceTransferPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.MoneyTransferPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.OBRisk1Validator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.PaymPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.PaymentConsent;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PaymentsApiEndpointWrapper extends RSEndpointWrapper<PaymentsApiEndpointWrapper, PaymentsApiEndpointWrapper.PaymentRestEndpointContent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsApiEndpointWrapper.class);
    private PaymentConsent payment;
    private final BalanceTransferPaymentValidator balanceTransferPaymentValidator;
    private final MoneyTransferPaymentValidator moneyTransferPaymentValidator;
    private final PaymPaymentValidator paymPaymentValidator;
    private final OBRisk1Validator riskValidator;
    private boolean isFundsConfirmationRequest;

    public PaymentsApiEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                      TppStoreService tppStoreService,
                                      BalanceTransferPaymentValidator balanceTransferPaymentValidator,
                                      MoneyTransferPaymentValidator moneyTransferPaymentValidator,
                                      PaymPaymentValidator paymPaymentValidator,
                                      OBRisk1Validator riskValidator) {
        super(RSEndpointWrapperService, tppStoreService);
        this.balanceTransferPaymentValidator = balanceTransferPaymentValidator;
        this.moneyTransferPaymentValidator = moneyTransferPaymentValidator;
        this.paymPaymentValidator = paymPaymentValidator;
        this.riskValidator = riskValidator;
        this.isFundsConfirmationRequest = false;
    }

    public PaymentsApiEndpointWrapper payment(PaymentConsent payment) {
        this.payment = payment;
        return this;
    }

    public PaymentsApiEndpointWrapper isFundsConfirmationRequest(boolean isFundsConfirmationRequest){
        this.isFundsConfirmationRequest = isFundsConfirmationRequest;
        return this;
    }

    @Override
    protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
        return main.run(oAuth2ClientId);
    }

    public void verifyConsentStatusForConfirmationOfFund() throws OBErrorException {
        switch (payment.getStatus()) {
            case AUTHORISED:
                //The customer has consent the payment, we can continue
                break;
            default:
                LOGGER.warn("Consent ({}) not authorised.", payment.getId());
                throw new OBErrorException(OBRIErrorType.CONSENT_STATUS_NOT_AUTHORISED,
                        payment.getStatus()
                );

        }
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();
        List grantTypes = Arrays.asList(OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        // the grant type for funds confirmation endpoint is different than the others payment endpoints
        if (isFundsConfirmationRequest) {
            grantTypes = Arrays.asList(
                    OIDCConstants.GrantType.AUTHORIZATION_CODE
            );
        }
        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS), grantTypes);
        verifyMatlsFromAccessToken();
    }

    public interface PaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }

    public void validateBalanceTransferPayment(OBWriteDomesticConsent2 consent) throws OBErrorException {
        balanceTransferPaymentValidator.validate(consent);
    }

    public void validateMoneyTransferPayment(OBWriteDomesticConsent2 consent) throws OBErrorException {
        moneyTransferPaymentValidator.validate(consent);
    }

    public void validatePaymPayment(OBWriteDomesticConsent2 consent) throws OBErrorException {
        paymPaymentValidator.validate(consent);
    }

    public void validateRisk(OBRisk1 risk) throws OBErrorException{
        if(riskValidator != null) {
            riskValidator.validate(risk);
        }else{
            String errorString = "validatePaymentCodeContext called but no validator present";
            log.error(errorString);
            throw new NullPointerException(errorString);
        }
    }
}
