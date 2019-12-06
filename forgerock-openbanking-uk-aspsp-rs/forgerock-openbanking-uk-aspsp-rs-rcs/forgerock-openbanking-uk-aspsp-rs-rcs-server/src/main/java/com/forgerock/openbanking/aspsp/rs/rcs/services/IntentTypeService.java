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
package com.forgerock.openbanking.aspsp.rs.rcs.services;

import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.ConsentDecisionDelegate;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.accounts.AccountAccessConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticpayments.DomesticPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticscheduledpayments.DomesticScheduledPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticstandingorders.DomesticStandingOrdersPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.filepayments.FilePaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.fundconfirmations.FundsConfirmationConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalpayment.InternationalPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalscheduledpayments.InternationalScheduledPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalstandingorders.InternationalStandingOrderConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.singlepayments.SinglePaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details.*;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class IntentTypeService {

    @Autowired
    private RCSErrorService rcsErrorService;
    @Autowired
    private RCSAccountDetailsApi rcsAccountDetailsApi;
    @Autowired
    private RCSSinglePaymentDetailsApi rcsSinglePaymentDetailsApi;
    @Autowired
    private RCSDomesticPaymentDetailsApi rcsDomesticPaymentDetailsApi;
    @Autowired
    private RCSDomesticSchedulePaymentDetailsApi rcsDomesticSchedulePaymentDetailsApi;
    @Autowired
    private RCSDomesticStandingOrderDetailsApi rcsDomesticStandingOrderDetailsApi;
    @Autowired
    private RCSInternationalPaymentDetailsApi rcsInternationalPaymentDetailsApi;
    @Autowired
    private RCSInternationalStandingOrderPaymentDetailsApi rcsInternationalStandingOrderPaymentDetailsApi;
    @Autowired
    private RCSInternationalScheduledPaymentDetailsApi rcsInternationalScheduledPaymentDetailsApi;
    @Autowired
    private RCSFundsConfirmationDetailsApi rcsFundsConfirmationDetailsApi;
    @Autowired
    private RCSFilePaymentDetailsApi rcsFilePaymentDetailsApi;

    @Autowired
    private SinglePaymentConsentDecisionFactory singlePaymentConsentDecisionService;
    @Autowired
    private AccountAccessConsentDecisionFactory accountAccessConsentDecisionApiController;
    @Autowired
    private InternationalPaymentConsentDecisionFactory internationalPaymentConsentDecisionFactory;
    @Autowired
    private DomesticPaymentConsentDecisionFactory domesticPaymentConsentDecisionFactory;
    @Autowired
    private DomesticScheduledPaymentConsentDecisionFactory domesticScheduledPaymentConsentDecisionFactory;
    @Autowired
    private DomesticStandingOrdersPaymentConsentDecisionFactory domesticStandingOrdersPaymentConsentDecisionFactory;
    @Autowired
    private InternationalScheduledPaymentConsentDecisionFactory internationalScheduledPaymentConsentDecisionFactory;
    @Autowired
    private InternationalStandingOrderConsentDecisionFactory internationalStandingOrderConsentDecisionFactory;
    @Autowired
    private FundsConfirmationConsentDecisionFactory fundsConfirmationConsentDecisionFactory;
    @Autowired
    private FilePaymentConsentDecisionFactory filePaymentConsentDecisionFactory;

    public ConsentDecisionDelegate getConsentDecision(String intentId) throws OBErrorException {
        switch (IntentType.identify(intentId)) {
            case ACCOUNT_REQUEST:
            case ACCOUNT_ACCESS_CONSENT:
                log.debug("It's an account consent request");
                return accountAccessConsentDecisionApiController.create(intentId);
            case PAYMENT_SINGLE_REQUEST:
                log.debug("It's a payment consent request");
            return singlePaymentConsentDecisionService.create(intentId);
            case PAYMENT_INTERNATIONAL_CONSENT:
                log.debug("It's a international payment consent request");
                return internationalPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_DOMESTIC_CONSENT:
                log.debug("It's a domestic payment consent request");
                return domesticPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_DOMESTIC_SCHEDULED_CONSENT:
                log.debug("It's a domestic payment consent request");
                return domesticScheduledPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT:
                log.debug("It's a domestic payment consent request");
                return domesticStandingOrdersPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT:
                log.debug("It's an international scheduled payment consent request");
                return internationalScheduledPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT:
                log.debug("It's an international standing order consent request");
                return internationalStandingOrderConsentDecisionFactory.create(intentId);
            case PAYMENT_FILE_CONSENT:
                log.debug("It's an file payment consent request");
                return filePaymentConsentDecisionFactory.create(intentId);
            case FUNDS_CONFIRMATION_CONSENT:
                return fundsConfirmationConsentDecisionFactory.create(intentId);
            default:
                log.error("Invalid intent ID");
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID,
                        "Invalid intent ID '" + intentId + "'");
        }
    }


    public ResponseEntity consentDetails(String intentId,
                                         String consentRequestJwt,
                                         List<FRAccountWithBalance> accounts,
                                         String username,
                                         String clientId) throws OBErrorException {
        switch (IntentType.identify(intentId)) {
            case ACCOUNT_REQUEST:
            case ACCOUNT_ACCESS_CONSENT:
                log.debug("It's an account consent request");
                return rcsAccountDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_SINGLE_REQUEST:
                log.debug("It's a payment consent request");
                return rcsSinglePaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_DOMESTIC_CONSENT:
                log.debug("It's a domestic payment consent request");
                return rcsDomesticPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_INTERNATIONAL_CONSENT:
                log.debug("It's an international payment consent request");
                return rcsInternationalPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_DOMESTIC_SCHEDULED_CONSENT:
                log.debug("It's a domestic scheduled payment consent request");
                return rcsDomesticSchedulePaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT:
                log.debug("It's a domestic standing order payment consent request");
                return rcsDomesticStandingOrderDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT:
                log.debug("It's an international scheduled payment consent request");
                return rcsInternationalScheduledPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT:
                log.debug("It's an international standing order consent request");
                return rcsInternationalStandingOrderPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_FILE_CONSENT:
                log.debug("It's an file payment consent request");
                return rcsFilePaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case FUNDS_CONFIRMATION_CONSENT:
                log.debug("It's a funds confirmation consent request");
                return rcsFundsConfirmationDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            default:
                log.error("Invalid intent ID");
                return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID,
                        "Invalid intent ID '" + intentId + "'");
        }
    }
}