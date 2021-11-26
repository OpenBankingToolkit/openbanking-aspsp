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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

public class DomesticVrpPaymentsEndpointWrapper extends RSEndpointWrapper<DomesticVrpPaymentsEndpointWrapper, DomesticVrpPaymentsEndpointWrapper.DomesticVrpPaymentRestEndpointContent> {

    private FRDomesticVRPConsent consent;

    public DomesticVrpPaymentsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                              TppStoreService tppStoreService) {
        super(RSEndpointWrapperService, tppStoreService);
    }

    public DomesticVrpPaymentsEndpointWrapper payment(FRDomesticVRPConsent consent) {
        this.consent = consent;
        return this;
    }

    @Override
    protected ResponseEntity run(DomesticVrpPaymentRestEndpointContent main) throws OBErrorException, JsonProcessingException {
        return main.run(oAuth2ClientId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Collections.singletonList(OpenBankingConstants.Scope.PAYMENTS),
                Arrays.asList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL,
                        OIDCConstants.GrantType.AUTHORIZATION_CODE // for funds confirmation
                )
        );

        verifyMatlsFromAccessToken();
    }

    public interface DomesticVrpPaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
