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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_1_3.party;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.account.OBReadParty2;
import uk.org.openbanking.datamodel.account.OBReadParty3;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller("PartyApiV3.1.3")
@Slf4j
public class PartyApiController implements PartyApi {

    private final RSEndpointWrapperService rsEndpointWrapperService;

    private final com.forgerock.openbanking.aspsp.rs.api.account.v3_1_2.party.PartyApiController previousVersionController;

    public PartyApiController(RSEndpointWrapperService rsEndpointWrapperService,
                              @Qualifier("PartyApiV3.1.2") com.forgerock.openbanking.aspsp.rs.api.account.v3_1_2.party.PartyApiController previousVersionController) {
        this.rsEndpointWrapperService = rsEndpointWrapperService;
        this.previousVersionController = previousVersionController;
    }

    @Override
    public ResponseEntity<OBReadParty2> getAccountParty(String accountId,
                                                        String authorization,
                                                        DateTime xFapiAuthDate,
                                                        String xFapiCustomerIpAddress,
                                                        String xFapiInteractionId,
                                                        String xCustomerUserAgent,
                                                        HttpServletRequest request,
                                                        Principal principal) throws OBErrorResponseException {
        return previousVersionController.getAccountParty(
                accountId,
                rsEndpointWrapperService.rsConfiguration.financialId,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }

    @Override
    public ResponseEntity<OBReadParty3> getAccountParties(String accountId,
                                                          String authorization,
                                                          DateTime xFapiAuthDate,
                                                          String xFapiCustomerIpAddress,
                                                          String xFapiInteractionId,
                                                          String xCustomerUserAgent,
                                                          HttpServletRequest request,
                                                          Principal principal) throws OBErrorResponseException {
        return previousVersionController.getAccountParties(
                accountId,
                rsEndpointWrapperService.rsConfiguration.financialId,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }

    @Override
    public ResponseEntity<OBReadParty2> getParty(String authorization,
                                                 DateTime xFapiAuthDate,
                                                 String xFapiCustomerIpAddress,
                                                 String xFapiInteractionId,
                                                 String xCustomerUserAgent,
                                                 HttpServletRequest request,
                                                 Principal principal) throws OBErrorResponseException {
        return previousVersionController.getParty(
                rsEndpointWrapperService.rsConfiguration.financialId,
                authorization,
                xFapiAuthDate,
                xFapiCustomerIpAddress,
                xFapiInteractionId,
                xCustomerUserAgent,
                request,
                principal);
    }
}
