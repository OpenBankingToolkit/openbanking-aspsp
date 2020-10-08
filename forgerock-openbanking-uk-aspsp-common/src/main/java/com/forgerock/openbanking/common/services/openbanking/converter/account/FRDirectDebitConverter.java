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
package com.forgerock.openbanking.common.services.openbanking.converter.account;

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRDirectDebitData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRDirectDebitData.FRDirectDebitStatus;
import uk.org.openbanking.datamodel.account.OBDirectDebit1;
import uk.org.openbanking.datamodel.account.OBExternalDirectDebitStatus1Code;
import uk.org.openbanking.datamodel.account.OBReadDirectDebit2DataDirectDebit;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount0;


/**
 * Converter for 'OBDirectDebit' model objects.
 */
public class FRDirectDebitConverter {

    // FR to OB
    public static OBDirectDebit1 toOBDirectDebit1(FRDirectDebitData directDebitData) {
        return directDebitData == null ? null : new OBDirectDebit1()
                .accountId(directDebitData.getAccountId())
                .directDebitId(directDebitData.getDirectDebitId())
                .mandateIdentification(directDebitData.getMandateIdentification())
                .directDebitStatusCode(toOBExternalDirectDebitStatus1Code(directDebitData.getDirectDebitStatusCode()))
                .name(directDebitData.getName())
                .previousPaymentDateTime(directDebitData.getPreviousPaymentDateTime())
                .previousPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(directDebitData.getPreviousPaymentAmount()));
    }

    public static OBReadDirectDebit2DataDirectDebit toOBReadDirectDebit2DataDirectDebit(FRDirectDebitData directDebitData) {
        return directDebitData == null ? null : new OBReadDirectDebit2DataDirectDebit()
                .accountId(directDebitData.getAccountId())
                .directDebitId(directDebitData.getDirectDebitId())
                .mandateIdentification(directDebitData.getMandateIdentification())
                .directDebitStatusCode(toOBExternalDirectDebitStatus1Code(directDebitData.getDirectDebitStatusCode()))
                .name(directDebitData.getName())
                .previousPaymentDateTime(directDebitData.getPreviousPaymentDateTime())
                .frequency(directDebitData.getFrequency())
                .previousPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount0(directDebitData.getPreviousPaymentAmount()));
    }

    public static OBExternalDirectDebitStatus1Code toOBExternalDirectDebitStatus1Code(FRDirectDebitStatus status) {
        return status == null ? null : OBExternalDirectDebitStatus1Code.valueOf(status.name());
    }
}
