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

import uk.org.openbanking.datamodel.payment.OBInternationalStandingOrder3;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiation;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAccountConverter.*;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBDomestic2InstructedAmount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBWriteDomestic2DataInitiationInstructedAmount;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.*;

public class OBInternationalStandingOrderConverter {

    public static OBInternationalStandingOrder3 toOBInternationalStandingOrder3(OBWriteInternationalStandingOrder4DataInitiation initiation) {
        return (new OBInternationalStandingOrder3())
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .instructedAmount(toOBDomestic2InstructedAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBCashAccountDebtor4(initiation.getDebtorAccount()))
                .creditor(toOBPartyIdentification43(initiation.getCreditor()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification6(initiation.getCreditorAgent()))
                .creditorAccount(toOBCashAccountCreditor3(initiation.getCreditorAccount()))
                .supplementaryData(initiation.getSupplementaryData());
    }

    public static OBWriteInternationalStandingOrder4DataInitiation toOBWriteInternationalStandingOrder4DataInitiation(OBInternationalStandingOrder3 initiation) {
        return (new OBWriteInternationalStandingOrder4DataInitiation())
                .frequency(initiation.getFrequency())
                .reference(initiation.getReference())
                .numberOfPayments(initiation.getNumberOfPayments())
                .firstPaymentDateTime(initiation.getFirstPaymentDateTime())
                .finalPaymentDateTime(initiation.getFinalPaymentDateTime())
                .purpose(initiation.getPurpose())
                // TODO #216 - populate extended purpose
                //.extendedPurpose(initiation.getExtendedPurpose())
                .chargeBearer(initiation.getChargeBearer())
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                // TODO #216 - populate destination country
                //.destinationCountryCode(initiation.getDestinationCountryCode())
                .instructedAmount(toOBWriteDomestic2DataInitiationInstructedAmount(initiation.getInstructedAmount()))
                .debtorAccount(toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount(initiation.getDebtorAccount()))
                .creditor(toOBWriteInternational3DataInitiationCreditor(initiation.getCreditor()))
                .creditorAgent(toOBWriteInternationalStandingOrder4DataInitiationCreditorAgent(initiation.getCreditorAgent()))
                .creditorAccount(toOBWriteInternationalStandingOrder4DataInitiationCreditorAccount(initiation.getCreditorAccount()))
                .supplementaryData(initiation.getSupplementaryData());
    }
}
