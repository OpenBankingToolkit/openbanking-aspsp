/**
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
package com.forgerock.openbanking.common.services.openbanking.converter;

import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccount1;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.datamodel.payment.OBCashAccountCreditor3;
import uk.org.openbanking.datamodel.payment.OBExternalAccountIdentification2Code;
import uk.org.openbanking.datamodel.service.converter.OBAccountConverter;

import java.util.ArrayList;
import java.util.List;

@Service
public class FRAccountConverter {

    public static OBCashAccount5 toOBCashAccount5(OBCashAccountCreditor3 creditorAccount) {
        OBCashAccount5 obCashAccount5 = new OBCashAccount5()
                .schemeName(creditorAccount.getSchemeName())
                .identification(creditorAccount.getIdentification());

        if (creditorAccount.getName() != null) {
            obCashAccount5.name(creditorAccount.getName());
        }

        if (creditorAccount.getSecondaryIdentification() != null) {
            obCashAccount5.secondaryIdentification(creditorAccount.getSecondaryIdentification());
        }
        return obCashAccount5;

    }

    public FRAccount1 toAccount1(FRAccount2 account2) {
        FRAccount1 frAccount1 =  new FRAccount1();
        frAccount1.setId(account2.getId());
        frAccount1.setCreated(account2.getCreated());
        frAccount1.setUserID(account2.getUserID());
        frAccount1.setAccount(OBAccountConverter.toAccount1(account2.getAccount()));
        frAccount1.setCreated(account2.getCreated());
        frAccount1.setUpdated(account2.getUpdated());
        return frAccount1;
    }

    public FRAccount1 toAccount1(FRAccount3 account3) {
        FRAccount1 frAccount1 =  new FRAccount1();
        frAccount1.setId(account3.getId());
        frAccount1.setUserID(account3.getUserID());
        frAccount1.setAccount(toOBAccount1(account3.getAccount()));
        frAccount1.setCreated(account3.getCreated());
        frAccount1.setUpdated(account3.getUpdated());
        return frAccount1;
    }

    public FRAccount2 toAccount2(FRAccount1 account1) {
        FRAccount2 frAccount2 =  new FRAccount2();
        frAccount2.setId(account1.getId());
        frAccount2.setCreated(account1.getCreated());
        frAccount2.setUserID(account1.getUserID());
        frAccount2.setAccount(OBAccountConverter.toAccount2(account1.getAccount()));
        frAccount2.setCreated(account1.getCreated());
        frAccount2.setUpdated(account1.getUpdated());
        return frAccount2;
    }

    public static FRAccount3 toAccount3(FRAccount2 account2) {
        FRAccount3 frAccount3 =  new FRAccount3();
        frAccount3.setId(account2.getId());
        frAccount3.setUserID(account2.getUserID());
        frAccount3.setAccount(toOBAccount3(account2.getAccount()));
        frAccount3.setCreated(account2.getCreated());
        frAccount3.setUpdated(account2.getUpdated());
        frAccount3.setLatestStatementId(account2.getLatestStatementId());
        return frAccount3;
    }

    public static OBAccount3 toOBAccount3(OBAccount2 account2) {
        OBAccount3 account3 =new OBAccount3()
                .accountId(account2.getAccountId())
                .currency(account2.getCurrency())
                .accountSubType(OBExternalAccountSubType1Code.CURRENTACCOUNT)
                .accountType(OBExternalAccountType1Code.PERSONAL);
        if (account2.getNickname() != null) {
            account3.nickname(account2.getNickname());
        }

        List<OBCashAccount5> obCashAccount5List = new ArrayList<>();
        for (OBCashAccount3 cashAccount3 : account2.getAccount()) {
            OBCashAccount5 account5Account = new OBCashAccount5()
                    .schemeName(cashAccount3.getSchemeName())
                    .identification(cashAccount3.getIdentification());
            if (cashAccount3.getName() != null) {
                account5Account.name(cashAccount3.getName());
            }

            if (cashAccount3.getSecondaryIdentification() != null) {
                account5Account.secondaryIdentification(cashAccount3.getSecondaryIdentification());
            }
            obCashAccount5List.add(account5Account);
        }
        account3.account(obCashAccount5List);

        if (account2.getServicer() != null) {
            account3.servicer(
                    new OBBranchAndFinancialInstitutionIdentification5()
                            .schemeName(account2.getServicer().getSchemeName())
                            .identification(account2.getServicer().getIdentification()));
        }

        return account3;
    }

    public static OBCashAccount5 toOBCashAccount5(OBCashAccount3 obCashAccount3) {
        OBCashAccount5 obCashAccount5 = new OBCashAccount5()
                .schemeName(obCashAccount3.getSchemeName())
                .identification(obCashAccount3.getIdentification());

        if (obCashAccount3.getName() != null) {
            obCashAccount5.name(obCashAccount3.getName());
        }

        if (obCashAccount3.getSecondaryIdentification() != null) {
            obCashAccount5.secondaryIdentification(obCashAccount3.getSecondaryIdentification());
        }
        return obCashAccount5;
    }

    public static OBCashAccount3 toOBCashAccount3(OBCashAccount5 obCashAccount5) {
        OBCashAccount3 account3Account = new OBCashAccount3()
                .schemeName(obCashAccount5.getSchemeName())
                .identification(obCashAccount5.getIdentification());

        if (obCashAccount5.getName() != null) {
            account3Account.name(obCashAccount5.getName());
        }

        if (obCashAccount5.getSecondaryIdentification() != null) {
            account3Account.secondaryIdentification(obCashAccount5.getSecondaryIdentification());
        }
        return account3Account;
    }


    public static OBCashAccount1 toOBCashAccount1(OBCashAccount5 obCashAccount5) {
        OBCashAccount1 account3Account = new OBCashAccount1()
                .schemeName(OBExternalAccountIdentification2Code.valueOfReference(obCashAccount5.getSchemeName()))
                .identification(obCashAccount5.getIdentification());

        if (obCashAccount5.getName() != null) {
            account3Account.name(obCashAccount5.getName());
        }

        if (obCashAccount5.getSecondaryIdentification() != null) {
            account3Account.secondaryIdentification(obCashAccount5.getSecondaryIdentification());
        }
        return account3Account;
    }

    public static OBAccount2 toOBAccount2(OBAccount3 account3) {
        OBAccount2 account2 =new OBAccount2()
                .accountId(account3.getAccountId())
                .currency(account3.getCurrency())
                .accountSubType(account3.getAccountSubType())
                .accountType(account3.getAccountType())
                .description(account3.getDescription());
        if (account3.getNickname() != null) {
            account2.nickname(account3.getNickname());
        }

        List<OBCashAccount3> obCashAccount3List = new ArrayList<>();
        for (OBCashAccount5 cashAccount5 : account3.getAccount()) {
            obCashAccount3List.add(toOBCashAccount3(cashAccount5));
        }
        account2.account(obCashAccount3List);

        if (account3.getServicer() != null) {
            account2.servicer(
                    new OBBranchAndFinancialInstitutionIdentification4()
                            .schemeName(account3.getServicer().getSchemeName())
                            .identification(account3.getServicer().getIdentification()));
        }

        return account2;
    }

    private OBAccount1 toOBAccount1(OBAccount3 account3) {
        OBAccount1 account1 =new OBAccount1()
                .accountId(account3.getAccountId())
                .currency(account3.getCurrency());
        if (account3.getNickname() != null) {
            account1.nickname(account3.getNickname());
        }

        List<OBCashAccount1> obCashAccountList = new ArrayList<>();
        for (OBCashAccount5 cashAccount5 : account3.getAccount()) {
            obCashAccountList.add(toOBCashAccount1(cashAccount5));
        }
        account1.account(obCashAccountList.get(0));

        if (account3.getServicer() != null) {
            account1.servicer(
                    new OBBranchAndFinancialInstitutionIdentification2()
                            .schemeName(OBExternalFinancialInstitutionIdentification2Code.fromValue(account3.getServicer().getSchemeName()))
                            .identification(account3.getServicer().getIdentification()));
        }

        return account1;
    }

}
