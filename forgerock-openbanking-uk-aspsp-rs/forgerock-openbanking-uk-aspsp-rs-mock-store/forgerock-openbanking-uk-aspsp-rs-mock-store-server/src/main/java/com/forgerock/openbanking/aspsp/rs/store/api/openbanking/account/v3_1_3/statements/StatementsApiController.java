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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v3_1_3.statements;

import com.forgerock.openbanking.aspsp.rs.store.repository.v2_0.accounts.statements.FRStatement1Repository;
import com.forgerock.openbanking.aspsp.rs.store.service.statement.StatementPDFService;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRStatement1;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import uk.org.openbanking.datamodel.account.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadStatement2;
import uk.org.openbanking.datamodel.account.OBReadStatement2Data;
import uk.org.openbanking.datamodel.account.OBStatement1;
import uk.org.openbanking.datamodel.account.OBStatement2;
import uk.org.openbanking.datamodel.account.OBStatementInterest1;
import uk.org.openbanking.datamodel.account.OBStatementInterest2;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-25T23:06:46.214+01:00")

@Controller("StatementsApiV3.1.3")
@Slf4j
public class StatementsApiController implements StatementsApi {

    private final int pageLimitStatements;

    private final FRStatement1Repository frStatement1Repository;

    private final AccountDataInternalIdFilter accountDataInternalIdFilter;

    private final StatementPDFService statementPDFService;

    public StatementsApiController(@Value("${rs.page.default.statement.size}") int pageLimitStatements,
                                   FRStatement1Repository frStatement1Repository,
                                   AccountDataInternalIdFilter accountDataInternalIdFilter,
                                   StatementPDFService statementPDFService) {
        this.pageLimitStatements = pageLimitStatements;
        this.frStatement1Repository = frStatement1Repository;
        this.accountDataInternalIdFilter = accountDataInternalIdFilter;
        this.statementPDFService = statementPDFService;
    }

    @Override
    public ResponseEntity<OBReadStatement2> getAccountStatement(String statementId,
                                                                String accountId,
                                                                int page,
                                                                String authorization,
                                                                DateTime xFapiAuthDate,
                                                                String xFapiCustomerIpAddress,
                                                                String xFapiInteractionId,
                                                                String xCustomerUserAgent,
                                                                List<OBExternalPermissions1Code> permissions,
                                                                String httpUrl) throws OBErrorResponseException {
        log.info("Read statements for account {} with minimumPermissions {}", accountId, permissions);

        List<FRStatement1> statements = frStatement1Repository.byAccountIdAndStatementIdWithPermissions(accountId, statementId, permissions);
        int totalPages = 1;
        return packageResponse(page, httpUrl, statements, totalPages);
    }

    @Override
    public ResponseEntity<Resource> getAccountStatementFile(String statementId,
                                                            String accountId,
                                                            int page,
                                                            String authorization,
                                                            DateTime xFapiAuthDate,
                                                            String xFapiCustomerIpAddress,
                                                            String xFapiInteractionId,
                                                            String accept) throws OBErrorResponseException {
        log.info("Received a statement file download request for account: {} (Accept: {}). Interaction Id: {}", accountId, accept, xFapiInteractionId);
        if (!accept.contains(MediaType.APPLICATION_PDF_VALUE)) {
            // Mo other file type is implemented apart from PDF
            return new ResponseEntity<Resource>(HttpStatus.NOT_IMPLEMENTED);
        }

        // Check if this cusotmer has a statement file
        Optional<Resource> statement = statementPDFService.getPdfStatement();
        if (statement.isPresent()) {
            return ResponseEntity.ok()
                    .contentLength(getContentLength(statement.get()))
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(statement.get());
        }
        return new ResponseEntity<Resource>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBReadStatement2> getAccountStatements(String accountId,
                                                                 int page,
                                                                 String authorization,
                                                                 DateTime xFapiAuthDate,
                                                                 DateTime fromStatementDateTime,
                                                                 DateTime toStatementDateTime,
                                                                 String xFapiCustomerIpAddress,
                                                                 String xFapiInteractionId,
                                                                 String xCustomerUserAgent,
                                                                 List<OBExternalPermissions1Code> permissions,
                                                                 String httpUrl) throws OBErrorResponseException {
        log.info("Read statements for account {} with minimumPermissions {}", accountId, permissions);

        Page<FRStatement1> statements = frStatement1Repository.byAccountIdWithPermissions(accountId,
                fromStatementDateTime, toStatementDateTime, permissions,
                PageRequest.of(page, pageLimitStatements, Sort.Direction.ASC, "startDateTime"));

        int totalPages = statements.getTotalPages();
        return packageResponse(page, httpUrl, statements.getContent(), totalPages);
    }

    @Override
    public ResponseEntity<OBReadStatement2> getStatements(int page,
                                                          String authorization,
                                                          DateTime xFapiAuthDate,
                                                          DateTime fromStatementDateTime,
                                                          DateTime toStatementDateTime,
                                                          String xFapiCustomerIpAddress,
                                                          String xFapiInteractionId,
                                                          String xCustomerUserAgent,
                                                          List<String> accountIds,
                                                          List<OBExternalPermissions1Code> permissions,
                                                          String httpUrl) throws OBErrorResponseException {
        log.info("Reading statements from account ids {}", accountIds);

        Page<FRStatement1> statements = frStatement1Repository.findByAccountIdIn(accountIds, PageRequest.of(page, pageLimitStatements, Sort.Direction.ASC, "startDateTime"));
        int totalPages = statements.getTotalPages();
        return packageResponse(page, httpUrl, statements.getContent(), totalPages);
    }

    private Integer getContentLength(Resource resource) {
        String data = "";
        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return bdata.length;
        } catch (IOException e) {
            log.warn("We found a statement PDF file '{}' for ASPSP but could no get content-length with error", resource.getFilename(), e);
            return null;
        }
    }

