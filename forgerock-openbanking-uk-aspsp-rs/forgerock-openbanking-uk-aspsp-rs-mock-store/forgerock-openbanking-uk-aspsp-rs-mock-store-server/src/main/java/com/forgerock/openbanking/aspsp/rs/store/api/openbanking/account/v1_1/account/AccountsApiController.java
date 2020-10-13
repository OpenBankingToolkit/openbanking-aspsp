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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v1_1.account;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBAccount1;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadAccount1;
import uk.org.openbanking.datamodel.account.OBReadDataAccount1;

import java.util.Collections;
import java.util.List;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRExternalPermissionsCodeConverter.toFRExternalPermissionsCodeList;
import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRFinancialAccountConverter.toOBAccount1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static java.util.stream.Collectors.toList;

@Controller("AccountsApiV1.1")
public class AccountsApiController implements AccountsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsApiController.class);

    private final FRAccountRepository frAccountRepository;

    public AccountsApiController(FRAccountRepository frAccountRepository) {
        this.frAccountRepository = frAccountRepository;
    }

    @Override
    public ResponseEntity<OBReadAccount1> getAccount(
            @PathVariable("AccountId") String accountId,

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @RequestHeader(value = "Authorization", required = true) String authorization,

            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl

    ) {
        FRAccount account = frAccountRepository.byAccountId(accountId, toFRExternalPermissionsCodeList(permissions));

        return ResponseEntity.ok(new OBReadAccount1()
                .data(new OBReadDataAccount1().account(Collections.singletonList(toOBAccount1(account.getAccount()))))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    @Override
    public ResponseEntity<OBReadAccount1> getAccounts(
            @RequestParam(value = "page", defaultValue = "0") int page,

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @RequestHeader(value = "Authorization", required = true) String authorization,

            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) {

        LOGGER.info("Read all accounts {} with minimumPermissions {}", accountIds,
                permissions);

        List<FRAccount> frAccounts = frAccountRepository.byAccountIds(accountIds, toFRExternalPermissionsCodeList(permissions));
        List<OBAccount1> accounts = frAccounts.stream()
                .map(a -> toOBAccount1(a.getAccount()))
                .collect(toList());

        return ResponseEntity.ok(new OBReadAccount1()
                .data(new OBReadDataAccount1().account(accounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }
}
