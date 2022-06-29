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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.vrp.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequest;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPConsentRequestTestDataFactory3_1_10;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory3_1_10;
import uk.org.openbanking.testsupport.vrp.OBVRPFundsConfirmationRequestTestDataFactory3_1_10;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultVrpExtensionValidatorTest {
    private final DefaultVrpExtensionValidator validator = new DefaultVrpExtensionValidator();

    @Test
    public void testDataFactoryConsentPassesValidation() throws Exception {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        validator.validateConsent(consent);
    }

    @Test
    public void periodLimitMustHaveMaximumOf2DecimalPlaces() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().getPeriodicLimits().get(0).setAmount("123.221");

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("Amount represented in field: 'PeriodicLimits.Amount' can have a maximum of 2 decimal places", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_AMOUNT_MAX_2_DP, obErrorException.getObriErrorType());
    }

    @Test
    public void vrpTypeMustBeSweeping() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().vrPType(List.of("blah"));

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("'VRPType' field only supports value 'UK.OBIE.VRPType.Sweeping'", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_VRP_TYPE_MUST_BE_SWEEPING, obErrorException.getObriErrorType());
    }

    @Test
    public void psuAuthenticationMethodMustBeSCA_NotRequired() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().psUAuthenticationMethods(List.of("blah"));

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("'PSUAuthenticationMethods' field only supports value 'UK.OBIE.SCANotRequired'", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_VRP_PSU_AUTHENTICATION_METHODS_INVALID, obErrorException.getObriErrorType());
    }

    @Test
    public void rejectValidToDateLessThanOrEqualToFromDate() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().validToDateTime(consent.getData().getControlParameters().getValidFromDateTime());

        OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("'ValidToDateTime' must be > 'ValidFromDateTime'", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_VRP_CONSENT_VALID_TO_DATE_INVALID, obErrorException.getObriErrorType());

        consent.getData().getControlParameters().validToDateTime(consent.getData().getControlParameters().getValidFromDateTime().minusDays(12));
        obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("'ValidToDateTime' must be > 'ValidFromDateTime'", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_VRP_CONSENT_VALID_TO_DATE_INVALID, obErrorException.getObriErrorType());
    }

    @Test
    public void rejectValidFromDateMoreThan31DaysInFuture() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().validFromDateTime(DateTime.now().plusDays(32));
        consent.getData().getControlParameters().validToDateTime(DateTime.now().plusYears(1));

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("'ValidFromDateTime' cannot be more than 31 days in the future", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_VRP_CONSENT_VALID_FROM_DATE_INVALID, obErrorException.getObriErrorType());
    }

    @Test
    public void rejectInvalidPaymentContextCodes() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();

        OBExternalPaymentContext1Code[] invalidPaymentContextCodes = new OBExternalPaymentContext1Code[] {
                OBExternalPaymentContext1Code.BILLPAYMENT, OBExternalPaymentContext1Code.ECOMMERCESERVICES, OBExternalPaymentContext1Code.BILLINGGOODSANDSERVICESINADVANCE
        };
        for (OBExternalPaymentContext1Code invalidPaymentContextCode : invalidPaymentContextCodes) {
            consent.getRisk().paymentContextCode(invalidPaymentContextCode);

            final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
            assertEquals("'Risk.PaymentContextCode' only supports values: ['PartyToParty', 'TransferToSelf']", obErrorException.getMessage());
            assertEquals(OBRIErrorType.REQUEST_VRP_RISK_PAYMENT_CONTEXT_CODE_INVALID, obErrorException.getObriErrorType());
        }
    }

    @Test
    public void rejectMaximumIndividualAmountMoreThan2Decimals() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().maximumIndividualAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("100.123").currency("GBP"));

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("Amount represented in field: 'MaximumIndividualAmount' can have a maximum of 2 decimal places", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_AMOUNT_MAX_2_DP, obErrorException.getObriErrorType());
    }

    @Test
    public void rejectMaximumIndividualAmountTooSmall() {
        final OBDomesticVRPConsentRequest consent = OBDomesticVRPConsentRequestTestDataFactory3_1_10.aValidOBDomesticVRPConsentRequest();
        consent.getData().getControlParameters().maximumIndividualAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("0.99").currency("GBP"));

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateConsent(consent));
        assertEquals("'MaximumIndividualAmount' must be >= 1.00", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_VRP_MAX_INDIVIDUAL_AMOUNT_TOO_SMALL, obErrorException.getObriErrorType());
    }

    @Test
    public void fundsConfirmationInstrumentAmountMustHaveMaximum2DecimalPlaces() {
        final OBVRPFundsConfirmationRequest fundsConfirmationRequest = OBVRPFundsConfirmationRequestTestDataFactory3_1_10.aValidOBVRPFundsConfirmationRequest("abc", "asdadasdasd", "123.333", "GBP");

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validateFundsConfirmationRequest(fundsConfirmationRequest));
        assertEquals("Amount represented in field: 'InstructedAmount' can have a maximum of 2 decimal places", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_AMOUNT_MAX_2_DP, obErrorException.getObriErrorType());
    }

    @Test
    public void testDataFactoryPaymentRequestPassesValidation() throws Exception {
        final OBDomesticVRPRequest domesticVRPRequest = OBDomesticVRPRequestTestDataFactory3_1_10.aValidOBDomesticVRPRequest();
        validator.validatePaymentRequest(domesticVRPRequest);
    }

    @Test
    public void paymentInstrumentAmountMustHaveMaximum2DecimalPlaces() {
        final OBDomesticVRPRequest domesticVRPRequest = OBDomesticVRPRequestTestDataFactory3_1_10.aValidOBDomesticVRPRequest();
        domesticVRPRequest.getData().getInstruction().instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().amount("1.123").currency("GBP"));

        final OBErrorException obErrorException = Assert.assertThrows(OBErrorException.class, () -> validator.validatePaymentRequest(domesticVRPRequest));
        assertEquals("Amount represented in field: 'InstructedAmount' can have a maximum of 2 decimal places", obErrorException.getMessage());
        assertEquals(OBRIErrorType.REQUEST_AMOUNT_MAX_2_DP, obErrorException.getObriErrorType());
    }
}
