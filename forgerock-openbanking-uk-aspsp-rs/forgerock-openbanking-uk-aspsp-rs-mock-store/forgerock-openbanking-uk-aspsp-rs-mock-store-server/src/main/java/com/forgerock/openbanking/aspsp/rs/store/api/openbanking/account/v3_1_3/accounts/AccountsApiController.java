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

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.accounts.FRAccount4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRAccount4;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBAccount6;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadAccount5;
import uk.org.openbanking.datamodel.account.OBReadAccount5Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller("AccountsApiV3.1.3")
@Slf4j
public class AccountsApiController implements AccountsApi {

    private final FRAccount4Repository frAccount4Repository;

    public AccountsApiController(FRAccount4Repository frAccount4Repository) {
        this.frAccount4Repository = frAccount4Repository;
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
        FRAccount4 response = frAccount4Repository.byAccountId(accountId, permissions);

        List<OBAccount6> obAccounts = Collections.singletonList(response.getAccount());
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

        List<FRAccount4> frAccounts = frAccount4Repository.byAccountIds(accountIds, permissions);
        List<OBAccount6> obAccounts = frAccounts
                .stream()
                .map(frAccount -> frAccount.getAccount())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new OBReadAccount5()
                .data(new OBReadAccount5Data().account(obAccounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

}
