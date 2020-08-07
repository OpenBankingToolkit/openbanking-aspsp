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

import com.forgerock.openbanking.common.services.openbanking.converter.FRModelMapper;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBCashAccountCreditor3;
import uk.org.openbanking.datamodel.payment.OBCashAccountDebtor4;
import uk.org.openbanking.datamodel.payment.OBExternalAccountIdentification2Code;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationCreditorAccount;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalStandingOrder4DataInitiationCreditorAccount;

public class OBCashAccountConverter {
    public static OBCashAccount3 toOBCashAccount3(OBCashAccount6 obCashAccount6) {
        return FRModelMapper.map(obCashAccount6, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccountCreditor3 obCashAccountCreditor3) {
        return FRModelMapper.map(obCashAccountCreditor3, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccountDebtor4 obCashAccountDebtor4) {
        return FRModelMapper.map(obCashAccountDebtor4, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount5 obCashAccount5) {
        return FRModelMapper.map(obCashAccount5, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount50 obCashAccount50) {
        return FRModelMapper.map(obCashAccount50, OBCashAccount3.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccount5.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount50 obCashAccount50Û) {
        return FRModelMapper.map(obCashAccount50Û, OBCashAccount5.class);
    }

    public static OBCashAccountDebtor4 toOBCashAccountDebtor4(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccountDebtor4.class);
    }

    public static OBCashAccountCreditor3 toOBCashAccountCreditor3(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccountCreditor3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBAccount3Account obAccount3Account) {
        return FRModelMapper.map(obAccount3Account, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount51 obCashAccount51) {
        return FRModelMapper.map(obCashAccount51, OBCashAccount3.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccountCreditor3 obCashAccountCreditor3) {
        return FRModelMapper.map(obCashAccountCreditor3, OBCashAccount5.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBAccount3Account obAccount3Account) {
        return FRModelMapper.map(obAccount3Account, OBCashAccount5.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount51 obCashAccount51) {
        return FRModelMapper.map(obCashAccount51, OBCashAccount5.class);
    }

    public static OBCashAccount5 toOBCashAccount5(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount creditorAccount) {
        return FRModelMapper.map(creditorAccount, OBCashAccount5.class);
    }

    public static OBCashAccount51 toOBCashAccount51(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccount51.class);
    }

    public static OBCashAccount6 toOBCashAccount6(OBCashAccount3 obCashAccount3) {
        return FRModelMapper.map(obCashAccount3, OBCashAccount6.class);
    }

    public static OBCashAccount6 toOBCashAccount6(OBCashAccount60 obCashAccount60) {
        return FRModelMapper.map(obCashAccount60, OBCashAccount6.class);
    }

    public static OBCashAccount6 toOBCashAccount6(OBCashAccount61 obCashAccount61) {
        return FRModelMapper.map(obCashAccount61, OBCashAccount6.class);
    }

    public static OBCashAccount6 toOBCashAccount6(OBCashAccount51 obCashAccount51) {
        return FRModelMapper.map(obCashAccount51, OBCashAccount6.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount60 obCashAccount60) {
        return FRModelMapper.map(obCashAccount60, OBCashAccount3.class);
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount61 obCashAccount61) {
        return FRModelMapper.map(obCashAccount61, OBCashAccount3.class);
    }

    public static OBCashAccount2 toOBCashAccount2(OBCashAccount60 obCashAccount60) {
        return FRModelMapper.map(obCashAccount60, OBCashAccount2.class);
    }

    public static OBCashAccount51 toOBCashAccount51(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount creditorAccount) {
        return FRModelMapper.map(creditorAccount, OBCashAccount51.class);
    }

    public static OBCashAccount51 toOBCashAccount51(OBWriteInternationalStandingOrder4DataInitiationCreditorAccount creditorAccount) {
        return FRModelMapper.map(creditorAccount, OBCashAccount51.class);
    }

    // cannot use model mapper due to OBExternalAccountIdentification2Code
    public static OBCashAccount1 toOBCashAccount1(OBCashAccount5 obCashAccount5) {
        return obCashAccount5 == null ? null : (new OBCashAccount1())
                .schemeName(obCashAccount5.getSchemeName() == null ? null : OBExternalAccountIdentification2Code.valueOf(obCashAccount5.getSchemeName()))
                .identification(obCashAccount5.getIdentification())
                .name(obCashAccount5.getName())
                .secondaryIdentification(obCashAccount5.getSecondaryIdentification());
    }

    public static OBCashAccount1 toOBCashAccount1(OBCashAccount50 obCashAccount50) {
        return obCashAccount50 == null ? null : (new OBCashAccount1())
                .schemeName(obCashAccount50.getSchemeName() == null ? null : OBExternalAccountIdentification2Code.valueOf(obCashAccount50.getSchemeName()))
                .identification(obCashAccount50.getIdentification())
                .name(obCashAccount50.getName())
                .secondaryIdentification(obCashAccount50.getSecondaryIdentification());
    }

    public static OBCashAccount1 toOBCashAccount1(OBCashAccount51 obCashAccount51) {
        return obCashAccount51 == null ? null : (new OBCashAccount1())
                .schemeName(obCashAccount51.getSchemeName() == null ? null : OBExternalAccountIdentification2Code.valueOf(obCashAccount51.getSchemeName()))
                .identification(obCashAccount51.getIdentification())
                .name(obCashAccount51.getName())
                .secondaryIdentification(obCashAccount51.getSecondaryIdentification());
    }

    public static OBCashAccount1 toOBCashAccount1(OBAccount3Account obAccount3Account) {
        return obAccount3Account == null ? null : (new OBCashAccount1())
                .schemeName(obAccount3Account.getSchemeName() == null ? null : OBExternalAccountIdentification2Code.valueOf(obAccount3Account.getSchemeName()))
                .identification(obAccount3Account.getIdentification())
                .name(obAccount3Account.getName())
                .secondaryIdentification(obAccount3Account.getSecondaryIdentification());
    }
}
