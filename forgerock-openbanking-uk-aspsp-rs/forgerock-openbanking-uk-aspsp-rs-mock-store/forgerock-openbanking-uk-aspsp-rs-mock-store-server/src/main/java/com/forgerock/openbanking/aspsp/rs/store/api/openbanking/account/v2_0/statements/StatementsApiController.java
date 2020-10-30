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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.v2_0.statements;

import com.forgerock.openbanking.aspsp.rs.store.repository.accounts.statements.FRStatementRepository;
import com.forgerock.openbanking.aspsp.rs.store.service.statement.StatementPDFService;
import com.forgerock.openbanking.aspsp.rs.store.utils.AccountDataInternalIdFilter;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.FRStatement;
import com.forgerock.openbanking.common.services.openbanking.converter.account.FRStatementConverter;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;
import uk.org.openbanking.datamodel.account.OBReadStatement1;
import uk.org.openbanking.datamodel.account.OBReadStatement1Data;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.common.services.openbanking.converter.account.FRExternalPermissionsCodeConverter.toFRExternalPermissionsCodeList;
import static com.forgerock.openbanking.constants.OpenBankingConstants.HTTP_DATE_FORMAT;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.FROM_STATEMENT_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.ParametersFieldName.TO_STATEMENT_DATE_TIME;
import static com.forgerock.openbanking.constants.OpenBankingConstants.STATEMENT_TIME_DATE_FORMAT;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-06-25T23:06:46.214+01:00")

