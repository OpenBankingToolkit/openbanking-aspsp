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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.directdebits;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_3.accounts.directdebits.FRDirectDebit4Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRDirectDebit4;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadDirectDebit2;
import uk.org.openbanking.datamodel.account.OBReadDirectDebit2Data;

import java.util.List;
import java.util.stream.Collectors;

@Controller("DirectDebitsApiV3.1.3")
@Slf4j
public class DirectDebitsApiController implements DirectDebitsApi {

    private final int pageLimitDirectDebits;

    private final FRDirectDebit4Repository frDirectDebit4Repository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    public DirectDebitsApiController(@Value("${rs.page.default.direct-debits.size}") int pageLimitDirectDebits,
                                     FRDirectDebit4Repository frDirectDebit4Repository,
                                     AccountDataInternalIdFilter accountDataInternalIdFilter) {
        this.pageLimitDirectDebits = pageLimitDirectDebits;
        this.frDirectDebit4Repository = frDirectDebit4Repository;
        this.accountDataInternalIdFilter = accountDataInternalIdFilter;
    }

    @Override
    public ResponseEntity<OBReadDirectDebit2> getAccountDirectDebits(String accountId,
                                                                     int page,
                                                                     String authorization,
                                                                     DateTime xFapiAuthDate,
                                                                     String xFapiCustomerIpAddress,
                                                                     String xFapiInteractionId,
                                                                     String xCustomerUserAgent,
                                                                     List<OBExternalPermissions1Code> permissions,
                                                                     String httpUrl) throws OBErrorResponseException {
        log.info("Read direct debits for account  {} with minimumPermissions {}", accountId, permissions);

        Page<FRDirectDebit4> directDebits = frDirectDebit4Repository.byAccountIdWithPermissions(accountId, permissions, PageRequest.of(page, pageLimitDirectDebits));
        return packageResponse(page, httpUrl, directDebits);
    }

    @Override
    public ResponseEntity<OBReadDirectDebit2> getDirectDebits(int page,
                                                              String authorization,
                                                              DateTime xFapiAuthDate,
                                                              String xFapiCustomerIpAddress,
                                                              String xFapiInteractionId,
                                                              String xCustomerUserAgent,
                                                              List<String> accountIds,
                                                              List<OBExternalPermissions1Code> permissions,
                                                              String httpUrl) throws OBErrorResponseException {
        log.info("DirectDebits fron account ids {} ", accountIds);

        Page<FRDirectDebit4> directDebits = frDirectDebit4Repository.byAccountIdInWithPermissions(accountIds, permissions, PageRequest.of(page, pageLimitDirectDebits));
        return packageResponse(page, httpUrl, directDebits);
    }

    private ResponseEntity<OBReadDirectDebit2> packageResponse(int page, String httpUrl, Page<FRDirectDebit4> directDebits) {
        int totalPages = directDebits.getTotalPages();

        return ResponseEntity.ok(new OBReadDirectDebit2()
                .data(new OBReadDirectDebit2Data().directDebit(directDebits.getContent()
                        .stream()
                        .map(FRDirectDebit4::getDirectDebit)
                        .map(dd -> accountDataInternalIdFilter.apply(dd))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

}
