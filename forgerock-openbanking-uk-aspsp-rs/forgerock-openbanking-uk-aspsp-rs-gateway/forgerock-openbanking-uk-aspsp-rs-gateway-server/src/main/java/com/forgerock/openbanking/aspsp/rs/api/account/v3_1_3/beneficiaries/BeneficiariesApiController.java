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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_3.beneficiaries;

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
import uk.org.openbanking.datamodel.account.OBReadBeneficiary4;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.stream.Collectors;

@Controller("BeneficiariesApiV3.1.3")
@Slf4j
public class BeneficiariesApiController implements BeneficiariesApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;

    private final RsStoreGateway rsStoreGateway;

    public BeneficiariesApiController(RSEndpointWrapperService rsEndpointWrapperService, RsStoreGateway rsStoreGateway) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.rsStoreGateway = rsStoreGateway;
    }

    @Override
    public ResponseEntity<OBReadBeneficiary4> getAccountBeneficiaries(String accountId,
                                                                      String page,
                                                                      String authorization,
                                                                      DateTime xFapiAuthDate,
                                                                      String xFapiCustomerIpAddress,
                                                                      String xFapiInteractionId,
                                                                      String xCustomerUserAgent,
                                                                      HttpServletRequest request,
                                                                      Principal principal) throws OBErrorResponseException {
        return rsEndpointWrapperService.<OBReadBeneficiary4>accountAndTransactionEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .accountId(accountId)
                .principal(principal)
                .page(page)
                .minimumPermissions(FRExternalPermissionsCode.READBENEFICIARIESDETAIL, FRExternalPermissionsCode.READBENEFICIARIESBASIC)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Read beneficiaries for account {} with minimumPermissions {}", accountId, permissions);
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(FRExternalPermissionsCode::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBReadBeneficiary4.class);
                        }
                );
    }

    @Override
    public ResponseEntity<OBReadBeneficiary4> getBeneficiaries(String page,
                                                               String authorization,
                                                               DateTime xFapiAuthDate,
                                                               String xFapiCustomerIpAddress,
                                                               String xFapiInteractionId,
                                                               String xCustomerUserAgent,
                                                               HttpServletRequest request,
                                                               Principal principal) throws OBErrorResponseException {
        return rsEndpointWrapperService.<OBReadBeneficiary4>accountAndTransactionEndpoint()
                .authorization(authorization)
                .xFapiFinancialId(rsEndpointWrapperService.rsConfiguration.financialId)
                .principal(principal)
                .page(page)
                .minimumPermissions(FRExternalPermissionsCode.READBENEFICIARIESBASIC, FRExternalPermissionsCode.READBENEFICIARIESDETAIL)
                .execute(
                        (accountRequest, permissions, pageNumber) -> {
                            log.info("Beneficaries get with id {}", accountRequest.getAccountIds());
                            HttpHeaders additionalHttpHeaders = new HttpHeaders();
                            additionalHttpHeaders.addAll("x-ob-account-ids", accountRequest.getAccountIds());
                            additionalHttpHeaders.addAll("x-ob-permissions", permissions.stream().map(FRExternalPermissionsCode::name).collect(Collectors.toList()));
                            additionalHttpHeaders.add("x-ob-url", new ServletServerHttpRequest(request).getURI().toString());

                            return rsStoreGateway.toRsStore(request, additionalHttpHeaders, OBReadBeneficiary4.class);
                        }
                );
    }
}
