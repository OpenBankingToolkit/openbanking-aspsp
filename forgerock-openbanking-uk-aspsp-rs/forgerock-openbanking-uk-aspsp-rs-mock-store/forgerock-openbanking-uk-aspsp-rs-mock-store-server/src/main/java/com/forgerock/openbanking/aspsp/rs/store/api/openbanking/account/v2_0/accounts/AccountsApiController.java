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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.accounts;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.accounts.FRAccountRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRAccount;
import com.forgerock.openbanking.common.services.openbanking.converter.account.FRAccountConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
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
import uk.org.openbanking.datamodel.account.OBAccount2;
import uk.org.openbanking.datamodel.account.OBAccount3Account;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification3Code;
import uk.org.openbanking.datamodel.account.OBExternalAccountIdentification4Code;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadAccount2;
import uk.org.openbanking.datamodel.account.OBReadAccount2Data;
import uk.org.openbanking.datamodel.service.converter.account.OBExternalAccountIdentificationConverter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("AccountsApiV2.0")
public class AccountsApiController implements AccountsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsApiController.class);

    @Autowired
    private FRAccountRepository frAccountRepository;

    public ResponseEntity<OBReadAccount2> getAccount(
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

        LOGGER.info("Read account {} with permission {}", accountId, permissions);
        FRAccount response = frAccountRepository.byAccountId(accountId, permissions);
        convertAccounts(response);

        final List<OBAccount2> obAccount2s = Collections.singletonList(
                FRAccountConverter.toOBAccount2(response.getAccount())
        );

        return ResponseEntity.ok(new OBReadAccount2()
                .data(new OBReadAccount2Data().account(obAccount2s))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    public ResponseEntity<OBReadAccount2> getAccounts(
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

        LOGGER.info("Accounts from account ids {}", accountIds);

        List<OBAccount2> accounts = frAccountRepository.byAccountIds(accountIds, permissions)
                .stream()
                .map(this::convertAccounts)
                .map(FRAccount::getAccount)
                .map(FRAccountConverter::toOBAccount2)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new OBReadAccount2()
                .data(new OBReadAccount2Data().account(accounts))
                .links(PaginationUtil.generateLinksOnePager(httpUrl))
                .meta(PaginationUtil.generateMetaData(1)));
    }

    /**
     * Because we always use latest model in persistent store, older API controller may need to do conversions on some of values e.g. the Account identifier codes.
     * This can be overidden is later versions of controller.
     */
    protected FRAccount convertAccounts(FRAccount account) {
        if (account.getAccount().getAccount() != null) {
            account.getAccount().getAccount()
                    .forEach(this::checkAndConvertV3SchemeNameToV2);
        }
        return account;
    }

    // This is a special case because V2.0 used enum and V3.x uses String so we cannot do simple type conversion - we need to map the V3.x strings into a corresponding V2 type if possible
    private void checkAndConvertV3SchemeNameToV2(OBAccount3Account obAccount3Account) {
        OBExternalAccountIdentification4Code code4 = OBExternalAccountIdentification4Code.fromValue(obAccount3Account.getSchemeName());
        if (code4 == null) {
            // Not a V3.x OBExternalAccountIdentification4Code scheme name so no action required
            return;
        }
        Optional<OBExternalAccountIdentification3Code> code3 = Optional.ofNullable(OBExternalAccountIdentificationConverter.toOBExternalAccountIdentification3(code4));
        obAccount3Account.setSchemeName(code3.map(OBExternalAccountIdentification3Code::toString).orElse(""));
    }

}
