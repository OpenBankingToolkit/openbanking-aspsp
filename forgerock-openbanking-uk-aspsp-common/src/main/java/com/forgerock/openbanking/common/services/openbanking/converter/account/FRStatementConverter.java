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

import com.forgerock.openbanking.common.model.openbanking.persistence.account.v2_0.FRStatement1;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRStatement4;
import uk.org.openbanking.datamodel.account.OBCreditDebitCode;
import uk.org.openbanking.datamodel.account.OBStatement1;
import uk.org.openbanking.datamodel.account.OBStatement2;
import uk.org.openbanking.datamodel.account.OBStatementFee1;
import uk.org.openbanking.datamodel.account.OBStatementFee2;
import uk.org.openbanking.datamodel.account.OBStatementInterest1;
import uk.org.openbanking.datamodel.account.OBStatementInterest2;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;

/**
 * Converter for 'FRStatement' documents.
 */
public class FRStatementConverter {

    public static FRStatement4 toFRStatement4(FRStatement1 frStatement1) {
        return frStatement1 == null ? null : FRStatement4.builder()
                .id(frStatement1.getId())
                .accountId(frStatement1.getAccountId())
                .statement(toOBStatement2(frStatement1.getStatement()))
                .created(frStatement1.getCreated())
                .updated(frStatement1.getUpdated())
                .build();
    }

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

    public static OBStatement2 toOBStatement2(OBStatement1 obStatement1) {
        return obStatement1 == null ? null : (new OBStatement2())
                .accountId(obStatement1.getAccountId())
                .statementId(obStatement1.getStatementId())
                .statementReference(obStatement1.getStatementReference())
                .type(obStatement1.getType())
                .startDateTime(obStatement1.getStartDateTime())
                .endDateTime(obStatement1.getEndDateTime())
                .creationDateTime(obStatement1.getCreationDateTime())
                .statementDescription(obStatement1.getStatementDescription())
                .statementBenefit(obStatement1.getStatementBenefit())
                .statementFee(toOBStatementFee2List(obStatement1.getStatementFee()))
                .statementInterest(toOBStatementInterest2List(obStatement1.getStatementInterest()))
                .statementDateTime(obStatement1.getStatementDateTime())
                .statementRate(obStatement1.getStatementRate())
                .statementValue(obStatement1.getStatementValue())
                .statementAmount(obStatement1.getStatementAmount());
    }

    public static List<OBStatementFee1> toOBStatementFee1List(List<OBStatementFee2> obStatementFee2s) {
        if (obStatementFee2s == null) {
            return null;
        }
        return obStatementFee2s.stream()
                .map(FRStatementConverter::toOBStatementFee1)
                .collect(Collectors.toList());
    }

    public static List<OBStatementFee2> toOBStatementFee2List(List<OBStatementFee1> obStatementFee1s) {
        if (obStatementFee1s == null) {
            return null;
        }
        return obStatementFee1s.stream()
                .map(FRStatementConverter::toOBStatementFee2)
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

    public static List<OBStatementInterest2> toOBStatementInterest2List(List<OBStatementInterest1> obStatementInterest1s) {
        if (obStatementInterest1s == null) {
            return null;
        }
        return obStatementInterest1s.stream()
                .map(FRStatementConverter::toOBStatementInterest2)
                .collect(Collectors.toList());
    }

    public static OBStatementFee1 toOBStatementFee1(OBStatementFee2 obStatementFee2) {
        return obStatementFee2 == null ? null : (new OBStatementFee1())
                .creditDebitIndicator(OBCreditDebitCode.valueOf(obStatementFee2.getCreditDebitIndicator().name()))
                .type(obStatementFee2.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obStatementFee2.getAmount()));
    }

    public static OBStatementFee2 toOBStatementFee2(OBStatementFee1 obStatementFee1) {
        return obStatementFee1 == null ? null : (new OBStatementFee2())
                .creditDebitIndicator(OBStatementFee2.CreditDebitIndicatorEnum.valueOf(obStatementFee1.getCreditDebitIndicator().name()))
                .type(obStatementFee1.getType())
                .amount(toAccountOBActiveOrHistoricCurrencyAndAmount(obStatementFee1.getAmount()));
    }

    public static OBStatementInterest1 toOBStatementInterest1(OBStatementInterest2 obStatementInterest2) {
        return obStatementInterest2 == null ? null : (new OBStatementInterest1())
                .creditDebitIndicator(OBCreditDebitCode.valueOf(obStatementInterest2.getCreditDebitIndicator().name()))
                .type(obStatementInterest2.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obStatementInterest2.getAmount()));
    }

    public static OBStatementInterest2 toOBStatementInterest2(OBStatementInterest1 obStatementInterest1) {
        return obStatementInterest1 == null ? null : (new OBStatementInterest2())
                .creditDebitIndicator(OBStatementInterest2.CreditDebitIndicatorEnum.valueOf(obStatementInterest1.getCreditDebitIndicator().name()))
                .type(obStatementInterest1.getType())
                .amount(toAccountOBActiveOrHistoricCurrencyAndAmount(obStatementInterest1.getAmount()));
    }
}
