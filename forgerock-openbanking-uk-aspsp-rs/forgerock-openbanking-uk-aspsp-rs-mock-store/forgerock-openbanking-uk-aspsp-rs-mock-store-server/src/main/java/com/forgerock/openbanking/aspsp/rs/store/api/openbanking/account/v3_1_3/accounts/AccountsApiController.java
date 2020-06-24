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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.accounts;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller("AccountsApiV3.1.3")
@Slf4j
public class AccountsApiController implements AccountsApi {

    private final FRAccount3Repository frAccount3Repository;

    public AccountsApiController(FRAccount3Repository frAccount3Repository) {
        this.frAccount3Repository = frAccount3Repository;
    }

    @Override
    public ResponseEntity<OBReadAccount5> getAccount(String accountId,
                                                     String authorization,
                                                     DateTime xFapiAuthDate,
                                                     String xFapiCustomerIpAddress,
                                                     String xFapiInteractionId,
                                                     String xCustomerUserAgent,
                                                     List<OBExternalPermissions1Code> permissions,
                                                     String httpUrl) throws OBErrorResponseException {
        log.info("Read account {} with permission {}", accountId, permissions);
        FRAccount3 response = frAccount3Repository.byAccountId(accountId, permissions);

        List<OBAccount6> obAccounts = Collections.singletonList(toOBAccount6(response.getAccount()));
        return ResponseEntity.ok(new OBReadAccount5()
                .data(new OBReadAccount5Data().account(obAccounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    @Override
    public ResponseEntity<OBReadAccount5> getAccounts(String page,
                                                      String authorization,
                                                      DateTime xFapiAuthDate,
                                                      String xFapiCustomerIpAddress,
                                                      String xFapiInteractionId,
                                                      String xCustomerUserAgent,
                                                      List<String> accountIds,
                                                      List<OBExternalPermissions1Code> permissions,
                                                      String httpUrl) throws OBErrorResponseException {
        log.info("Accounts from account ids {}", accountIds);

        List<FRAccount3> frAccounts = frAccount3Repository.byAccountIds(accountIds, permissions);
        List<OBAccount6> obAccounts = frAccounts
                .stream()
                .map(frAccount -> toOBAccount6(frAccount.getAccount()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new OBReadAccount5()
                .data(new OBReadAccount5Data().account(obAccounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    // TODO #232 - move to uk-datamodel repo
    public static OBAccount6 toOBAccount6(OBAccount3 account) {
        DateTime now = DateTime.now();
        return account == null ? null : (new OBAccount6())
                .accountId(account.getAccountId())
                .status(OBAccountStatus1Code.ENABLED) // TODO #232 - populate this field from the datasource?
                .statusUpdateDateTime(now) // TODO #232 - populate this field from the datasource?
                .currency(account.getCurrency())
                .accountType(account.getAccountType())
                .accountSubType(account.getAccountSubType())
                .description(account.getDescription())
                .nickname(account.getNickname())
                .openingDate(now) // TODO #232 - populate this field from the datasource?
                .maturityDate(now) // TODO #232 - populate this field from the datasource?
                .account(toOBAccount3Accounts(account.getAccount()))
                .servicer(toOBBranchAndFinancialInstitutionIdentification50(account.getServicer()));
    }

    public static List<OBAccount3Account> toOBAccount3Accounts(List<OBCashAccount5> accounts) {
        return accounts == null ? null : accounts
                .stream()
                .map(a -> toOBAccount3Account(a))
                .collect(Collectors.toList());
    }

    public static OBAccount3Account toOBAccount3Account(OBCashAccount5 obCashAccount5) {
        return obCashAccount5 == null ? null : (new OBAccount3Account())
                .schemeName(obCashAccount5.getSchemeName())
                .identification(obCashAccount5.getIdentification())
                .name(obCashAccount5.getName())
                .secondaryIdentification(obCashAccount5.getSecondaryIdentification());
    }

    public static OBBranchAndFinancialInstitutionIdentification50 toOBBranchAndFinancialInstitutionIdentification50(OBBranchAndFinancialInstitutionIdentification5 obBranchAndFinancialInstitutionIdentification5) {
        return obBranchAndFinancialInstitutionIdentification5 == null ? null : (new OBBranchAndFinancialInstitutionIdentification50())
                .schemeName(obBranchAndFinancialInstitutionIdentification5.getSchemeName())
                .identification(obBranchAndFinancialInstitutionIdentification5.getIdentification());
    }
}
