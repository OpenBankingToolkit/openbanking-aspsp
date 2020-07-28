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

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccount1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRAccount4;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBCashAccountCreditor3;
import uk.org.openbanking.datamodel.payment.OBExternalAccountIdentification2Code;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticStandingOrder3DataInitiationCreditorAccount;
import uk.org.openbanking.datamodel.service.converter.account.OBAccountConverter;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification4;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.OBBranchAndFinancialInstitutionIdentificationConverter.toOBBranchAndFinancialInstitutionIdentification5;
import static java.util.Collections.emptyList;

@Service
public class FRAccountConverter {

    public FRAccount1 toAccount1(FRAccount2 account2) {
        FRAccount1 frAccount1 = new FRAccount1();
        frAccount1.setId(account2.getId());
        frAccount1.setCreated(account2.getCreated());
        frAccount1.setUserID(account2.getUserID());
        frAccount1.setAccount(OBAccountConverter.toAccount1(account2.getAccount()));
        frAccount1.setCreated(account2.getCreated());
        frAccount1.setUpdated(account2.getUpdated());
        return frAccount1;
    }

    public FRAccount1 toAccount1(FRAccount3 account3) {
        FRAccount1 frAccount1 = new FRAccount1();
        frAccount1.setId(account3.getId());
        frAccount1.setUserID(account3.getUserID());
        frAccount1.setAccount(toOBAccount1(account3.getAccount()));
        frAccount1.setCreated(account3.getCreated());
        frAccount1.setUpdated(account3.getUpdated());
        return frAccount1;
    }

    public FRAccount1 toAccount1(FRAccount4 account4) {
        FRAccount1 frAccount1 = new FRAccount1();
        frAccount1.setId(account4.getId());
        frAccount1.setUserID(account4.getUserID());
        frAccount1.setAccount(toOBAccount1(account4.getAccount()));
        frAccount1.setCreated(account4.getCreated());
        frAccount1.setUpdated(account4.getUpdated());
        return frAccount1;
    }

    public FRAccount2 toAccount2(FRAccount1 account1) {
        FRAccount2 frAccount2 = new FRAccount2();
        frAccount2.setId(account1.getId());
        frAccount2.setCreated(account1.getCreated());
        frAccount2.setUserID(account1.getUserID());
        frAccount2.setAccount(OBAccountConverter.toAccount2(account1.getAccount()));
        frAccount2.setCreated(account1.getCreated());
        frAccount2.setUpdated(account1.getUpdated());
        return frAccount2;
    }

    public static FRAccount3 toAccount3(FRAccount2 account2) {
        FRAccount3 frAccount3 = new FRAccount3();
        frAccount3.setId(account2.getId());
        frAccount3.setUserID(account2.getUserID());
        frAccount3.setAccount(toOBAccount3(account2.getAccount()));
        frAccount3.setCreated(account2.getCreated());
        frAccount3.setUpdated(account2.getUpdated());
        frAccount3.setLatestStatementId(account2.getLatestStatementId());
        return frAccount3;
    }

