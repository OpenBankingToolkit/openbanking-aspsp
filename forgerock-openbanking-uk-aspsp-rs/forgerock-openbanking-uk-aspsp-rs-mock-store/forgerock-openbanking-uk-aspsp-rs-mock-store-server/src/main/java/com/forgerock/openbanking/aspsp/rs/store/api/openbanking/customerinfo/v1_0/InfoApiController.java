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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.customerinfo.v1_0;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.repositories.TppRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.org.openbanking.datamodel.customerinfo.ReadCustomerInfo;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-12-20T11:13:54.447312Z[Europe/London]")
@Controller
@Slf4j
@RequestMapping("${openapi.customerInfoAPISpecification.base-path:/customer-info/v1.0}")
public class InfoApiController implements InfoApi {

    private final TppRepository tppRepository;

    private final ConsentMetricService consentMetricService;

    private final AccountStoreService accountsStoreService;



    @Autowired
    public InfoApiController(TppRepository tppRepository, ConsentMetricService consentMetricService,
                             AccountStoreService accountsStoreService) {
        this.tppRepository = tppRepository;
        this.consentMetricService = consentMetricService;
        this.accountsStoreService = accountsStoreService;
    }

    @Override
    public ResponseEntity<ReadCustomerInfo> getCustomerInfo(String authorization,
                                                            String xFapiAuthDate,
                                                            String xFapiCustomerIpAddress,
                                                            String xFapiInteractionId,
                                                            String xCustomerUserAgent,
                                                            String xObPsuUserId){
        log.debug("getCustomerInfo called()");

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
