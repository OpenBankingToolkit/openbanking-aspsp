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
package com.forgerock.openbanking.common.services.openbanking.converter.common;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.services.openbanking.converter.FRModelMapper;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1DataDebtorAccount;
import uk.org.openbanking.datamodel.payment.*;
import uk.org.openbanking.datamodel.vrp.OBCashAccountDebtorWithName;

public class FRAccountIdentifierConverter {

    // OB to FR
    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount3 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount6 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount51 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount60 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount61 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccountDebtor4 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccountCreditor3 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBWriteDomestic2DataInitiationCreditorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBWriteDomestic2DataInitiationDebtorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBWriteInternationalStandingOrder4DataInitiationCreditorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBWriteDomesticStandingOrder3DataInitiationDebtorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccountDebtor1 account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName() == null ? null : account.getSchemeName().getReference())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccountCreditor1 account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName() == null ? null : account.getSchemeName().getReference())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccountDebtorWithName account){
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBFundsConfirmationConsent1DataDebtorAccount account) {
        return account == null ? null :FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName() == null ? null : account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    // FR to OB
    public static OBFundsConfirmationConsent1DataDebtorAccount toOBFundsConfirmationConsent1DataDebtorAccount(FRAccountIdentifier account){
        return FRModelMapper.map(account, OBFundsConfirmationConsent1DataDebtorAccount.class);
    }

    public static OBCashAccountCreditor3 toOBCashAccountCreditor3(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccountCreditor3.class);
    }

    public static OBCashAccountDebtor4 toOBCashAccountDebtor4(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccountDebtor4.class);
    }

    public static OBCashAccount1 toOBCashAccount1(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount1.class);
    }

    public static OBCashAccount2 toOBCashAccount2(FRAccountIdentifier account) {
        return account == null ? null : new OBCashAccount2()
                .schemeName(account.getSchemeName() == null ? null : OBExternalAccountIdentification3Code.fromValue(account.getSchemeName()))
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification());
    }

    public static OBCashAccount3 toOBCashAccount3(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount3.class);
    }

    public static OBCashAccount5 toOBCashAccount5(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount5.class);
    }

    public static OBCashAccount6 toOBCashAccount6(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount6.class);
    }

    public static OBCashAccount50 toOBCashAccount50(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount50.class);
    }

    public static OBCashAccount51 toOBCashAccount51(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount51.class);
    }

    public static OBCashAccount60 toOBCashAccount60(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount60.class);
    }

    public static OBCashAccount61 toOBCashAccount61(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount61.class);
    }

    public static OBAccount3Account toOBAccount3Account(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBAccount3Account.class);
    }

    public static OBAccount4Account toOBAccount4Account(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBAccount4Account.class);
    }

    public static OBAccount6Account toOBAccount6Account(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBAccount6Account.class);
    }

    public static OBWriteDomestic2DataInitiationCreditorAccount toOBWriteDomestic2DataInitiationCreditorAccount(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBWriteDomestic2DataInitiationCreditorAccount.class);
    }

    public static OBWriteDomestic2DataInitiationDebtorAccount toOBWriteDomestic2DataInitiationDebtorAccount(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBWriteDomestic2DataInitiationDebtorAccount.class);
    }

    public static OBWriteDomesticStandingOrder3DataInitiationCreditorAccount toOBWriteDomesticStandingOrder3DataInitiationCreditorAccount(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBWriteDomesticStandingOrder3DataInitiationCreditorAccount.class);
    }

    public static OBWriteInternationalStandingOrder4DataInitiationCreditorAccount toOBWriteInternationalStandingOrder4DataInitiationCreditorAccount(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBWriteInternationalStandingOrder4DataInitiationCreditorAccount.class);
    }

    public static OBWriteDomesticStandingOrder3DataInitiationDebtorAccount toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBWriteDomesticStandingOrder3DataInitiationDebtorAccount.class);
    }

    public static OBCashAccountDebtor1 toOBCashAccountDebtor1(FRAccountIdentifier account) {
        return account == null ? null : new OBCashAccountDebtor1()
                .schemeName(account.getSchemeName() == null ? null : OBExternalAccountIdentification2Code.valueOfReference(account.getSchemeName()))
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification());
    }

    public static OBCashAccountCreditor1 toOBCashAccountCreditor1(FRAccountIdentifier account) {
        return account == null ? null : new OBCashAccountCreditor1()
                .schemeName(account.getSchemeName() == null ? null : OBExternalAccountIdentification2Code.valueOfReference(account.getSchemeName()))
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification());
    }

    public static OBDebtorIdentification1 toOBDebtorIdentification1(FRAccountIdentifier account) {
        return account == null ? null : new OBDebtorIdentification1()
                .name(account.getName());
    }

    // OB to FR
    public static FRAccountIdentifier toFRAccountIdentifier(OBAccount3Account account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBAccount4Account account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBAccount6Account account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount5 account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRAccountIdentifier(OBCashAccount50 account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }
}
