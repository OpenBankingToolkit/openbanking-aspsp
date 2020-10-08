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

import com.forgerock.openbanking.common.model.openbanking.domain.account.FRFinancialAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter;
import uk.org.openbanking.datamodel.account.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification2;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification4;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification5;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountServicerConverter.toOBBranchAndFinancialInstitutionIdentification50;
import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBCashAccount1;

public class FRFinancialAccountConverter {

    public static OBAccount1 toOBAccount1(FRFinancialAccount account) {
        return account == null ? null : new OBAccount1()
                .accountId(account.getAccountId())
                .currency(account.getCurrency())
                .nickname(account.getNickname())
                .account(toOBCashAccount1(account.getAccount().get(0)))
                .servicer(toOBBranchAndFinancialInstitutionIdentification2(account.getServicer()));
    }

    public static OBAccount2 toOBAccount2(FRFinancialAccount account) {
        return account == null ? null : new OBAccount2()
                .accountId(account.getAccountId())
                .currency(account.getCurrency())
                .accountType(toOBExternalAccountType1Code(account.getAccountType()))
                .accountSubType(toOBExternalAccountSubType1Code(account.getAccountSubType()))
                .description(account.getDescription())
                .nickname(account.getNickname())
                .account(toOBCashAccount3List(account.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification4(account.getServicer()));

    }

    public static OBAccount3 toOBAccount3(FRFinancialAccount account) {
        return account == null ? null : new OBAccount3()
                .accountId(account.getAccountId())
                .currency(account.getCurrency())
                .accountType(toOBExternalAccountType1Code(account.getAccountType()))
                .accountSubType(toOBExternalAccountSubType1Code(account.getAccountSubType()))
                .description(account.getDescription())
                .nickname(account.getNickname())
                .account(toOBCashAccount5List(account.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification5(account.getServicer()));
    }

    public static OBAccount6 toOBAccount6(FRFinancialAccount account) {
        return account == null ? null : new OBAccount6()
                .accountId(account.getAccountId())
                .status(toOBAccountStatus1Code(account.getStatus()))
                .statusUpdateDateTime(account.getStatusUpdateDateTime())
                .currency(account.getCurrency())
                .accountType(toOBExternalAccountType1Code(account.getAccountType()))
                .accountSubType(toOBExternalAccountSubType1Code(account.getAccountSubType()))
                .description(account.getDescription())
                .nickname(account.getNickname())
                .openingDate(account.getOpeningDate())
                .maturityDate(account.getMaturityDate())
                .account(toOBAccount3AccountList(account.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification50(account.getServicer()));
    }

    public static OBAccountStatus1Code toOBAccountStatus1Code(FRFinancialAccount.FRAccountStatusCode status) {
        return status == null ? null : OBAccountStatus1Code.valueOf(status.name());
    }

    public static OBExternalAccountType1Code toOBExternalAccountType1Code(FRFinancialAccount.FRAccountTypeCode accountType) {
        return accountType == null ? null : OBExternalAccountType1Code.valueOf(accountType.name());
    }

    public static OBExternalAccountSubType1Code toOBExternalAccountSubType1Code(FRFinancialAccount.FRAccountSubTypeCode accountSubType) {
        return accountSubType == null ? null : OBExternalAccountSubType1Code.valueOf(accountSubType.name());
    }

    public static List<OBCashAccount3> toOBCashAccount3List(List<FRAccountIdentifier> accounts) {
        return accounts == null ? null : accounts.stream()
                .map(FRAccountIdentifierConverter::toOBCashAccount3)
                .collect(Collectors.toList());
    }

    public static List<OBCashAccount5> toOBCashAccount5List(List<FRAccountIdentifier> accounts) {
        return accounts == null ? null : accounts.stream()
                .map(FRAccountIdentifierConverter::toOBCashAccount5)
                .collect(Collectors.toList());
    }

    public static List<OBAccount3Account> toOBAccount3AccountList(List<FRAccountIdentifier> accounts) {
        return accounts == null ? null : accounts.stream()
                .map(FRAccountIdentifierConverter::toOBAccount3Account)
                .collect(Collectors.toList());
    }
}
