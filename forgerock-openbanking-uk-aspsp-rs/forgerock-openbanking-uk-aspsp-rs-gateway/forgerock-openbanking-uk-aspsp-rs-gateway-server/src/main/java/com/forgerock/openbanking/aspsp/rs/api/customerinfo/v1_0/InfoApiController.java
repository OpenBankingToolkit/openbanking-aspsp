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
package com.forgerock.openbanking.aspsp.rs.api.customerinfo.v1_0;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.org.openbanking.datamodel.customerinfo.CustomerInfo;
import uk.org.openbanking.datamodel.customerinfo.ReadCustomerInfo;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
                            date = "2021-12-20T11:13:54.447312Z[Europe/London]")
@Controller
@Slf4j
@RequestMapping("${openapi.customerInfoAPISpecification.base-path:/customer-info/v1.0}")
public class InfoApiController implements InfoApi {

    private final RsStoreGateway rsStoreGateway;

    private final RSEndpointWrapperService rsEndpointWrapperService;

    @org.springframework.beans.factory.annotation.Autowired
    public InfoApiController(RsStoreGateway rsStoreGateway, RSEndpointWrapperService rsEndpointWrapperService) {
        this.rsStoreGateway = rsStoreGateway;
        this.rsEndpointWrapperService = rsEndpointWrapperService;
    }

    @Override
    public ResponseEntity<ReadCustomerInfo> getCustomerInfo(String xFapiFinancialId,
                                                            String authorization,
                                                            String xFapiAuthDate,
                                                            String xFapiCustomerIpAddress,
                                                            String xFapiInteractionId,
                                                            String xCustomerUserAgent,
                                                            HttpServletRequest request,
                                                            Principal principal) throws OBErrorResponseException {
        return rsEndpointWrapperService.getCustomerInfoEndpointWrapper()
                .authorization(authorization)
                .xFapiFinancialId(xFapiFinancialId)
                .principal(principal)
                .minimumPermissions(FRExternalPermissionsCode.READCUSTOMERINFOCONSENT)
                .execute(
                        (accountRequest, permissions) -> {
                            log.debug("Running annonymous function passed to execute by InfoApiController(). " +
                                    "accountRequest; '{}', permissions: '{}'", accountRequest, permissions);
                            HttpHeaders additionalHeaders = new HttpHeaders();
                            additionalHeaders.addAll("x-ob-permissions", permissions.stream().map(FRExternalPermissionsCode::name).collect(Collectors.toList()));
                            additionalHeaders.add("x-ob-psu-user-id", accountRequest.getUserId());
                            ResponseEntity response = rsStoreGateway.toRsStore(request, additionalHeaders,
                                    ReadCustomerInfo.class);
                            if(response.getStatusCode() == HttpStatus.OK){
                                if(response.getBody() != null){
                                    ReadCustomerInfo readCustomerInfo = (ReadCustomerInfo) response.getBody();
                                    CustomerInfo customerInfo = readCustomerInfo.getData();
                                    if(customerInfo != null){
                                        LocalDate birthdate = customerInfo.getBirthdate();
                                        LocalDate now = LocalDate.now();
                                        Period age = Period.fieldDifference(birthdate, now);
                                        if(age.getYears() < 18){
                                            log.info("Customer is under 18 and is forbidden from sharing their " +
                                                    "Customer Info. Returning '{}'", HttpStatus.FORBIDDEN);
                                            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                                        }
                                    }
                                }
                            }
                            log.trace("Returning response with body '{}'", response.getBody());
                            return ResponseEntity.status(response.getStatusCode()).body(response.getBody()) ;
                        }
                );
    }

}
