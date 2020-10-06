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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_5.standingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.standingorders.FRStandingOrderRepository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStandingOrder;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadStandingOrder6;
import uk.org.openbanking.datamodel.account.OBReadStandingOrder6Data;

import java.util.List;
import java.util.stream.Collectors;

@Controller("StandingOrdersApiV3.1.5")
@Slf4j
public class StandingOrdersApiController implements StandingOrdersApi {

    private final int pageLimitStandingOrders;

    private final FRStandingOrderRepository frStandingOrderRepository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    public StandingOrdersApiController(@Value("${rs.page.default.standing-order.size}") int pageLimitStandingOrders,
                                       FRStandingOrderRepository frStandingOrderRepository,
                                       AccountDataInternalIdFilter accountDataInternalIdFilter) {
        this.pageLimitStandingOrders = pageLimitStandingOrders;
        this.frStandingOrderRepository = frStandingOrderRepository;
        this.accountDataInternalIdFilter = accountDataInternalIdFilter;
    }

    @Override
    public ResponseEntity<OBReadStandingOrder6> getAccountStandingOrders(String accountId,
                                                                         int page,
                                                                         String authorization,
                                                                         DateTime xFapiAuthDate,
                                                                         String xFapiCustomerIpAddress,
                                                                         String xFapiInteractionId,
                                                                         String xCustomerUserAgent,
                                                                         List<OBExternalPermissions1Code> permissions,
                                                                         String httpUrl) throws OBErrorResponseException {
        log.info("Read standing orders for account {} with minimumPermissions {}",
                accountId, permissions);
        Page<FRStandingOrder> standingOrders =
                frStandingOrderRepository.byAccountIdWithPermissions(accountId, permissions,
                        PageRequest.of(page, pageLimitStandingOrders));

        int totalPages = standingOrders.getTotalPages();

        return ResponseEntity.ok(new OBReadStandingOrder6()
                .data(new OBReadStandingOrder6Data().standingOrder(standingOrders.getContent()
                        .stream()
                        .map(FRStandingOrder::getStandingOrder)
                        .map(so -> accountDataInternalIdFilter.apply(so))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<OBReadStandingOrder6> getStandingOrders(int page,
                                                                  String authorization,
                                                                  DateTime xFapiAuthDate,
                                                                  String xFapiCustomerIpAddress,
                                                                  String xFapiInteractionId,
                                                                  String xCustomerUserAgent,
                                                                  List<String> accountIds,
                                                                  List<OBExternalPermissions1Code> permissions,
                                                                  String httpUrl) throws OBErrorResponseException {
        log.info("Reading standing orders from account ids {}", accountIds);
        Page<FRStandingOrder> standingOrders = frStandingOrderRepository.byAccountIdInWithPermissions(accountIds, permissions,
                PageRequest.of(page, pageLimitStandingOrders));
        int totalPages = standingOrders.getTotalPages();

        return ResponseEntity.ok(new OBReadStandingOrder6()
                .data(new OBReadStandingOrder6Data().standingOrder(standingOrders.getContent().stream()
                        .map(FRStandingOrder::getStandingOrder)
                        .map(so -> accountDataInternalIdFilter.apply(so))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }
}
