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

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Collections;

import static com.forgerock.openbanking.model.error.OBRIErrorType.REQUEST_FIELD_INVALID;


@Slf4j
public class FundsConfirmationConsentApiEndpointWrapper extends RSEndpointWrapper<FundsConfirmationConsentApiEndpointWrapper, FundsConfirmationConsentApiEndpointWrapper.FundsConfirmationRestEndpointContent> {

    public FundsConfirmationConsentApiEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                                      TppStoreService tppStoreService) {
        super(RSEndpointWrapperService, tppStoreService);
    }

    @Override
    protected ResponseEntity run(FundsConfirmationRestEndpointContent main) throws OBErrorException {
        try {
            return main.run(oAuth2ClientId);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                // Change the 404 to 400 in line with V3 spec - implication is that client has submitted a non-existent id in URL therefore a bad request as opposed to resource not found
                // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/641992418/Read+Write+Data+API+Specification+-+v3.0#Read/WriteDataAPISpecification-v3.0-400(BadRequest)v/s404(NotFound)
                log.debug("Resource server returned 404 so an incorrect resource has been requested.");
                throw new OBErrorException(REQUEST_FIELD_INVALID, "resource does not exist");
            }
            throw e;
        }
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Collections.singletonList(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS),
                Arrays.asList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL
                )
        );

        verifyMatlsFromAccessToken();
    }


    public interface FundsConfirmationRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
