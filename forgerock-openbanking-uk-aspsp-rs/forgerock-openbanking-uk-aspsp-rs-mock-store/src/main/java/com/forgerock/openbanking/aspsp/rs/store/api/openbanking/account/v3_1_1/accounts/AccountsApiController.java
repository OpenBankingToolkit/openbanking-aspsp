/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.accounts;


import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.account.FRAccount3;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBAccount3;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadAccount3;
import uk.org.openbanking.datamodel.account.OBReadAccount3Data;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("AccountsApiV3.1.1")
@Slf4j
public class AccountsApiController implements AccountsApi {

    @Autowired
    private FRAccount3Repository frAccount3Repository;

    public ResponseEntity<OBReadAccount3> getAccount(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true )
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        log.info("Read account {} with permission {}", accountId, permissions);
        FRAccount3 response = frAccount3Repository.byAccountId(accountId, permissions);
        final List<OBAccount3> obAccounts = Collections.singletonList(response.getAccount());

        return ResponseEntity.ok(new OBReadAccount3()
                .data(new OBReadAccount3Data().account(obAccounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    public ResponseEntity<OBReadAccount3> getAccounts(
            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") String page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        log.info("Accounts from account ids {}", accountIds);

        List<OBAccount3> accounts = frAccount3Repository.byAccountIds(accountIds, permissions)
                .stream()
                .map(FRAccount3::getAccount)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new OBReadAccount3()
                .data(new OBReadAccount3Data().account(accounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

}
