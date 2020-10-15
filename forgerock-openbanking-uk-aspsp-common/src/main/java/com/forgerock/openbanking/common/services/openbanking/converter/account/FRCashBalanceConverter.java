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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRCashBalance;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRCreditLine;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRBalanceType;
import uk.org.openbanking.datamodel.account.OBBalanceType1Code;
import uk.org.openbanking.datamodel.account.OBCashBalance1;
import uk.org.openbanking.datamodel.account.OBCreditLine1;
import uk.org.openbanking.datamodel.account.OBExternalLimitType1Code;

import java.util.List;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCreditDebitIndicatorConverter.toFRCreditDebitIndicator;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCreditDebitIndicatorConverter.toOBCreditDebitCode;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static java.util.stream.Collectors.toList;

public class FRCashBalanceConverter {

    // FR to OB
    public static OBCashBalance1 toOBCashBalance1(FRCashBalance balance) {
        return balance == null ? null : new OBCashBalance1()
                .accountId(balance.getAccountId())
                .creditDebitIndicator(toOBCreditDebitCode(balance.getCreditDebitIndicator()))
                .type(toOBBalanceType1Code(balance.getType()))
                .dateTime(balance.getDateTime())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(balance.getAmount()))
                .creditLine(toCreditLineList(balance.getCreditLines()));
    }

    public static OBBalanceType1Code toOBBalanceType1Code(FRBalanceType type) {
        return type == null ? null : OBBalanceType1Code.valueOf(type.name());
    }

    public static List<OBCreditLine1> toCreditLineList(List<FRCreditLine> creditLines) {
        return creditLines == null ? null : creditLines.stream()
                .map(c -> toOBCreditLine1(c))
                .collect(toList());
    }

    public static OBCreditLine1 toOBCreditLine1(FRCreditLine creditLine) {
        return creditLine == null ? null : new OBCreditLine1()
                .included(creditLine.getIncluded())
                .type(toOBExternalLimitType1Code(creditLine.getType()))
                .amount(toOBActiveOrHistoricCurrencyAndAmount(creditLine.getAmount()));
    }

    public static OBExternalLimitType1Code toOBExternalLimitType1Code(FRCreditLine.FRLimitType type) {
        return type == null ? null : OBExternalLimitType1Code.valueOf(type.name());
    }

    // OB to FR
    public static FRCashBalance toFRCashBalance(OBCashBalance1 balance) {
        return balance == null ? null : FRCashBalance.builder()
                .accountId(balance.getAccountId())
                .creditDebitIndicator(toFRCreditDebitIndicator(balance.getCreditDebitIndicator()))
                .type(toFRBalanceType(balance.getType()))
                .dateTime(balance.getDateTime())
                .amount(toFRAmount(balance.getAmount()))
                .creditLines(toFRCreditLineList(balance.getCreditLine()))
                .build();
    }

    public static FRBalanceType toFRBalanceType(OBBalanceType1Code type) {
        return type == null ? null : FRBalanceType.valueOf(type.name());
    }

    public static List<FRCreditLine> toFRCreditLineList(List<OBCreditLine1> creditLines) {
        return creditLines == null ? null : creditLines.stream()
                .map(c -> toFRCreditLine(c))
                .collect(toList());
    }

    public static FRCreditLine toFRCreditLine(OBCreditLine1 creditLine) {
        return creditLine == null ? null : FRCreditLine.builder()
                .included(creditLine.isIncluded())
                .type(toFRLimitType(creditLine.getType()))
                .amount(toFRAmount(creditLine.getAmount()))
                .build();
    }

    public static FRCreditLine.FRLimitType toFRLimitType(OBExternalLimitType1Code type) {
        return type == null ? null : FRCreditLine.FRLimitType.valueOf(type.name());
    }
}
