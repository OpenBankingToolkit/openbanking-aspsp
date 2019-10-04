/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v1_1.account;


import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v1_1.account.FRAccount1;
import com.forgerock.openbanking.common.services.openbanking.converter.FRAccountConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("AccountsApiV1.1")
public class AccountsApiController implements AccountsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsApiController.class);

    @Autowired
    private FRAccount3Repository frAccount3Repository;
    @Autowired
    private FRAccountConverter accountConverter;

    @Override
    public ResponseEntity<OBReadAccount1> getAccount(
            @PathVariable("AccountId") String accountId,

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
        FRAccount1 account = accountConverter.toAccount1(frAccount3Repository.byAccountId(accountId, permissions));

        return ResponseEntity.ok(new OBReadAccount1()
                .data(new OBReadDataAccount1().account(Collections.singletonList(account.getAccount())))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    @Override
    public ResponseEntity<OBReadAccount1> getAccounts(
            @RequestParam(value = "page", defaultValue = "0") int page,

            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @RequestHeader(value = "Authorization", required = true) String authorization,

            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
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

        List<OBAccount1> accounts = frAccount3Repository.byAccountIds(accountIds, permissions)
                .stream().map(a -> accountConverter.toAccount1(a).getAccount()).collect(Collectors.toList());

        return ResponseEntity.ok(new OBReadAccount1()
                .data(new OBReadDataAccount1().account(accounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }
}
