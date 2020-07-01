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

import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBStatement1;
import uk.org.openbanking.datamodel.account.OBStatement2;
import uk.org.openbanking.datamodel.account.OBStatementFee1;
import uk.org.openbanking.datamodel.account.OBStatementFee2;
import uk.org.openbanking.datamodel.account.OBStatementInterest1;
import uk.org.openbanking.datamodel.account.OBStatementInterest2;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;

/**
 * Converter for 'FRStatement' documents.
 */
public class FRStatementConverter {

    public static OBStatement1 toOBStatement1(OBStatement2 obStatement2) {
        return obStatement2 == null ? null : (new OBStatement1())
                .accountId(obStatement2.getAccountId())
                .statementId(obStatement2.getStatementId())
                .statementReference(obStatement2.getStatementReference())
                .type(obStatement2.getType())
                .startDateTime(obStatement2.getStartDateTime())
                .endDateTime(obStatement2.getEndDateTime())
                .creationDateTime(obStatement2.getCreationDateTime())
                .statementDescription(obStatement2.getStatementDescription())
                .statementBenefit(obStatement2.getStatementBenefit())
                .statementFee(toOBStatementFee1List(obStatement2.getStatementFee()))
                .statementInterest(toOBStatementInterest1List(obStatement2.getStatementInterest()))
                .statementDateTime(obStatement2.getStatementDateTime())
                .statementRate(obStatement2.getStatementRate())
                .statementValue(obStatement2.getStatementValue())
                .statementAmount(obStatement2.getStatementAmount());
    }

    public static List<OBStatementFee1> toOBStatementFee1List(List<OBStatementFee2> obStatementFee2s) {
        if (obStatementFee2s == null) {
            return null;
        }
        return obStatementFee2s.stream()
                .map(FRStatementConverter::toOBStatementFee1)
                .collect(Collectors.toList());
    }

    public static List<OBStatementInterest1> toOBStatementInterest1List(List<OBStatementInterest2> obStatementInterest2s) {
        if (obStatementInterest2s == null) {
            return null;
        }
        return obStatementInterest2s.stream()
                .map(FRStatementConverter::toOBStatementInterest1)
                .collect(Collectors.toList());
    }

    public static OBStatementFee1 toOBStatementFee1(OBStatementFee2 obStatementFee2) {
        return obStatementFee2 == null ? null : (new OBStatementFee1())
                .creditDebitIndicator(OBCreditDebitCode.valueOf(obStatementFee2.getCreditDebitIndicator().name()))
                .type(obStatementFee2.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obStatementFee2.getAmount()));
    }

    public static OBStatementInterest1 toOBStatementInterest1(OBStatementInterest2 obStatementInterest2) {
        return obStatementInterest2 == null ? null : (new OBStatementInterest1())
                .creditDebitIndicator(OBCreditDebitCode.valueOf(obStatementInterest2.getCreditDebitIndicator().name()))
                .type(obStatementInterest2.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obStatementInterest2.getAmount()));
    }
}
