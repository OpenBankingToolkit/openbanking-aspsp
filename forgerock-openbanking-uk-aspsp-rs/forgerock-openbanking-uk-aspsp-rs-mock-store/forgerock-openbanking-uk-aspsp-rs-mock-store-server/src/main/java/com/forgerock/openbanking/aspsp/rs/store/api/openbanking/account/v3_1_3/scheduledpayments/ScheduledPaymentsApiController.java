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

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.scheduledpayments.FRScheduledPayment2Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRScheduledPayment2;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller("ScheduledPaymentsApiV3.1.3")
@Slf4j
public class ScheduledPaymentsApiController implements ScheduledPaymentsApi {

    private final int pageLimitSchedulePayments;

    private final FRScheduledPayment2Repository frScheduledPayment2Repository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    public ScheduledPaymentsApiController(@Value("${rs.page.default.schedule-payments.size}") int pageLimitSchedulePayments,
                                          FRScheduledPayment2Repository frScheduledPayment2Repository,
                                          AccountDataInternalIdFilter accountDataInternalIdFilter) {
        this.pageLimitSchedulePayments = pageLimitSchedulePayments;
        this.frScheduledPayment2Repository = frScheduledPayment2Repository;
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

        Page<FRScheduledPayment2> scheduledPayments = frScheduledPayment2Repository.byAccountIdWithPermissions(accountId, permissions, PageRequest.of(page, pageLimitSchedulePayments));
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

        Page<FRScheduledPayment2> scheduledPayments = frScheduledPayment2Repository.byAccountIdInWithPermissions(accountIds, permissions, PageRequest.of(page, pageLimitSchedulePayments));
        return packageResponse(page, httpUrl, scheduledPayments);
    }

    private ResponseEntity<OBReadScheduledPayment3> packageResponse(int page, String httpUrl, Page<FRScheduledPayment2> scheduledPayments) {
        int totalPages = scheduledPayments.getTotalPages();

        return ResponseEntity.ok(new OBReadScheduledPayment3().data(new OBReadScheduledPayment3Data().scheduledPayment(
                scheduledPayments.getContent()
                        .stream()
                        .map(FRScheduledPayment2::getScheduledPayment)
                        .map(sp -> accountDataInternalIdFilter.apply(sp))
                        .map(sp -> toOBScheduledPayment3(sp))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    // TODO #232 - move to uk-datamodel repo
    public static OBScheduledPayment3 toOBScheduledPayment3(OBScheduledPayment2 obScheduledPayment2) {
        return obScheduledPayment2 == null ? null : (new OBScheduledPayment3())
                .accountId(obScheduledPayment2.getAccountId())
                .scheduledPaymentId(obScheduledPayment2.getScheduledPaymentId())
                .scheduledPaymentDateTime(obScheduledPayment2.getScheduledPaymentDateTime())
                .scheduledType(obScheduledPayment2.getScheduledType())
                .reference(obScheduledPayment2.getReference())
                .debtorReference(null) // TODO #232 - populate this field from the datasource?
                .instructedAmount(toOBActiveOrHistoricCurrencyAndAmount1(obScheduledPayment2.getInstructedAmount()))
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification51(obScheduledPayment2.getCreditorAgent()))
                .creditorAccount(toOBCashAccount51(obScheduledPayment2.getCreditorAccount()));
    }

    public static OBActiveOrHistoricCurrencyAndAmount1 toOBActiveOrHistoricCurrencyAndAmount1(OBActiveOrHistoricCurrencyAndAmount amount) {
        return amount == null ? null : (new OBActiveOrHistoricCurrencyAndAmount1())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }

    public static OBBranchAndFinancialInstitutionIdentification51 toOBBranchAndFinancialInstitutionIdentification51(OBBranchAndFinancialInstitutionIdentification5 identification) {
        return identification == null ? null : (new OBBranchAndFinancialInstitutionIdentification51())
                .schemeName(identification.getSchemeName())
                .identification(identification.getIdentification());
    }

    public static OBCashAccount51 toOBCashAccount51(OBCashAccount5 account) {
        return account == null ? null : (new OBCashAccount51())
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification());
    }
}
