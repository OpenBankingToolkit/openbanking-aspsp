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
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBWriteDomesticConsentConverter.toOBWriteDomesticConsent2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.joda.time.DateTime.now;

/**
 * Unit test for {@link OBWriteDomesticConsentConverter}.
 */
public class OBWriteDomesticConsentConverterTest {

    private static final DateTime NOW = now();
    private static final String GBP = "GBP";
    private static final String AMOUNT = "10.01";

    @Test
    public void shouldConvertToOBWriteDomesticConsent2() {
        // Given
        OBWriteDomesticConsent3Data data = obWriteDomesticConsent3Data();
        OBRisk1 risk = new OBRisk1();
        OBWriteDomesticConsent3 obWriteDomesticConsent3 = new OBWriteDomesticConsent3();
        obWriteDomesticConsent3.data(data);
        obWriteDomesticConsent3.risk(risk);
        OBWriteDataDomesticConsent2 expectedData = expectedObWriteDataDomesticConsent2(data);

        // When
        OBWriteDomesticConsent2 obWriteDomesticConsent2 = toOBWriteDomesticConsent2(obWriteDomesticConsent3);

        // Then
        assertThat(obWriteDomesticConsent2.getData()).isEqualTo(expectedData);
        assertThat(obWriteDomesticConsent2.getRisk()).isEqualTo(risk);
    }

    private OBWriteDomesticConsent3Data obWriteDomesticConsent3Data() {
        return (new OBWriteDomesticConsent3Data())
                .initiation(obWriteDomestic2DataInitiation())
                .authorisation(obWriteDomesticConsent3DataAuthorisation());
    }

    private OBWriteDomestic2DataInitiation obWriteDomestic2DataInitiation() {
        return (new OBWriteDomestic2DataInitiation())
                .instructionIdentification("instructionIdentification")
                .endToEndIdentification("endToEndIdentification")
                .localInstrument("localInstrument")
                .instructedAmount((new OBWriteDomestic2DataInitiationInstructedAmount())
                        .currency(GBP)
                        .amount(AMOUNT))
                .debtorAccount((new OBWriteDomestic2DataInitiationDebtorAccount())
                        .schemeName("schemeName")
                        .identification("1")
                        .name("debtor account name")
                        .secondaryIdentification("11"))
                .creditorAccount((new OBWriteDomestic2DataInitiationCreditorAccount())
                        .schemeName("schemeName")
                        .identification("2")
                        .name("creditor account name")
                        .secondaryIdentification("22"))
                .creditorPostalAddress((new OBPostalAddress6())
                        .addressType(OBAddressTypeCode.RESIDENTIAL)
                        .streetName("The Mall")
                        .buildingNumber("1")
                        .postCode("WC1 1AB")
                        .townName("London")
                        .country("UK"))
                .remittanceInformation((new OBWriteDomestic2DataInitiationRemittanceInformation())
                        .unstructured("unstructured remittance info")
                        .reference("1"))
                .supplementaryData(new OBSupplementaryData1());
    }

    private OBWriteDomesticConsent3DataAuthorisation obWriteDomesticConsent3DataAuthorisation() {
        return (new OBWriteDomesticConsent3DataAuthorisation())
                .authorisationType(OBWriteDomesticConsent3DataAuthorisation.AuthorisationTypeEnum.ANY)
                .completionDateTime(NOW);
    }

    private OBWriteDataDomesticConsent2 expectedObWriteDataDomesticConsent2(OBWriteDomesticConsent3Data data) {
        return (new OBWriteDataDomesticConsent2())
                .initiation(expectedObDomestic2(data.getInitiation()))
                .authorisation(expectedObAuthorisation1(data.getAuthorisation()));
    }

    private OBDomestic2 expectedObDomestic2(OBWriteDomestic2DataInitiation initiation) {
        return (new OBDomestic2())
                .instructionIdentification(initiation.getInstructionIdentification())
                .endToEndIdentification(initiation.getEndToEndIdentification())
                .localInstrument(initiation.getLocalInstrument())
                .instructedAmount((new OBActiveOrHistoricCurrencyAndAmount())
                        .currency(initiation.getInstructedAmount().getCurrency())
                        .amount(initiation.getInstructedAmount().getAmount()))
                .debtorAccount((new OBCashAccount3())
                        .schemeName(initiation.getDebtorAccount().getSchemeName())
                        .identification(initiation.getDebtorAccount().getIdentification())
                        .name(initiation.getDebtorAccount().getName())
                        .secondaryIdentification(initiation.getDebtorAccount().getSecondaryIdentification()))
                .creditorAccount((new OBCashAccount3())
                        .schemeName(initiation.getCreditorAccount().getSchemeName())
                        .identification(initiation.getCreditorAccount().getIdentification())
                        .name(initiation.getCreditorAccount().getName())
                        .secondaryIdentification(initiation.getCreditorAccount().getSecondaryIdentification()))
                .creditorPostalAddress(initiation.getCreditorPostalAddress())
                .remittanceInformation((new OBRemittanceInformation1())
                        .unstructured(initiation.getRemittanceInformation().getUnstructured())
                        .reference(initiation.getRemittanceInformation().getReference()))
                .supplementaryData(new OBSupplementaryData1());
    }

    private OBAuthorisation1 expectedObAuthorisation1(OBWriteDomesticConsent3DataAuthorisation authorisation) {
        return (new OBAuthorisation1())
                .authorisationType(OBExternalAuthorisation1Code.valueOf(authorisation.getAuthorisationType().name()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }
}