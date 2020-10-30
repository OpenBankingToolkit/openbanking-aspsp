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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v1_1.balances;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.balances.FRBalanceRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRBalance;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadBalance1;
import uk.org.openbanking.datamodel.account.OBReadBalance1Data;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRCashBalanceConverter.toOBCashBalance1;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("BalancesApiV1.1")
public class BalancesApiController implements BalancesApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(BalancesApiController.class);

    @Value("${rs.page.default.balances.size}")
    private int PAGE_LIMIT_BALANCES;
    @Autowired
    private FRBalanceRepository frBalanceRepository;

    @Override
    public ResponseEntity<OBReadBalance1> getAccountBalances(
            @PathVariable("AccountId") String accountId,

            @RequestParam(value = "page", defaultValue = "0") int page,

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @RequestHeader(value = "Authorization", required = true) String authorization,

            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl

    ) {
        LOGGER.info("Read balances for account  {} with minimumPermissions {}", accountId, permissions);
        Page<FRBalance> balances = frBalanceRepository.findByAccountId(accountId, PageRequest.of(page, PAGE_LIMIT_BALANCES));
        int totalPage = balances.getTotalPages();

        return ResponseEntity.ok(new OBReadBalance1()
                .data(new OBReadBalance1Data().balance(balances.getContent().stream()
                        .map(b -> toOBCashBalance1(b.getBalance()))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPage))
                .meta(PaginationUtil.generateMetaData(totalPage)));
    }

    @Override
    public ResponseEntity<OBReadBalance1> getBalances(
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @RequestParam(value = "page", defaultValue = "0") int page,

            @RequestHeader(value = "Authorization", required = true) String authorization,

            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl

    ) throws OBErrorResponseException {

        LOGGER.info("Reading balances from account ids {}", accountIds);
        Page<FRBalance> balances = frBalanceRepository.findByAccountIdIn(accountIds, PageRequest.of(page, PAGE_LIMIT_BALANCES));

        int totalPage = balances.getTotalPages();

        return ResponseEntity.ok(new OBReadBalance1()
                .data(new OBReadBalance1Data().balance(balances.getContent().stream()
                        .map(b -> toOBCashBalance1(b.getBalance()))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPage))
                .meta(PaginationUtil.generateMetaData(totalPage)));
    }
}
