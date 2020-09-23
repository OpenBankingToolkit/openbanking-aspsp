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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_1.scheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.scheduledpayments.FRScheduledPayment4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_3.account.FRScheduledPayment4;
import com.forgerock.openbanking.common.services.openbanking.converter.account.FRScheduledPaymentConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
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
import uk.org.openbanking.datamodel.account.OBReadScheduledPayment2;
import uk.org.openbanking.datamodel.account.OBReadScheduledPayment2Data;

import java.util.List;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;

@Controller("ScheduledPaymentsApiV3.1.1")
@Slf4j
public class ScheduledPaymentsApiController implements ScheduledPaymentsApi {
    @Value("${rs.page.default.schedule-payments.size}")
    private int PAGE_LIMIT_SCHEDULE_PAYMENTS;
    @Autowired
    private FRScheduledPayment4Repository frScheduledPaymentRepository;
    @Autowired
    private AccountDataInternalIdFilter accountDataInternalIdFilter;

    @Override
    public ResponseEntity<OBReadScheduledPayment2> getAccountScheduledPayments(
            @ApiParam(value = "A unique identifier used to identify the account resource.",required=true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {

        log.info("Read scheduled payments for account {} with minimumPermissions {}", accountId, permissions);
        Page<FRScheduledPayment4> scheduledPayments = frScheduledPaymentRepository.byAccountIdWithPermissions(accountId, permissions,
                PageRequest.of(page, PAGE_LIMIT_SCHEDULE_PAYMENTS));
        int totalPages = scheduledPayments.getTotalPages();

        return ResponseEntity.ok(new OBReadScheduledPayment2().data(new OBReadScheduledPayment2Data().scheduledPayment(
                scheduledPayments.getContent()
                        .stream()
                        .map(FRScheduledPayment4::getScheduledPayment)
                        .map(FRScheduledPaymentConverter::toOBScheduledPayment2)
                        .map(dd -> accountDataInternalIdFilter.apply(dd))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<OBReadScheduledPayment2> getScheduledPayments(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB." ,required=true)
            @RequestHeader(value="x-fapi-financial-id", required=true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750" ,required=true)
            @RequestHeader(value="Authorization", required=true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC" )
            @RequestHeader(value="x-fapi-customer-last-logged-time", required=false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP." )
            @RequestHeader(value="x-fapi-customer-ip-address", required=false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id." )
            @RequestHeader(value="x-fapi-interaction-id", required=false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The OB account IDs")
            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @ApiParam(value = "The OB permissions")
            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @ApiParam(value = "The origin http url")
            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        log.info("Reading schedule payment from account ids {}", accountIds);
        Page<FRScheduledPayment4> scheduledPayments = frScheduledPaymentRepository.byAccountIdInWithPermissions(accountIds, permissions,
                PageRequest.of(page, PAGE_LIMIT_SCHEDULE_PAYMENTS));
        int totalPages = scheduledPayments.getTotalPages();

        return ResponseEntity.ok(new OBReadScheduledPayment2().data(new OBReadScheduledPayment2Data().scheduledPayment(
                scheduledPayments.getContent()
                        .stream()
                        .map(FRScheduledPayment4::getScheduledPayment)
                        .map(FRScheduledPaymentConverter::toOBScheduledPayment2)
                        .map(dd -> accountDataInternalIdFilter.apply(dd))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }
}
