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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.scheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.scheduledpayments.FRScheduledPayment4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v3_1_3.account.FRScheduledPayment4;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadScheduledPayment3;
import uk.org.openbanking.datamodel.account.OBReadScheduledPayment3Data;

import java.util.List;
import java.util.stream.Collectors;

@Controller("ScheduledPaymentsApiV3.1.3")
@Slf4j
public class ScheduledPaymentsApiController implements ScheduledPaymentsApi {

    private final int pageLimitSchedulePayments;

    private final FRScheduledPayment4Repository frScheduledPayment4Repository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    public ScheduledPaymentsApiController(@Value("${rs.page.default.schedule-payments.size}") int pageLimitSchedulePayments,
                                          FRScheduledPayment4Repository frScheduledPayment4Repository,
                                          AccountDataInternalIdFilter accountDataInternalIdFilter) {
        this.pageLimitSchedulePayments = pageLimitSchedulePayments;
        this.frScheduledPayment4Repository = frScheduledPayment4Repository;
        this.accountDataInternalIdFilter = accountDataInternalIdFilter;
    }

    @Override
    public ResponseEntity<OBReadScheduledPayment3> getAccountScheduledPayments(String accountId,
                                                                               int page,
                                                                               String authorization,
                                                                               DateTime xFapiAuthDate,
                                                                               String xFapiCustomerIpAddress,
                                                                               String xFapiInteractionId,
                                                                               String xCustomerUserAgent,
                                                                               List<OBExternalPermissions1Code> permissions,
                                                                               String httpUrl) throws OBErrorResponseException {
        log.info("Read scheduled payments for account {} with minimumPermissions {}", accountId, permissions);

        Page<FRScheduledPayment4> scheduledPayments = frScheduledPayment4Repository.byAccountIdWithPermissions(accountId, permissions, PageRequest.of(page, pageLimitSchedulePayments));
        return packageResponse(page, httpUrl, scheduledPayments);
    }

    @Override
    public ResponseEntity<OBReadScheduledPayment3> getScheduledPayments(int page,
                                                                        String authorization,
                                                                        DateTime xFapiAuthDate,
                                                                        String xFapiCustomerIpAddress,
                                                                        String xFapiInteractionId,
                                                                        String xCustomerUserAgent,
                                                                        List<String> accountIds,
                                                                        List<OBExternalPermissions1Code> permissions,
                                                                        String httpUrl) throws OBErrorResponseException {
        log.info("Reading schedule payment from account ids {}", accountIds);

        Page<FRScheduledPayment4> scheduledPayments = frScheduledPayment4Repository.byAccountIdInWithPermissions(accountIds, permissions, PageRequest.of(page, pageLimitSchedulePayments));
        return packageResponse(page, httpUrl, scheduledPayments);
    }

    private ResponseEntity<OBReadScheduledPayment3> packageResponse(int page, String httpUrl, Page<FRScheduledPayment4> scheduledPayments) {
        int totalPages = scheduledPayments.getTotalPages();

        return ResponseEntity.ok(new OBReadScheduledPayment3().data(new OBReadScheduledPayment3Data().scheduledPayment(
                scheduledPayments.getContent()
                        .stream()
                        .map(FRScheduledPayment4::getScheduledPayment)
                        .map(sp -> accountDataInternalIdFilter.apply(sp))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

}
