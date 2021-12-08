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
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.OBRisk1Validator;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.payment.OBRisk1;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class DomesticVrpPaymentsEndpointWrapper extends RSEndpointWrapper<DomesticVrpPaymentsEndpointWrapper, DomesticVrpPaymentsEndpointWrapper.DomesticVrpPaymentRestEndpointContent> {

    private FRDomesticVRPConsent consent;
    private final OBRisk1Validator riskValidator;
    private boolean isFundsConfirmationRequest;

    public DomesticVrpPaymentsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                              TppStoreService tppStoreService,
                                              OBRisk1Validator riskValidator) {
        super(RSEndpointWrapperService, tppStoreService);
        this.riskValidator = riskValidator;
    }

    public DomesticVrpPaymentsEndpointWrapper payment(FRDomesticVRPConsent consent) {
        this.consent = consent;
        return this;
    }

    public DomesticVrpPaymentsEndpointWrapper isFundsConfirmationRequest(boolean isFundsConfirmationRequest) {
        this.isFundsConfirmationRequest = isFundsConfirmationRequest;
        return this;
    }


    @Override
    protected ResponseEntity run(DomesticVrpPaymentRestEndpointContent main) throws OBErrorException, JsonProcessingException {
        return main.run(oAuth2ClientId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        List grantTypes = Arrays.asList(OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        // the grant type for funds confirmation endpoint is different than the others payment endpoints
        if (isFundsConfirmationRequest) {
            grantTypes = Arrays.asList(
                    OIDCConstants.GrantType.AUTHORIZATION_CODE
            );
        }
        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS), grantTypes);
        verifyMatlsFromAccessToken();
    }

    public void validateRisk(OBRisk1 risk) throws OBErrorException {
        if (riskValidator != null) {
            riskValidator.validate(risk);
        } else {
            String errorString = "validatePaymentCodeContext called but no validator present";
            log.error(errorString);
            throw new NullPointerException(errorString);
        }
    }

    public interface DomesticVrpPaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
