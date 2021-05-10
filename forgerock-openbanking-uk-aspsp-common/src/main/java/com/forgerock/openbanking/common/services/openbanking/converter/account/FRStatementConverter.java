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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementBenefit;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementDateTime;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementFee;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementInterest;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementRate;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementType;
import com.forgerock.openbanking.common.model.openbanking.domain.account.FRStatementData.FRStatementValue;
import lombok.extern.slf4j.Slf4j;
import uk.org.openbanking.datamodel.account.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCreditDebitIndicatorConverter.*;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.*;

/**
 * Converter for 'FRStatement' documents.
 */
@Slf4j
public class FRStatementConverter {

    // FR to OB
    public static OBStatement1 toOBStatement1(FRStatementData frStatementData) {
        return frStatementData == null ? null : new OBStatement1()
                .accountId(frStatementData.getAccountId())
                .statementId(frStatementData.getStatementId())
                .statementReference(frStatementData.getStatementReference())
                .type(toOBExternalStatementType1Code(frStatementData.getType()))
                .startDateTime(frStatementData.getStartDateTime())
                .endDateTime(frStatementData.getEndDateTime())
                .creationDateTime(frStatementData.getCreationDateTime())
                .statementDescription(frStatementData.getStatementDescriptions())
                .statementBenefit(toOBStatementBenefit1List(frStatementData.getStatementBenefits()))
                .statementFee(toOBStatementFee1List(frStatementData.getStatementFees()))
                .statementInterest(toOBStatementInterest1List(frStatementData.getStatementInterests()))
                .statementDateTime(toOBStatementDateTime1List(frStatementData.getStatementDateTimes()))
                .statementRate(toOBStatementRate1List(frStatementData.getStatementRates()))
                .statementValue(toOBStatementValue1List(frStatementData.getStatementValues()))
                .statementAmount(toOBStatementAmount1List(frStatementData.getStatementAmounts()));
    }

    public static OBStatement2 toOBStatement2(FRStatementData frStatementData) {
        return frStatementData == null ? null : new OBStatement2()
                .accountId(frStatementData.getAccountId())
                .statementId(frStatementData.getStatementId())
                .statementReference(frStatementData.getStatementReference())
                .type(toOBExternalStatementType1Code(frStatementData.getType()))
                .startDateTime(frStatementData.getStartDateTime())
                .endDateTime(frStatementData.getEndDateTime())
                .creationDateTime(frStatementData.getCreationDateTime())
                .statementDescription(frStatementData.getStatementDescriptions())
                .statementBenefit(toOBStatement2StatementBenefitList(frStatementData.getStatementBenefits()))
                .statementFee(toOBStatement2StatementFeeList(frStatementData.getStatementFees()))
                .statementInterest(toOBStatement2StatementInterestList(frStatementData.getStatementInterests()))
                .statementDateTime(toOBStatement2StatementDateTimeList(frStatementData.getStatementDateTimes()))
                .statementRate(toOBStatement2StatementRateList(frStatementData.getStatementRates()))
                .statementValue(toOBStatement2StatementValueList(frStatementData.getStatementValues()))
                .statementAmount(toOBStatement2StatementAmountList(frStatementData.getStatementAmounts()));
    }

    public static OBExternalStatementType1Code toOBExternalStatementType1Code(FRStatementType type) {
        return type == null ? null : OBExternalStatementType1Code.valueOf(type.name());
    }

