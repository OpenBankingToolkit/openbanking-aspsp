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
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.account.OBCashAccount5;
import uk.org.openbanking.datamodel.account.OBCashAccount51;
import uk.org.openbanking.datamodel.payment.*;

public class FRFinancialAccountConverter {

    // OB to FR
    public static FRAccountIdentifier toFRFinancialAccount(OBCashAccount3 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBCashAccountDebtor4 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBCashAccountCreditor3 account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBWriteDomestic2DataInitiationCreditorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBWriteDomestic2DataInitiationDebtorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBWriteInternationalStandingOrder4DataInitiationCreditorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBWriteDomesticStandingOrder3DataInitiationDebtorAccount account) {
        return FRModelMapper.map(account, FRAccountIdentifier.class);
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBCashAccountDebtor1 account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName() == null ? null : account.getSchemeName().getReference())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    public static FRAccountIdentifier toFRFinancialAccount(OBCashAccountCreditor1 account) {
        return account == null ? null : FRAccountIdentifier.builder()
                .schemeName(account.getSchemeName() == null ? null : account.getSchemeName().getReference())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification())
                .build();
    }

    // FR to OB
    public static OBCashAccountCreditor3 toOBCashAccountCreditor3(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccountCreditor3.class);
    }

    public static OBCashAccountDebtor4 toOBCashAccountDebtor4(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccountDebtor4.class);
    }

    public static OBCashAccount3 toOBCashAccount3(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount3.class);
    }

    public static OBCashAccount5 toOBCashAccount5(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount5.class);
    }

    public static OBCashAccount51 toOBCashAccount51(FRAccountIdentifier account) {
        return FRModelMapper.map(account, OBCashAccount51.class);
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
}