@Controller("StatementsApiV2.0")
@Slf4j
public class StatementsApiController implements StatementsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatementsApiController.class);

    @Value("${rs.page.default.statement.size}")
    private int PAGE_LIMIT_STATEMENTS;

    @Autowired
    private FRStatementRepository frStatementRepository;

    @Autowired
    private AccountDataInternalIdFilter accountDataInternalIdFilter;

    @Autowired
    private StatementPDFService statementPDFService;

    @Override
    public ResponseEntity<OBReadStatement1> getAccountStatement(
            @ApiParam(value = "A unique identifier used to identify the account resource.", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "A unique identifier used to identify the statement resource.", required = true)
            @PathVariable("StatementId") String statementId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP.  " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        LOGGER.info("Read statements for account {} with minimumPermissions {}", accountId, permissions);
        List<FRStatement> statements = frStatementRepository.byAccountIdAndStatementIdWithPermissions(accountId, statementId, toFRExternalPermissionsCodeList(permissions));
        int totalPages = 1;

        return ResponseEntity.ok(new OBReadStatement1().data(new OBReadStatement1Data().statement(
                statements
                        .stream()
                        .map(FRStatement::getStatement)
                        .map(FRStatementConverter::toOBStatement1)
                        .map(so -> accountDataInternalIdFilter.apply(so))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<Resource> getAccountStatementFile(
            @ApiParam(value = "A unique identifier used to identify the account resource.", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "A unique identifier used to identify the statement resource.", required = true)
            @PathVariable("StatementId") String statementId,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP. " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below: " +
                    " Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "HTTP accept header. Statements only implemented for certain media types.", required = true)
            @RequestHeader(value = "Accept", required = true) String accept) {

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

    @Override
    public ResponseEntity<OBReadStatement1> getStatements(
            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter statements FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_STATEMENT_DATE_TIME, required = false) DateTime fromStatementDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter statements TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_STATEMENT_DATE_TIME, required = false) DateTime toStatementDateTime,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The time when the PSU last logged in with the TPP. " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @ApiParam(value = "The OB account IDs")
            @RequestHeader(value = "x-ob-account-ids", required = true) List<String> accountIds,

            @ApiParam(value = "The OB permissions")
            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @ApiParam(value = "The origin http url")
            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {
        LOGGER.info("Reading statements from account ids {}", accountIds);
        Page<FRStatement> statements = frStatementRepository.findByAccountIdIn(accountIds,
                PageRequest.of(page, PAGE_LIMIT_STATEMENTS, Sort.Direction.ASC, "startDateTime"));
        int totalPages = statements.getTotalPages();

        return ResponseEntity.ok(new OBReadStatement1().data(new OBReadStatement1Data().statement(
                statements.getContent()
                        .stream()
                        .map(FRStatement::getStatement)
                        .map(FRStatementConverter::toOBStatement1)
                        .map(so -> accountDataInternalIdFilter.apply(so))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<OBReadStatement1> getAccountStatements(
            @ApiParam(value = "A unique identifier used to identify the account resource.", required = true)
            @PathVariable("AccountId") String accountId,

            @ApiParam(value = "Page number.", required = false, defaultValue = "0")
            @RequestParam(value = "page", defaultValue = "0") int page,

            @ApiParam(value = "The unique id of the ASPSP to which the request is issued. The unique id will be issued by OB.", required = true)
            @RequestHeader(value = "x-fapi-financial-id", required = true) String xFapiFinancialId,

            @ApiParam(value = "An Authorisation Token as per https://tools.ietf.org/html/rfc6750", required = true)
            @RequestHeader(value = "Authorization", required = true) String authorization,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter statements FROM  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = FROM_STATEMENT_DATE_TIME, required = false)
            @DateTimeFormat(pattern = STATEMENT_TIME_DATE_FORMAT) DateTime fromStatementDateTime,

            @ApiParam(value = "The UTC ISO 8601 Date Time to filter statements TO  NB Time component is optional " +
                    "- set to 00:00:00 for just Date.   The parameter must NOT have a timezone set")
            @RequestParam(value = TO_STATEMENT_DATE_TIME, required = false)
            @DateTimeFormat(pattern = STATEMENT_TIME_DATE_FORMAT) DateTime toStatementDateTime,

            @ApiParam(value = "The time when the PSU last logged in with the TPP. " +
                    "All dates in the HTTP headers are represented as RFC 7231 Full Dates. An example is below:  " +
                    "Sun, 10 Sep 2017 19:43:31 UTC")
            @RequestHeader(value = "x-fapi-customer-last-logged-time", required = false)
            @DateTimeFormat(pattern = HTTP_DATE_FORMAT) DateTime xFapiCustomerLastLoggedTime,

            @ApiParam(value = "The PSU's IP address if the PSU is currently logged in with the TPP.")
            @RequestHeader(value = "x-fapi-customer-ip-address", required = false) String xFapiCustomerIpAddress,

            @ApiParam(value = "An RFC4122 UID used as a correlation id.")
            @RequestHeader(value = "x-fapi-interaction-id", required = false) String xFapiInteractionId,

            @ApiParam(value = "Indicates the user-agent that the PSU is using.")
            @RequestHeader(value = "x-customer-user-agent", required = false) String xCustomerUserAgent,

            @RequestHeader(value = "x-ob-permissions", required = true) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = true) String httpUrl
    ) throws OBErrorResponseException {

        LOGGER.info("Read statements for account {} with minimumPermissions {}", accountId, permissions);
        Page<FRStatement> statements = frStatementRepository.byAccountIdWithPermissions(accountId,
                fromStatementDateTime, toStatementDateTime,
                toFRExternalPermissionsCodeList(permissions),
                PageRequest.of(page, PAGE_LIMIT_STATEMENTS, Sort.Direction.ASC, "startDateTime"));

        int totalPages = statements.getTotalPages();

        return ResponseEntity.ok(new OBReadStatement1().data(new OBReadStatement1Data().statement(
                statements.getContent()
                        .stream()
                        .map(FRStatement::getStatement)
                        .map(FRStatementConverter::toOBStatement1)
                        .map(so -> accountDataInternalIdFilter.apply(so))
                        .collect(Collectors.toList())))
                .links(PaginationUtil.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtil.generateMetaData(totalPages)));
    }

}
