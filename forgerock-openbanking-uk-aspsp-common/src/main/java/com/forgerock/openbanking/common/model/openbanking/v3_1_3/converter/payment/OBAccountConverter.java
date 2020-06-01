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

import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.*;

public class OBAccountConverter {

    public static OBCashAccount3 toOBCashAccount3(OBWriteDomestic2DataInitiationDebtorAccount debtorAccount) {
        return (new OBCashAccount3())
                .schemeName(debtorAccount.getSchemeName())
                .identification(debtorAccount.getIdentification())
                .name(debtorAccount.getName())
                .secondaryIdentification(debtorAccount.getSecondaryIdentification());
    }

    public static OBCashAccount3 toOBCashAccount3(OBWriteDomestic2DataInitiationCreditorAccount creditorAccount) {
        return (new OBCashAccount3())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }

    public static OBWriteDomestic2DataInitiationDebtorAccount toOBWriteDomestic2DataInitiationDebtorAccount(OBCashAccount3 debtorAccount) {
        return (new OBWriteDomestic2DataInitiationDebtorAccount())
                .schemeName(debtorAccount.getSchemeName())
                .identification(debtorAccount.getIdentification())
                .name(debtorAccount.getName())
                .secondaryIdentification(debtorAccount.getSecondaryIdentification());
    }

    public static OBWriteDomestic2DataInitiationCreditorAccount toOBWriteDomestic2DataInitiationCreditorAccount(OBCashAccount3 creditorAccount) {
        return (new OBWriteDomestic2DataInitiationCreditorAccount())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }

    public static OBWriteDomesticStandingOrder3DataInitiationDebtorAccount toOBWriteDomesticStandingOrder3DataInitiationDebtorAccount(OBCashAccountDebtor4 debtorAccount) {
        return (new OBWriteDomesticStandingOrder3DataInitiationDebtorAccount())
                .schemeName(debtorAccount.getSchemeName())
                .identification(debtorAccount.getIdentification())
                .name(debtorAccount.getName())
                .secondaryIdentification(debtorAccount.getSecondaryIdentification());
    }

    public static OBWriteDomesticStandingOrder3DataInitiationCreditorAccount toOBWriteDomesticStandingOrder3DataInitiationCreditorAccount(OBCashAccountCreditor3 creditorAccount) {
        return (new OBWriteDomesticStandingOrder3DataInitiationCreditorAccount())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }

    public static OBCashAccountDebtor4 toOBCashAccountDebtor4(OBWriteDomesticStandingOrder3DataInitiationDebtorAccount debtorAccount) {
        return (new OBCashAccountDebtor4())
                .schemeName(debtorAccount.getSchemeName())
                .identification(debtorAccount.getIdentification())
                .name(debtorAccount.getName())
                .secondaryIdentification(debtorAccount.getSecondaryIdentification());
    }

    public static OBCashAccountCreditor3 toOBCashAccountCreditor3(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount creditorAccount) {
        return (new OBCashAccountCreditor3())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }
}
