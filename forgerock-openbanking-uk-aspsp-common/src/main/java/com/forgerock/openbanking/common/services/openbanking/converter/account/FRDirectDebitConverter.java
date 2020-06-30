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

import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount0;
import uk.org.openbanking.datamodel.account.OBDirectDebit1;
import uk.org.openbanking.datamodel.account.OBReadDirectDebit2DataDirectDebit;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;

/**
 * Converter for 'OBDirectDebit' model objects.
 */
public class FRDirectDebitConverter {

    public static OBDirectDebit1 toOBDirectDebit1(OBReadDirectDebit2DataDirectDebit obReadDirectDebit2DataDirectDebit) {
        return obReadDirectDebit2DataDirectDebit == null ? null : (new OBDirectDebit1())
                .accountId(obReadDirectDebit2DataDirectDebit.getAccountId())
                .directDebitId(obReadDirectDebit2DataDirectDebit.getDirectDebitId())
                .mandateIdentification(obReadDirectDebit2DataDirectDebit.getMandateIdentification())
                .directDebitStatusCode(obReadDirectDebit2DataDirectDebit.getDirectDebitStatusCode())
                .name(obReadDirectDebit2DataDirectDebit.getName())
                .previousPaymentDateTime(obReadDirectDebit2DataDirectDebit.getPreviousPaymentDateTime())
                .previousPaymentAmount(toOBActiveOrHistoricCurrencyAndAmount(obReadDirectDebit2DataDirectDebit.getPreviousPaymentAmount()));
    }

    private static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(OBActiveOrHistoricCurrencyAndAmount0 amount) {
        return amount == null ? null : (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }
}
