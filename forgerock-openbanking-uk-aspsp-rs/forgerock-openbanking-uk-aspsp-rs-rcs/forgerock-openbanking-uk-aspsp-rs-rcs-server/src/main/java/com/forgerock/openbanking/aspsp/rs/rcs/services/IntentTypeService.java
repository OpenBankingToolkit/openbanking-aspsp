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
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.customerinfo.CustomerInfoAccountAccessConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticpayments.DomesticPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticscheduledpayments.DomesticScheduledPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.domesticstandingorders.DomesticStandingOrdersPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.filepayments.FilePaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.fundconfirmations.FundsConfirmationConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalpayment.InternationalPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalscheduledpayments.InternationalScheduledPaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.internationalstandingorders.InternationalStandingOrderConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.singlepayments.SinglePaymentConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions.vrp.DomesticVRPConsentDecisionFactory;
import com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details.*;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
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
    private RCSVrpPaymentDetailsApi rcsVrpPaymentDetailsApi;
    @Autowired
    private RCSCustomerInfoDetailsApi rcsCustomerInfoDetailsApi;

    @Autowired
    private SinglePaymentConsentDecisionFactory singlePaymentConsentDecisionService;
    @Autowired
    private AccountAccessConsentDecisionFactory accountAccessConsentDecisionFactory;
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
    @Autowired
    private DomesticVRPConsentDecisionFactory domesticVRPConsentDecisionFactory;
    @Autowired
    private CustomerInfoAccountAccessConsentDecisionFactory customerInfoAccountAccessConsentDecisionFactory;

    public ConsentDecisionDelegate getConsentDecision(String intentId) throws OBErrorException {
        switch (IntentType.identify(intentId)) {
            case ACCOUNT_REQUEST:
            case ACCOUNT_ACCESS_CONSENT:
            case CUSTOMER_INFO_CONSENT:
                log.debug("It's an account consent decision request");
                return accountAccessConsentDecisionFactory.create(intentId);
            case PAYMENT_SINGLE_REQUEST:
                log.debug("It's a payment consent decision request");
                return singlePaymentConsentDecisionService.create(intentId);
            case PAYMENT_INTERNATIONAL_CONSENT:
                log.debug("It's a international payment consent decision request");
                return internationalPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_DOMESTIC_CONSENT:
                log.debug("It's a domestic payment consent decision request");
                return domesticPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_DOMESTIC_SCHEDULED_CONSENT:
                log.debug("It's a domestic payment consent decision request");
                return domesticScheduledPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT:
                log.debug("It's a domestic payment consent decision request");
                return domesticStandingOrdersPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT:
                log.debug("It's an international scheduled payment consent decision request");
                return internationalScheduledPaymentConsentDecisionFactory.create(intentId);
            case PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT:
                log.debug("It's an international standing order consent decision request");
                return internationalStandingOrderConsentDecisionFactory.create(intentId);
            case PAYMENT_FILE_CONSENT:
                log.debug("It's an file payment consent decision request");
                return filePaymentConsentDecisionFactory.create(intentId);
            case FUNDS_CONFIRMATION_CONSENT:
                log.debug("It's an funds confirmation consent decision request");
                return fundsConfirmationConsentDecisionFactory.create(intentId);
            case DOMESTIC_VRP_PAYMENT_CONSENT:
                log.debug("It's a VRP payment consent decision request");
                return domesticVRPConsentDecisionFactory.create(intentId);
            case CUSTOMER_INFO_CONSENT:
                log.debug("It's an customer info account consent decision request");
                return customerInfoAccountAccessConsentDecisionFactory.create(intentId);
            default:
                log.error("Invalid intent ID");
                throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID,
                        "Invalid intent ID '" + intentId + "'");
        }
    }


    public ResponseEntity consentDetails(String intentId,
                                         String consentRequestJwt,
                                         List<AccountWithBalance> accounts,
                                         String username,
                                         String clientId) throws OBErrorException {
        switch (IntentType.identify(intentId)) {
            case ACCOUNT_REQUEST:
            case ACCOUNT_ACCESS_CONSENT:
                log.debug("It's an account consent details request");
                return rcsAccountDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_SINGLE_REQUEST:
                log.debug("It's a payment consent details request");
                return rcsSinglePaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_DOMESTIC_CONSENT:
                log.debug("It's a domestic payment consent details request");
                return rcsDomesticPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_INTERNATIONAL_CONSENT:
                log.debug("It's an international payment consent details request");
                return rcsInternationalPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_DOMESTIC_SCHEDULED_CONSENT:
                log.debug("It's a domestic scheduled payment consent details request");
                return rcsDomesticSchedulePaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT:
                log.debug("It's a domestic standing order payment consent details request");
                return rcsDomesticStandingOrderDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT:
                log.debug("It's an international scheduled payment consent details request");
                return rcsInternationalScheduledPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT:
                log.debug("It's an international standing order consent details request");
                return rcsInternationalStandingOrderPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case PAYMENT_FILE_CONSENT:
                log.debug("It's an file payment consent details request");
                return rcsFilePaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case FUNDS_CONFIRMATION_CONSENT:
                log.debug("It's a funds confirmation consent details request");
                return rcsFundsConfirmationDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case DOMESTIC_VRP_PAYMENT_CONSENT:
                log.debug("It's a VRP payment consent details request");
                return rcsVrpPaymentDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            case CUSTOMER_INFO_CONSENT:
                log.debug("It's a Customer Info account consent details request");
                return rcsCustomerInfoDetailsApi.consentDetails(consentRequestJwt, accounts, username, intentId, clientId);
            default:
                log.error("Invalid intent ID");
                return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID,
                        "Invalid intent ID '" + intentId + "'");
        }
    }
}
