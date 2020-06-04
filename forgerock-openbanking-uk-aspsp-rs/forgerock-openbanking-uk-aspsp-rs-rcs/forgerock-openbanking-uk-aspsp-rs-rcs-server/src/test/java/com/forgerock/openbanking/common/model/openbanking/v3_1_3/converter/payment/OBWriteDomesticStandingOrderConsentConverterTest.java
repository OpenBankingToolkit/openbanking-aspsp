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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import org.joda.time.DateTime;
import org.junit.Test;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent3DataAuthorisation.AuthorisationTypeEnum;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrderConsent4Data.PermissionEnum;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBWriteDomesticStandingOrderConsentConverter.toOBWriteDomesticStandingOrderConsent3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.DateTime.now;

/**
 * Unit test for {@link OBWriteDomesticStandingOrderConsentConverter}.
 */
public class OBWriteDomesticStandingOrderConsentConverterTest {

    private static final DateTime NOW = now();
    private static final String GBP = "GBP";
    private static final String AMOUNT = "10.01";

    @Test
    public void shouldConvertToOBWriteDomesticStandingOrderConsent3() {
        // Given
        OBWriteDomesticStandingOrderConsent4Data data = obWriteDomesticStandingOrderConsent4Data();
        OBRisk1 risk = new OBRisk1();
        OBWriteDomesticStandingOrderConsent4 obWriteDomesticStandingOrderConsent4 = new OBWriteDomesticStandingOrderConsent4();
        obWriteDomesticStandingOrderConsent4.data(data);
        obWriteDomesticStandingOrderConsent4.risk(risk);
        OBWriteDataDomesticStandingOrderConsent3 expectedData = expectedObWriteDataDomesticStandingOrderConsent3(data);

        // When
        OBWriteDomesticStandingOrderConsent3 obWriteDomesticStandingOrderConsent3 = toOBWriteDomesticStandingOrderConsent3(obWriteDomesticStandingOrderConsent4);

        // Then
        assertThat(obWriteDomesticStandingOrderConsent3.getData()).isEqualTo(expectedData);
        assertThat(obWriteDomesticStandingOrderConsent3.getRisk()).isEqualTo(risk);
    }

    private OBWriteDomesticStandingOrderConsent4Data obWriteDomesticStandingOrderConsent4Data() {
        return (new OBWriteDomesticStandingOrderConsent4Data())
                .permission(PermissionEnum.CREATE)
                .initiation(obWriteDomesticStandingOrder3DataInitiation())
                .authorisation(obWriteDomesticConsent3DataAuthorisation());
    }

    private OBWriteDomesticStandingOrder3DataInitiation obWriteDomesticStandingOrder3DataInitiation() {
        return (new OBWriteDomesticStandingOrder3DataInitiation())
                .frequency("frequency")
                .reference("reference")
                .numberOfPayments("1")
                .firstPaymentDateTime(NOW)
                .recurringPaymentDateTime(NOW)
                .finalPaymentDateTime(NOW)
                .firstPaymentAmount((new OBWriteDomesticStandingOrder3DataInitiationFirstPaymentAmount())
                        .currency(GBP)
                        .amount(AMOUNT))
                .recurringPaymentAmount((new OBWriteDomesticStandingOrder3DataInitiationRecurringPaymentAmount())
                        .currency(GBP)
                        .amount(AMOUNT))
                .finalPaymentAmount((new OBWriteDomesticStandingOrder3DataInitiationFinalPaymentAmount())
                        .currency(GBP)
                        .amount(AMOUNT))
                .debtorAccount((new OBWriteDomesticStandingOrder3DataInitiationDebtorAccount())
                        .schemeName("schemeName")
                        .identification("1")
                        .name("debtor account name")
                        .secondaryIdentification("11"))
                .creditorAccount((new OBWriteDomesticStandingOrder3DataInitiationCreditorAccount())
                        .schemeName("schemeName")
                        .identification("2")
                        .name("creditor account name")
                        .secondaryIdentification("22"))
                .supplementaryData(new OBSupplementaryData1());
    }

    private OBWriteDomesticConsent3DataAuthorisation obWriteDomesticConsent3DataAuthorisation() {
        return (new OBWriteDomesticConsent3DataAuthorisation())
                .authorisationType(AuthorisationTypeEnum.ANY)
                .completionDateTime(NOW);
    }

    private OBWriteDataDomesticStandingOrderConsent3 expectedObWriteDataDomesticStandingOrderConsent3(OBWriteDomesticStandingOrderConsent4Data data) {
        return (new OBWriteDataDomesticStandingOrderConsent3())
                .permission(OBExternalPermissions2Code.valueOf(data.getPermission().name()))
                .initiation(expectedObDomesticStandingOrder3(data.getInitiation()))
                .authorisation(expectedObAuthorisation1(data.getAuthorisation()));
    }

    private OBDomesticStandingOrder3 expectedObDomesticStandingOrder3(OBWriteDomesticStandingOrder3DataInitiation initiation) {
        return (new OBDomesticStandingOrder3())
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .recurringPaymentDateTime(initiation.getRecurringPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .firstPaymentAmount((new OBDomesticStandingOrder3FirstPaymentAmount())
                        .currency(initiation.getFirstPaymentAmount().getCurrency())
                        .amount(initiation.getFirstPaymentAmount().getAmount()))
                .recurringPaymentAmount((new OBDomesticStandingOrder3RecurringPaymentAmount())
                        .currency(initiation.getRecurringPaymentAmount().getCurrency())
                        .amount(initiation.getRecurringPaymentAmount().getAmount()))
                .finalPaymentAmount((new OBDomesticStandingOrder3FinalPaymentAmount())
                        .currency(initiation.getFirstPaymentAmount().getCurrency())
                        .amount(initiation.getFirstPaymentAmount().getAmount()))
                .debtorAccount((new OBCashAccountDebtor4())
                        .schemeName(initiation.getDebtorAccount().getSchemeName())
                        .identification(initiation.getDebtorAccount().getIdentification())
                        .name(initiation.getDebtorAccount().getName())
                        .secondaryIdentification(initiation.getDebtorAccount().getSecondaryIdentification()))
                .creditorAccount((new OBCashAccountCreditor3())
                        .schemeName(initiation.getCreditorAccount().getSchemeName())
                        .identification(initiation.getCreditorAccount().getIdentification())
                        .name(initiation.getCreditorAccount().getName())
                        .secondaryIdentification(initiation.getCreditorAccount().getSecondaryIdentification()))
                .supplementaryData(new OBSupplementaryData1());
    }

    private OBAuthorisation1 expectedObAuthorisation1(OBWriteDomesticConsent3DataAuthorisation authorisation) {
        return (new OBAuthorisation1())
                .authorisationType(OBExternalAuthorisation1Code.valueOf(authorisation.getAuthorisationType().name()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }
}