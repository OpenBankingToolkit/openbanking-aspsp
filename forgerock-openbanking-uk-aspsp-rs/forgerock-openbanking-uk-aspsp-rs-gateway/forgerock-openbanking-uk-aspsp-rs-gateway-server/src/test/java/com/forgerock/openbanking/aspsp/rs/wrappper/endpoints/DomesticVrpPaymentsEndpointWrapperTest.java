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

import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.OBRisk1Validator;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRWriteDomesticVRPDataInitiation;
import com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConverters;
import com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRWriteDomesticVRPDataInitiationConverter;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.integration.test.support.FRVrpTestDataFactory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.error.OBStandardErrorCodes1;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInitiation;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequest;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPCommonTestDataFactory;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory;

import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toOBRisk1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@RunWith(MockitoJUnitRunner.class)
public class DomesticVrpPaymentsEndpointWrapperTest {

    @Mock
    RSEndpointWrapperService endpointWrapperService;

    @Mock
    TppStoreService tppStoreService;

    @Mock
    OBRisk1Validator riskValidator;

    @Test
    public void success_validateRisk() throws OBErrorException {
        // Given
        DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper =
                new DomesticVrpPaymentsEndpointWrapper(endpointWrapperService, tppStoreService, riskValidator);
        OBDomesticVRPRequest vrpRequest = OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPRequest();
        FRDomesticVRPConsent vrpConsent = FRVrpTestDataFactory.aValidFRDomesticVRPConsent();
        vrpRequest.setRisk(toOBRisk1(vrpConsent.getRisk()));

        // When
        domesticVrpPaymentsEndpointWrapper.checkRequestAndConsentRiskMatch(vrpRequest, vrpConsent);

        // Then
        // If no exception then we're good
    }

    @Test
    public void fail_validateRisk() throws OBErrorException {
        // Given
        DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper =
                new DomesticVrpPaymentsEndpointWrapper(endpointWrapperService, tppStoreService, riskValidator);
        OBDomesticVRPRequest vrpRequest = OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPRequest();
        FRDomesticVRPConsent vrpConsent = FRVrpTestDataFactory.aValidFRDomesticVRPConsent();
        vrpRequest.setRisk(toOBRisk1(vrpConsent.getRisk()));
        vrpRequest.getRisk().setMerchantCategoryCode("mismatched Merchange Category Code");

        // When
        OBErrorException exception =
                catchThrowableOfType(() ->
                        domesticVrpPaymentsEndpointWrapper.checkRequestAndConsentRiskMatch(vrpRequest, vrpConsent),
                        OBErrorException.class);

        // Then
        assertThat(exception.getObriErrorType()).isEqualTo(OBRIErrorType.REQUEST_VRP_RISK_DOESNT_MATCH_CONSENT);
    }

    @Test
    public void success_checkRequestAndConsentInitiationMatch() throws OBErrorException {
        // Given
        DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper =
                new DomesticVrpPaymentsEndpointWrapper(endpointWrapperService, tppStoreService, riskValidator);
        OBDomesticVRPInitiation requestInitiation = OBDomesticVRPCommonTestDataFactory.aValidOBDomesticVRPInitiation();
        FRDomesticVRPConsent frConsent = FRVrpTestDataFactory.aValidFRDomesticVRPConsent();
        FRWriteDomesticVRPDataInitiation matchingInitiation = FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation(requestInitiation);
        frConsent.getVrpDetails().getData().setInitiation(matchingInitiation);

        // When
        domesticVrpPaymentsEndpointWrapper.checkRequestAndConsentInitiationMatch(requestInitiation, frConsent);

        // Then
        // If no exception then we're good!
    }

    @Test
    public void fail_checkRequestAndConsentInitiationMatch() throws OBErrorException {
        // Given
        DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper =
                new DomesticVrpPaymentsEndpointWrapper(endpointWrapperService, tppStoreService,
                        riskValidator);
          // Create the request data
        OBDomesticVRPInitiation requestInitiation = OBDomesticVRPCommonTestDataFactory.aValidOBDomesticVRPInitiation();
          // Create an FR Consent with slightly differing initiation data
        FRDomesticVRPConsent frConsent = FRVrpTestDataFactory.aValidFRDomesticVRPConsent();
        FRWriteDomesticVRPDataInitiation differentInitiationData = FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation(requestInitiation);
        differentInitiationData.getDebtorAccount().setIdentification("mismatched identification");
        frConsent.getVrpDetails().getData().setInitiation(differentInitiationData);

        // When
        OBErrorException exception =
                catchThrowableOfType(() -> domesticVrpPaymentsEndpointWrapper.checkRequestAndConsentInitiationMatch(
                        requestInitiation, frConsent), OBErrorException.class);

        // Then
        assertThat(exception.getObriErrorType()).isEqualTo(OBRIErrorType.REQUEST_VRP_INITIATION_DOESNT_MATCH_CONSENT);
        assertThat(exception.getOBError().getErrorCode()).isEqualTo(OBStandardErrorCodes1.UK_OBIE_RESOURCE_CONSENT_MISMATCH.toString());
    }

    /**
     * If the CreditorAccount was not specified in the consent, the CreditorAccount must be specified in the
     * instruction.
     */
    @Test
    public void success_checkCreditorAccountIsInInstructionIfNotInConsent() throws OBErrorException {
        // Given
        DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper =
                new DomesticVrpPaymentsEndpointWrapper(endpointWrapperService, tppStoreService,
                        riskValidator);
            // Create the request data
        OBDomesticVRPRequest vrpRequest = OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPRequest();

            // Create an FR Consent with slightly differing initiation data
        FRDomesticVRPConsent frConsent = FRVrpTestDataFactory.aValidFRDomesticVRPConsent();
        frConsent.getVrpDetails().getData().getInitiation().setCreditorAccount(null);


        // When
        domesticVrpPaymentsEndpointWrapper.checkCreditorAccountIsInInstructionIfNotInConsent(vrpRequest, frConsent);

        // Then

    }

    /**
     * If the CreditorAccount was not specified in the consent, the CreditorAccount must be specified in the
     * instruction.
     */
    @Test
    public void fail_checkCreditorAccountIsInInstructionIfNotInConsent() throws OBErrorException {
        // Given
        DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper =
                new DomesticVrpPaymentsEndpointWrapper(endpointWrapperService, tppStoreService,
                        riskValidator);
        // Create the request data
        OBDomesticVRPRequest vrpRequest = OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPRequest();
        vrpRequest.getData().getInitiation().setCreditorAccount(null);
        vrpRequest.getData().getInstruction().setCreditorAccount(null);

        // Create an FR Consent with slightly differing initiation data
        FRDomesticVRPConsent frConsent = FRVrpTestDataFactory.aValidFRDomesticVRPConsent();
        frConsent.getVrpDetails().getData().getInitiation().setCreditorAccount(null);


        // When
        OBErrorException exception =
                catchThrowableOfType(() ->
                        domesticVrpPaymentsEndpointWrapper.checkCreditorAccountIsInInstructionIfNotInConsent(vrpRequest, frConsent),
                        OBErrorException.class);

        // Then
        assertThat(exception.getObriErrorType()).isEqualTo(OBRIErrorType.REQUEST_VRP_CREDITOR_ACCOUNT_NOT_SPECIFIED);
        assertThat(exception.getOBError().getErrorCode()).isEqualTo(OBStandardErrorCodes1.UK_OBIE_RESOURCE_CONSENT_MISMATCH.toString());

    }

}