    public static List<OBStatementBenefit1> toOBStatementBenefit1List(List<FRStatementBenefit> statementBenefit) {
        return statementBenefit == null ? null : statementBenefit.stream()
                .map(FRStatementConverter::toOBStatementBenefit1)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementBenefit> toOBStatement2StatementBenefitList(List<FRStatementBenefit> statementBenefit) {
        return statementBenefit == null ? null : statementBenefit.stream()
                .map(FRStatementConverter::toOBStatement2StatementBenefit)
                .collect(Collectors.toList());
    }

    public static List<OBStatementFee1> toOBStatementFee1List(List<FRStatementFee> statementFee) {
        return statementFee == null ? null : statementFee.stream()
                .map(FRStatementConverter::toOBStatementFee1)
                .collect(Collectors.toList());
    }

    public static List<OBStatementFee2> toOBStatementFee2List(List<FRStatementFee> statementFee) {
        return statementFee == null ? null : statementFee.stream()
                .map(FRStatementConverter::toOBStatementFee2)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementFee> toOBStatement2StatementFeeList(List<FRStatementFee> statementFees) {
        return statementFees == null ? null : statementFees.stream()
                .map(FRStatementConverter::toOBStatement2StatementFee)
                .collect(Collectors.toList());
    }

    public static List<OBStatementInterest1> toOBStatementInterest1List(List<FRStatementInterest> statementInterest) {
        return statementInterest == null ? null : statementInterest.stream()
                .map(FRStatementConverter::toOBStatementInterest1)
                .collect(Collectors.toList());
    }

    public static List<OBStatementInterest2> toOBStatementInterest2List(List<FRStatementInterest> statementInterest) {
        return statementInterest == null ? null : statementInterest.stream()
                .map(FRStatementConverter::toOBStatementInterest2)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementInterest> toOBStatement2StatementInterestList(List<FRStatementInterest> statementInterest) {
        return statementInterest == null ? null : statementInterest.stream()
                .map(FRStatementConverter::toOBStatement2StatementInterest)
                .collect(Collectors.toList());
    }

    public static List<OBStatementDateTime1> toOBStatementDateTime1List(List<FRStatementDateTime> statementDateTime) {
        return statementDateTime == null ? null : statementDateTime.stream()
                .map(FRStatementConverter::toOBStatementDateTime1)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementDateTime> toOBStatement2StatementDateTimeList(List<FRStatementDateTime> statementDateTimes) {
        return statementDateTimes == null ? null : statementDateTimes.stream()
                .map(FRStatementConverter::toOBStatement2StatementDateTime)
                .collect(Collectors.toList());
    }

    public static List<OBStatementRate1> toOBStatementRate1List(List<FRStatementRate> statementRate) {
        return statementRate == null ? null : statementRate.stream()
                .map(FRStatementConverter::toOBStatementRate1)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementRate> toOBStatement2StatementRateList(List<FRStatementRate> statementRate) {
        return statementRate == null ? null : statementRate.stream()
                .map(FRStatementConverter::toOBStatement2StatementRate)
                .collect(Collectors.toList());
    }

    public static List<OBStatementValue1> toOBStatementValue1List(List<FRStatementValue> statementValue) {
        return statementValue == null ? null : statementValue.stream()
                .map(FRStatementConverter::toOBStatementValue1)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementValue> toOBStatement2StatementValueList(List<FRStatementValue> statementValue) {
        return statementValue == null ? null : statementValue.stream()
                .map(FRStatementConverter::toOBStatement2StatementValue)
                .collect(Collectors.toList());
    }

    public static List<OBStatementAmount1> toOBStatementAmount1List(List<FRStatementAmount> statementAmount) {
        return statementAmount == null ? null : statementAmount.stream()
                .map(FRStatementConverter::toOBStatementAmount1)
                .collect(Collectors.toList());
    }

    public static List<OBStatement2StatementAmount> toOBStatement2StatementAmountList(List<FRStatementAmount> statementAmount) {
        return statementAmount == null ? null : statementAmount.stream()
                .map(FRStatementConverter::toOBStatement2StatementAmount)
                .collect(Collectors.toList());
    }

    public static OBStatementBenefit1 toOBStatementBenefit1(FRStatementBenefit statementBenefit) {
        return statementBenefit == null ? null : new OBStatementBenefit1()
                .type(statementBenefit.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(statementBenefit.getAmount()));
    }

    public static OBStatement2StatementBenefit toOBStatement2StatementBenefit(FRStatementBenefit statementBenefit) {
        return statementBenefit == null ? null : new OBStatement2StatementBenefit()
                .type(statementBenefit.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount5(statementBenefit.getAmount()));
    }

    public static OBStatementFee1 toOBStatementFee1(FRStatementFee statementFee) {
        return statementFee == null ? null : new OBStatementFee1()
                .creditDebitIndicator(toOBCreditDebitCode(statementFee.getCreditDebitIndicator()))
                .type(statementFee.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(statementFee.getAmount()));
    }

    public static OBStatementFee2 toOBStatementFee2(FRStatementFee statementFee) {
        return statementFee == null ? null : new OBStatementFee2()
                .description(statementFee.getDescription())
                .creditDebitIndicator(toOBStatementFee2CreditDebitIndicatorEnum(statementFee.getCreditDebitIndicator()))
                .type(statementFee.getType())
                .rate(statementFee.getRate())
                .rateType(statementFee.getRateType())
                .frequency(statementFee.getFrequency())
                .amount(toAccountOBActiveOrHistoricCurrencyAndAmount(statementFee.getAmount()));
    }

    public static OBStatement2StatementFee toOBStatement2StatementFee(FRStatementFee statementFee) {
        return statementFee == null ? null : new OBStatement2StatementFee()
                .description(statementFee.getDescription())
                .creditDebitIndicator(toOBCreditDebitCode0(statementFee.getCreditDebitIndicator()))
                .type(statementFee.getType())
                .rate(statementFee.getRate())
                .rateType(statementFee.getRateType())
                .frequency(statementFee.getFrequency())
                .amount(toOBActiveOrHistoricCurrencyAndAmount6(statementFee.getAmount()));
    }

    public static OBStatementInterest1 toOBStatementInterest1(FRStatementInterest statementInterest) {
        return statementInterest == null ? null : new OBStatementInterest1()
                .creditDebitIndicator(toOBCreditDebitCode(statementInterest.getCreditDebitIndicator()))
                .type(statementInterest.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(statementInterest.getAmount()));
    }

    public static OBStatementInterest2 toOBStatementInterest2(FRStatementInterest statementInterest) {
        return statementInterest == null ? null : new OBStatementInterest2()
                .description(statementInterest.getDescription())
                .creditDebitIndicator(toOBStatementInterest2CreditDebitIndicatorEnum(statementInterest.getCreditDebitIndicator()))
                .type(statementInterest.getType())
                .rate(statementInterest.getRate())
                .rateType(statementInterest.getRateType())
                .frequency(statementInterest.getFrequency())
                .amount(toAccountOBActiveOrHistoricCurrencyAndAmount(statementInterest.getAmount()));
    }

    public static OBStatement2StatementInterest toOBStatement2StatementInterest(FRStatementInterest statementInterest) {
        return statementInterest == null ? null : new OBStatement2StatementInterest()
                .description(statementInterest.getDescription())
                .creditDebitIndicator(toOBCreditDebitCode0(statementInterest.getCreditDebitIndicator()))
                .type(statementInterest.getType())
                .rate(statementInterest.getRate())
                .rateType(statementInterest.getRateType())
                .frequency(statementInterest.getFrequency())
                .amount(toOBActiveOrHistoricCurrencyAndAmount7(statementInterest.getAmount()));
    }

    public static OBStatementDateTime1 toOBStatementDateTime1(FRStatementDateTime statementDateTime) {
        return statementDateTime == null ? null : new OBStatementDateTime1()
                .dateTime(statementDateTime.getDateTime())
                .type(statementDateTime.getType());
    }

    public static OBStatement2StatementDateTime toOBStatement2StatementDateTime(FRStatementDateTime statementDateTime) {
        return statementDateTime == null ? null : new OBStatement2StatementDateTime()
                .dateTime(statementDateTime.getDateTime())
                .type(statementDateTime.getType());
    }

    public static OBStatementRate1 toOBStatementRate1(FRStatementRate statementRate) {
        return statementRate == null ? null : new OBStatementRate1()
                .rate(statementRate.getRate())
                .type(statementRate.getType());
    }

    public static OBStatement2StatementRate toOBStatement2StatementRate(FRStatementRate statementRate) {
        return statementRate == null ? null : new OBStatement2StatementRate()
                .rate(statementRate.getRate())
                .type(statementRate.getType());
    }

    public static OBStatementValue1 toOBStatementValue1(FRStatementValue statementValue) {
        if (statementValue == null) {
            return null;
        }
        Integer value = null;
        try {
            value = Integer.valueOf(statementValue.getValue());
        } catch (NumberFormatException e) {
            log.warn("Unable to convert statementValue [{}] to Integer", statementValue.getValue());
        }
        return new OBStatementValue1()
                .value(value)
                .type(statementValue.getType());
    }

    public static OBStatement2StatementValue toOBStatement2StatementValue(FRStatementValue statementValue) {
        return statementValue == null ? null : new OBStatement2StatementValue()
                .value(String.valueOf(statementValue.getValue()))
                .type(statementValue.getType());
    }

    public static OBStatementAmount1 toOBStatementAmount1(FRStatementAmount statementAmount) {
        return statementAmount == null ? null : new OBStatementAmount1()
                .creditDebitIndicator(toOBCreditDebitCode(statementAmount.getCreditDebitIndicator()))
                .type(statementAmount.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount(statementAmount.getAmount()));
    }

    public static OBStatement2StatementAmount toOBStatement2StatementAmount(FRStatementAmount statementAmount) {
        return statementAmount == null ? null : new OBStatement2StatementAmount()
                .creditDebitIndicator(toOBCreditDebitCode0(statementAmount.getCreditDebitIndicator()))
                .type(statementAmount.getType())
                .amount(toOBActiveOrHistoricCurrencyAndAmount8(statementAmount.getAmount()));
    }

    // OB to FR
    public static FRStatementData toFRStatementData(OBStatement1 obStatement) {
        return obStatement == null ? null : FRStatementData.builder()
                .accountId(obStatement.getAccountId())
                .statementId(obStatement.getStatementId())
                .statementReference(obStatement.getStatementReference())
                .type(toFRStatementType(obStatement.getType()))
                .startDateTime(obStatement.getStartDateTime())
                .endDateTime(obStatement.getEndDateTime())
                .creationDateTime(obStatement.getCreationDateTime())
                .statementDescriptions(obStatement.getStatementDescription())
                .statementBenefits(toStatementBenefitsList(obStatement.getStatementBenefit(), FRStatementConverter::toFRStatementBenefit))
                .statementFees(toStatementFeesList(obStatement.getStatementFee(), FRStatementConverter::toFRStatementFee))
                .statementInterests(toStatementInterestsList(obStatement.getStatementInterest(), FRStatementConverter::toFRStatementInterest))
                .statementDateTimes(toStatementDateTimesList(obStatement.getStatementDateTime(), FRStatementConverter::toFRStatementDateTime))
                .statementRates(toStatementRatesList(obStatement.getStatementRate(), FRStatementConverter::toFRStatementRate))
                .statementValues(toStatementValuesList(obStatement.getStatementValue(), FRStatementConverter::toFRStatementValue))
                .statementAmounts(toStatementAmountsList(obStatement.getStatementAmount(), FRStatementConverter::toFRStatementAmount))
                .build();
    }

    public static FRStatementData toFRStatementData(OBStatement2 obStatement) {
        return obStatement == null ? null : FRStatementData.builder()
                .accountId(obStatement.getAccountId())
                .statementId(obStatement.getStatementId())
                .statementReference(obStatement.getStatementReference())
                .type(toFRStatementType(obStatement.getType()))
                .startDateTime(obStatement.getStartDateTime())
                .endDateTime(obStatement.getEndDateTime())
                .creationDateTime(obStatement.getCreationDateTime())
                .statementDescriptions(obStatement.getStatementDescription())
                .statementBenefits(toStatementBenefitsList(obStatement.getStatementBenefit(), FRStatementConverter::toFRStatementBenefit))
                .statementFees(toStatementFeesList(obStatement.getStatementFee(), FRStatementConverter::toFRStatementFee))
                .statementInterests(toStatementInterestsList(obStatement.getStatementInterest(), FRStatementConverter::toFRStatementInterest))
                .statementDateTimes(toStatementDateTimesList(obStatement.getStatementDateTime(), FRStatementConverter::toFRStatementDateTime))
                .statementRates(toStatementRatesList(obStatement.getStatementRate(), FRStatementConverter::toFRStatementRate))
                .statementValues(toStatementValuesList(obStatement.getStatementValue(), FRStatementConverter::toFRStatementValue))
                .statementAmounts(toStatementAmountsList(obStatement.getStatementAmount(), FRStatementConverter::toFRStatementAmount))
                .build();
    }

    public static FRStatementType toFRStatementType(OBExternalStatementType1Code type) {
        return type == null ? null : FRStatementType.valueOf(type.name());
    }

    public static <T> List<FRStatementBenefit> toStatementBenefitsList(List<T> statementBenefit, Function<T, FRStatementBenefit> converter) {
        return statementBenefit == null ? null : statementBenefit.stream()
                .map(converter::apply)
                .collect(Collectors.toList());
    }

    public static <T> List<FRStatementFee> toStatementFeesList(List<T> statementFee, Function<T, FRStatementFee> converter) {
        return statementFee == null ? null : statementFee.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static <T> List<FRStatementInterest> toStatementInterestsList(List<T> statementInterest, Function<T, FRStatementInterest> converter) {
        return statementInterest == null ? null : statementInterest.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static <T> List<FRStatementDateTime> toStatementDateTimesList(List<T> statementDateTime, Function<T, FRStatementDateTime> converter) {
        return statementDateTime == null ? null : statementDateTime.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static <T> List<FRStatementRate> toStatementRatesList(List<T> statementRate, Function<T, FRStatementRate> converter) {
        return statementRate == null ? null : statementRate.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static <T> List<FRStatementValue> toStatementValuesList(List<T> statementValue, Function<T, FRStatementValue> converter) {
        return statementValue == null ? null : statementValue.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static <T> List<FRStatementAmount> toStatementAmountsList(List<T> statementAmount, Function<T, FRStatementAmount> converter) {
        return statementAmount == null ? null : statementAmount.stream()
                .map(converter)
                .collect(Collectors.toList());
    }

    public static FRStatementBenefit toFRStatementBenefit(OBStatementBenefit1 statementBenefit) {
        return statementBenefit == null ? null : FRStatementBenefit.builder()
                .type(statementBenefit.getType())
                .amount(toFRAmount(statementBenefit.getAmount()))
                .build();
    }

    public static FRStatementBenefit toFRStatementBenefit(OBStatement2StatementBenefit statementBenefit) {
        return statementBenefit == null ? null : FRStatementBenefit.builder()
                .type(statementBenefit.getType())
                .amount(toFRAmount(statementBenefit.getAmount()))
                .build();
    }

    public static FRStatementFee toFRStatementFee(OBStatementFee1 statementFee) {
        return statementFee == null ? null : FRStatementFee.builder()
                .creditDebitIndicator(toFRCreditDebitIndicator(statementFee.getCreditDebitIndicator()))
                .type(statementFee.getType())
                .amount(toFRAmount(statementFee.getAmount()))
                .build();
    }

    public static FRStatementFee toFRStatementFee(OBStatementFee2 statementFee) {
        return statementFee == null ? null : FRStatementFee.builder()
                .description(statementFee.getDescription())
                .creditDebitIndicator(toFRCreditDebitIndicator(statementFee.getCreditDebitIndicator()))
                .type(statementFee.getType())
                .rate(statementFee.getRate())
                .rateType(statementFee.getRateType())
                .frequency(statementFee.getFrequency())
                .amount(toFRAmount(statementFee.getAmount()))
                .build();
    }

    public static FRStatementFee toFRStatementFee(OBStatement2StatementFee statementFee) {
        return statementFee == null ? null : FRStatementFee.builder()
                .description(statementFee.getDescription())
                .creditDebitIndicator(toFRCreditDebitIndicator(statementFee.getCreditDebitIndicator()))
                .type(statementFee.getType())
                .rate(statementFee.getRate())
                .rateType(statementFee.getRateType())
                .frequency(statementFee.getFrequency())
                .amount(toFRAmount(statementFee.getAmount()))
                .build();
    }

    public static FRStatementInterest toFRStatementInterest(OBStatementInterest1 statementInterest) {
        return statementInterest == null ? null : FRStatementInterest.builder()
                .creditDebitIndicator(toFRCreditDebitIndicator(statementInterest.getCreditDebitIndicator()))
                .type(statementInterest.getType())
                .amount(toFRAmount(statementInterest.getAmount()))
                .build();
    }

    public static FRStatementInterest toFRStatementInterest(OBStatementInterest2 statementInterest) {
        return statementInterest == null ? null : FRStatementInterest.builder()
                .description(statementInterest.getDescription())
                .creditDebitIndicator(toFRCreditDebitIndicator(statementInterest.getCreditDebitIndicator()))
                .type(statementInterest.getType())
                .rate(statementInterest.getRate())
                .rateType(statementInterest.getRateType())
                .frequency(statementInterest.getFrequency())
                .amount(toFRAmount(statementInterest.getAmount()))
                .build();
    }

    public static FRStatementInterest toFRStatementInterest(OBStatement2StatementInterest statementInterest) {
        return statementInterest == null ? null : FRStatementInterest.builder()
                .description(statementInterest.getDescription())
                .creditDebitIndicator(toFRCreditDebitIndicator(statementInterest.getCreditDebitIndicator()))
                .type(statementInterest.getType())
                .rate(statementInterest.getRate())
                .rateType(statementInterest.getRateType())
                .frequency(statementInterest.getFrequency())
                .amount(toFRAmount(statementInterest.getAmount()))
                .build();
    }

    public static FRStatementDateTime toFRStatementDateTime(OBStatementDateTime1 statementDateTime) {
        return statementDateTime == null ? null : FRStatementDateTime.builder()
                .dateTime(statementDateTime.getDateTime())
                .type(statementDateTime.getType())
                .build();
    }

    public static FRStatementDateTime toFRStatementDateTime(OBStatement2StatementDateTime statementDateTime) {
        return statementDateTime == null ? null : FRStatementDateTime.builder()
                .dateTime(statementDateTime.getDateTime())
                .type(statementDateTime.getType())
                .build();
    }

    public static FRStatementRate toFRStatementRate(OBStatementRate1 statementRate) {
        return statementRate == null ? null : FRStatementRate.builder()
                .rate(statementRate.getRate())
                .type(statementRate.getType())
                .build();
    }

    public static FRStatementRate toFRStatementRate(OBStatement2StatementRate statementRate) {
        return statementRate == null ? null : FRStatementRate.builder()
                .rate(statementRate.getRate())
                .type(statementRate.getType())
                .build();
    }

    public static FRStatementValue toFRStatementValue(OBStatementValue1 statementValue) {
        return statementValue == null ? null : FRStatementValue.builder()
                .value(statementValue.getValue())
                .type(statementValue.getType())
                .build();
    }

    public static FRStatementValue toFRStatementValue(OBStatement2StatementValue statementValue) {
        if (statementValue == null) {
            return null;
        } else {
            Integer value = null;
            try {
                value = Integer.valueOf(statementValue.getValue());
            } catch (NumberFormatException e) {
                log.warn("Unable to convert statementValue [{}] to Integer", statementValue.getValue());
            }
            return FRStatementValue.builder()
                    .value(value)
                    .type(statementValue.getType())
                    .build();
        }
    }

    public static FRStatementAmount toFRStatementAmount(OBStatementAmount1 statementAmount) {
        return statementAmount == null ? null : FRStatementAmount.builder()
                .creditDebitIndicator(toFRCreditDebitIndicator(statementAmount.getCreditDebitIndicator()))
                .type(statementAmount.getType())
                .amount(toFRAmount(statementAmount.getAmount()))
                .build();
    }

    public static FRStatementAmount toFRStatementAmount(OBStatement2StatementAmount statementAmount) {
        return statementAmount == null ? null : FRStatementAmount.builder()
                .creditDebitIndicator(toFRCreditDebitIndicator(statementAmount.getCreditDebitIndicator()))
                .type(statementAmount.getType())
                .amount(toFRAmount(statementAmount.getAmount()))
                .build();
    }
}