    private ResponseEntity<OBReadStatement2> packageResponse(int page, String httpUrl, List<FRStatement1> statements, int totalPages) {
        return ResponseEntity.ok(new OBReadStatement2().data(new OBReadStatement2Data().statement(
                statements
                        .stream()
                        .map(FRStatement1::getStatement)
                        .map(st -> accountDataInternalIdFilter.apply(st))
                        .map(st -> toOBStatement2(st))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    // TODO #232 - move to uk-datemodel repo
    public static OBStatement2 toOBStatement2(OBStatement1 obStatement1) {
        return obStatement1 == null ? null : (new OBStatement2())
                .accountId(obStatement1.getAccountId())
                .statementId(obStatement1.getStatementId())
                .statementReference(obStatement1.getStatementReference())
                .type(obStatement1.getType())
                .startDateTime(obStatement1.getStartDateTime())
                .endDateTime(obStatement1.getEndDateTime())
                .creationDateTime(obStatement1.getCreationDateTime())
                .statementDescription(obStatement1.getStatementDescription())
                .statementBenefit(obStatement1.getStatementBenefit())
                .statementFee(null) // TODO #232 - populate from datasource?
                .statementInterest(toOBStatementInterest2List(obStatement1.getStatementInterest()))
                .statementDateTime(obStatement1.getStatementDateTime())
                .statementRate(obStatement1.getStatementRate())
                .statementValue(obStatement1.getStatementValue())
                .statementAmount(obStatement1.getStatementAmount());
    }

    public static List<OBStatementInterest2> toOBStatementInterest2List(List<OBStatementInterest1> statementInterest1s) {
        return statementInterest1s == null ? null : statementInterest1s
                .stream()
                .map(st -> tostatementInterest2(st))
                .collect(Collectors.toList());
    }

    public static OBStatementInterest2 tostatementInterest2(OBStatementInterest1 obStatementInterest1) {
        return obStatementInterest1 == null ? null : (new OBStatementInterest2())
                .description(null) // TODO #232 - populate from datasource?
                .creditDebitIndicator(OBStatementInterest2.CreditDebitIndicatorEnum.valueOf(obStatementInterest1.getCreditDebitIndicator().name()))
                .type(obStatementInterest1.getType())
                .rate(null) // TODO #232 - populate from datasource?
                .rateType(null) // TODO #232 - populate from datasource?
                .frequency(null) // TODO #232 - populate from datasource?
                .amount(toOBActiveOrHistoricCurrencyAndAmount(obStatementInterest1.getAmount()));
    }

    public static OBActiveOrHistoricCurrencyAndAmount toOBActiveOrHistoricCurrencyAndAmount(uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount amount) {
        return amount == null ? null : (new OBActiveOrHistoricCurrencyAndAmount())
                .currency(amount.getCurrency())
                .amount(amount.getAmount());
    }
}