    public static OBAccount1 toOBAccount1(OBAccount3 obAccount3) {
        return obAccount3 == null ? null : (new OBAccount1())
                .accountId(obAccount3.getAccountId())
                .currency(obAccount3.getCurrency())
                .nickname(obAccount3.getNickname())
                .account(toOBCashAccount1(obAccount3.getAccount().get(0)))
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(obAccount3.getServicer()));
    }

    public static OBAccount1 toOBAccount1(OBAccount4 obAccount4) {
        return obAccount4 == null ? null : (new OBAccount1())
                .accountId(obAccount4.getAccountId())
                .currency(obAccount4.getCurrency())
                .nickname(obAccount4.getNickname())
                .account(toOBCashAccount1(obAccount4.getAccount().get(0)))
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(obAccount4.getServicer()));
    }

    public static OBAccount1 toOBAccount1(OBAccount6 obAccount6) {
        return obAccount6 == null ? null : (new OBAccount1())
                .accountId(obAccount6.getAccountId())
                .currency(obAccount6.getCurrency())
                .nickname(obAccount6.getNickname())
                .account(toOBCashAccount1(obAccount6.getAccount().get(0)))
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(obAccount6.getServicer()));
    }

    public static OBAccount2 toOBAccount2(OBAccount3 obAccount3) {
        return obAccount3 == null ? null : (new OBAccount2())
                .accountId(obAccount3.getAccountId())
                .currency(obAccount3.getCurrency())
                .accountType(obAccount3.getAccountType())
                .accountSubType(obAccount3.getAccountSubType())
                .description(obAccount3.getDescription())
                .nickname(obAccount3.getNickname())
                .account(fromOBCashAccount5ToOBCashAccount3List(obAccount3.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification4(obAccount3.getServicer()));
    }

    public static OBAccount2 toOBAccount2(OBAccount6 obAccount6) {
        return obAccount6 == null ? null : (new OBAccount2())
                .accountId(obAccount6.getAccountId())
                .currency(obAccount6.getCurrency())
                .accountType(obAccount6.getAccountType())
                .accountSubType(obAccount6.getAccountSubType())
                .description(obAccount6.getDescription())
                .nickname(obAccount6.getNickname())
                .account(fromOBAccount3AccountToOBCashAccount3List(obAccount6.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification4(obAccount6.getServicer()));
    }

    public static OBCashAccount1 toOBCashAccount1(OBCashAccount5 obCashAccount5) {
        return obCashAccount5 == null ? null : (new OBCashAccount1())
                .schemeName(OBExternalAccountIdentification2Code.valueOfReference(obCashAccount5.getSchemeName()))
                .identification(obCashAccount5.getIdentification())
                .name(obCashAccount5.getName())
                .secondaryIdentification(obCashAccount5.getSecondaryIdentification());
    }

    public static OBCashAccount1 toOBCashAccount1(OBAccount3Account obAccount3Account) {
        return obAccount3Account == null ? null : (new OBCashAccount1())
                .schemeName(OBExternalAccountIdentification2Code.valueOfReference(obAccount3Account.getSchemeName()))
                .identification(obAccount3Account.getIdentification())
                .name(obAccount3Account.getName())
                .secondaryIdentification(obAccount3Account.getSecondaryIdentification());
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount5 obCashAccount5) {
        return obCashAccount5 == null ? null : (new OBCashAccount3())
                .schemeName(obCashAccount5.getSchemeName())
                .identification(obCashAccount5.getIdentification())
                .name(obCashAccount5.getName())
                .secondaryIdentification(obCashAccount5.getSecondaryIdentification());
    }

    public static OBCashAccount3 toOBCashAccount3(OBAccount3Account obAccount3Account) {
        return obAccount3Account == null ? null : (new OBCashAccount3())
                .schemeName(obAccount3Account.getSchemeName())
                .identification(obAccount3Account.getIdentification())
                .name(obAccount3Account.getName())
                .secondaryIdentification(obAccount3Account.getSecondaryIdentification());
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount51 obCashAccount51) {
        return obCashAccount51 == null ? null : (new OBCashAccount3())
                .schemeName(obCashAccount51.getSchemeName())
                .identification(obCashAccount51.getIdentification())
                .name(obCashAccount51.getName())
                .secondaryIdentification(obCashAccount51.getSecondaryIdentification());
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount3 obCashAccount3) {
        return obCashAccount3 == null ? null : (new OBCashAccount5())
                .schemeName(obCashAccount3.getSchemeName())
                .identification(obCashAccount3.getIdentification())
                .name(obCashAccount3.getName())
                .secondaryIdentification(obCashAccount3.getSecondaryIdentification());
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccountCreditor3 obCashAccountCreditor3) {
        return obCashAccountCreditor3 == null ? null : (new OBCashAccount5())
                .schemeName(obCashAccountCreditor3.getSchemeName())
                .identification(obCashAccountCreditor3.getIdentification())
                .name(obCashAccountCreditor3.getName())
                .secondaryIdentification(obCashAccountCreditor3.getSecondaryIdentification());
    }

    public static OBCashAccount5 toOBCashAccount5(OBAccount3Account obAccount3Account) {
        return obAccount3Account == null ? null : (new OBCashAccount5())
                .schemeName(obAccount3Account.getSchemeName())
                .identification(obAccount3Account.getIdentification())
                .name(obAccount3Account.getName())
                .secondaryIdentification(obAccount3Account.getSecondaryIdentification());
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount51 obCashAccount51) {
        return obCashAccount51 == null ? null : (new OBCashAccount5())
                .schemeName(obCashAccount51.getSchemeName())
                .identification(obCashAccount51.getIdentification())
                .name(obCashAccount51.getName())
                .secondaryIdentification(obCashAccount51.getSecondaryIdentification());
    }

    public static OBCashAccount5 toOBCashAccount5(OBWriteDomesticStandingOrder3DataInitiationCreditorAccount creditorAccount) {
        return creditorAccount == null ? null : (new OBCashAccount5())
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification())
                .name(creditorAccount.getName())
                .secondaryIdentification(creditorAccount.getSecondaryIdentification());
    }

    public static OBAccount3 toOBAccount3(OBAccount2 account2) {
        return account2 == null ? null : (new OBAccount3())
                .accountId(account2.getAccountId())
                .currency(account2.getCurrency())
                .accountType(account2.getAccountType())
                .accountSubType(account2.getAccountSubType())
                .description(account2.getDescription())
                .nickname(account2.getNickname())
                .account(fromOBCashAccount3ToOBCashAccount5List(account2.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification5(account2.getServicer()));
    }

    public static OBAccount3 toOBAccount3(OBAccount6 obAccount6) {
        return obAccount6 == null ? null : (new OBAccount3())
                .accountId(obAccount6.getAccountId())
                .currency(obAccount6.getCurrency())
                .accountType(obAccount6.getAccountType())
                .accountSubType(obAccount6.getAccountSubType())
                .description(obAccount6.getDescription())
                .nickname(obAccount6.getNickname())
                .account(fromOBAccount3AccountToOBCashAccount5List(obAccount6.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification5(obAccount6.getServicer()));
    }

    private static List<OBCashAccount3> fromOBAccount3AccountToOBCashAccount3List(List<OBAccount3Account> accounts) {
        if (accounts == null) {
            return emptyList();
        }
        return accounts.stream()
                .map(FRAccountConverter::toOBCashAccount3)
                .collect(Collectors.toList());
    }

    private static List<OBCashAccount3> fromOBCashAccount5ToOBCashAccount3List(List<OBCashAccount5> accounts) {
        if (accounts == null) {
            return emptyList();
        }
        return accounts.stream()
                .map(FRAccountConverter::toOBCashAccount3)
                .collect(Collectors.toList());
    }

    private static List<OBCashAccount5> fromOBCashAccount3ToOBCashAccount5List(List<OBCashAccount3> accounts) {
        if (accounts == null) {
            return emptyList();
        }
        return accounts.stream()
                .map(FRAccountConverter::toOBCashAccount5)
                .collect(Collectors.toList());
    }

    private static List<OBCashAccount5> fromOBAccount3AccountToOBCashAccount5List(List<OBAccount3Account> accounts) {
        if (accounts == null) {
            return emptyList();
        }
        return accounts.stream()
                .map(FRAccountConverter::toOBCashAccount5)
                .collect(Collectors.toList());
    }

}
