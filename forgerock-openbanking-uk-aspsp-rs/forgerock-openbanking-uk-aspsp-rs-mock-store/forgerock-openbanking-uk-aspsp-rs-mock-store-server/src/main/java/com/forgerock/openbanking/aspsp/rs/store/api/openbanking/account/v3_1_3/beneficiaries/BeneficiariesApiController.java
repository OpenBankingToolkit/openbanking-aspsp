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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.beneficiaries;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.beneficiaries.FRBeneficiary3Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRBeneficiary3;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBBeneficiary3;
import uk.org.openbanking.datamodel.account.OBBeneficiary4;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification6;
import uk.org.openbanking.datamodel.account.OBBranchAndFinancialInstitutionIdentification60;
import uk.org.openbanking.datamodel.account.OBCashAccount5;
import uk.org.openbanking.datamodel.account.OBCashAccount50;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadBeneficiary4;
import uk.org.openbanking.datamodel.account.OBReadBeneficiary4Data;

import java.util.List;
import java.util.stream.Collectors;

@Controller("BeneficiariesApiV3.1.3")
@Slf4j
public class BeneficiariesApiController implements BeneficiariesApi {

    private final int pageLimitBeneficiaries;

    private final FRBeneficiary3Repository frBeneficiary3Repository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    public BeneficiariesApiController(@Value("${rs.page.default.beneficiaries.size}") int pageLimitBeneficiaries,
                                      FRBeneficiary3Repository frBeneficiary3Repository,
                                      AccountDataInternalIdFilter accountDataInternalIdFilter) {
        this.pageLimitBeneficiaries = pageLimitBeneficiaries;
        this.frBeneficiary3Repository = frBeneficiary3Repository;
        this.accountDataInternalIdFilter = accountDataInternalIdFilter;
    }

    @Override
    public ResponseEntity<OBReadBeneficiary4> getAccountBeneficiaries(String accountId,
                                                                      int page,
                                                                      String authorization,
                                                                      DateTime xFapiAuthDate,
                                                                      String xFapiCustomerIpAddress,
                                                                      String xFapiInteractionId,
                                                                      String xCustomerUserAgent,
                                                                      List<OBExternalPermissions1Code> permissions,
                                                                      String httpUrl) throws OBErrorResponseException {
        log.info("Read beneficiaries for account {} with minimumPermissions {}", accountId, permissions);

        Page<FRBeneficiary3> beneficiaries = frBeneficiary3Repository.byAccountIdWithPermissions(accountId, permissions, PageRequest.of(page, pageLimitBeneficiaries));
        return packageResponse(page, httpUrl, beneficiaries);
    }

    @Override
    public ResponseEntity<OBReadBeneficiary4> getBeneficiaries(int page,
                                                               String authorization,
                                                               DateTime xFapiAuthDate,
                                                               String xFapiCustomerIpAddress,
                                                               String xFapiInteractionId,
                                                               String xCustomerUserAgent,
                                                               List<String> accountIds,
                                                               List<OBExternalPermissions1Code> permissions,
                                                               String httpUrl) throws OBErrorResponseException {
        log.info("Beneficaries from account ids {}", accountIds);

        Page<FRBeneficiary3> beneficiaries = frBeneficiary3Repository.byAccountIdInWithPermissions(accountIds, permissions, PageRequest.of(page, pageLimitBeneficiaries));
        return packageResponse(page, httpUrl, beneficiaries);
    }

    private ResponseEntity<OBReadBeneficiary4> packageResponse(int page, String httpUrl, Page<FRBeneficiary3> beneficiaries) {
        int totalPages = beneficiaries.getTotalPages();

        return ResponseEntity.ok(new OBReadBeneficiary4().data(new OBReadBeneficiary4Data().beneficiary(
                beneficiaries.getContent()
                        .stream()
                        .map(FRBeneficiary3::getBeneficiary)
                        .map(b -> accountDataInternalIdFilter.apply(b))
                        .map(b -> toOBBeneficiary4(b))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    // TODO #232 - move to uk-datamodel
    public static OBBeneficiary4 toOBBeneficiary4(OBBeneficiary3 obBeneficiary3) {
        return obBeneficiary3 == null ? null : (new OBBeneficiary4())
                .accountId(obBeneficiary3.getAccountId())
                .beneficiaryId(obBeneficiary3.getBeneficiaryId())
                .reference(obBeneficiary3.getReference())
                .supplementaryData(null) // TODO #232 - populate this field from the datasource?
                .creditorAgent(toOBBranchAndFinancialInstitutionIdentification60(obBeneficiary3.getCreditorAgent()))
                .creditorAccount(toOBCashAccount50(obBeneficiary3.getCreditorAccount()));
    }


    public static OBBranchAndFinancialInstitutionIdentification60 toOBBranchAndFinancialInstitutionIdentification60(OBBranchAndFinancialInstitutionIdentification6 identification) {
        return identification == null ? null : (new OBBranchAndFinancialInstitutionIdentification60())
                .schemeName(identification.getSchemeName())
                .identification(identification.getIdentification())
                .name(identification.getName())
                .postalAddress(identification.getPostalAddress());
    }

    public static OBCashAccount50 toOBCashAccount50(OBCashAccount5 account) {
        return account == null ? null : (new OBCashAccount50())
                .schemeName(account.getSchemeName())
                .identification(account.getIdentification())
                .name(account.getName())
                .secondaryIdentification(account.getSecondaryIdentification());
    }
}
