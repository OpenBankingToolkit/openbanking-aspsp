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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_3.directdebits;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBReadDirectDebit2;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller("DirectDebitsApiV3.1.3")
@Slf4j
public class DirectDebitsApiController implements DirectDebitsApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;

    private final RsStoreGateway rsStoreGateway;

    public DirectDebitsApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
    }

    @Override
    public ResponseEntity<OBReadDirectDebit2> getAccountDirectDebits(String accountId,
                                                                     String page,
                                                                     String authorization,
                                                                     DateTime xFapiAuthDate,
                                                                     String xFapiCustomerIpAddress,
                                                                     String xFapiInteractionId,
                                                                     String xCustomerUserAgent,
                                                                     HttpServletRequest request,
                                                                     Principal principal) throws OBErrorResponseException {
        return rsEndpointWrapperService.<OBReadDirectDebit2>accountAndTransactionEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .accountId(accountId)
                .principal(principal)
                .page(page)
                .minimumPermissions(FRExternalPermissionsCode.READDIRECTDEBITS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read direct debits for account  {} with minimumPermissions {}", accountId, permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(FRExternalPermissionsCode::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBReadDirectDebit2.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadDirectDebit2> getDirectDebits(String page,
                                                              String authorization,
                                                              DateTime xFapiAuthDate,
                                                              String xFapiCustomerIpAddress,
                                                              String xFapiInteractionId,
                                                              String xCustomerUserAgent,
                                                              HttpServletRequest request,
                                                              Principal principal) throws OBErrorResponseException {
        return rsEndpointWrapperService.<OBReadDirectDebit2>accountAndTransactionEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .page(page)
                .minimumPermissions(FRExternalPermissionsCode.READDIRECTDEBITS)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("DirectDebits get with id {} ", accountRequest.getAccountIds());
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(FRExternalPermissionsCode::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBReadDirectDebit2.class);
                        }
                );
    }
}
