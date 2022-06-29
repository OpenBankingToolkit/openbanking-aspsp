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
package com.forgerock.openbanking.common.services.openbanking.converter.vrp;

import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPInstruction;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPRequest;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPRequestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInitiation;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInstruction;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequestData;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPCommonTestDataFactory;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FRDomesticVRPConvertersTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void success_toFRDomesticVRPRequest() {
        // Given
        OBDomesticVRPRequest obDomesticVRPPaymentRequest = getOBDomesticVRPRequest();

        // When
        FRDomesticVRPRequest frDomesticVRPRequest =
                FRDomesticVRPConverters.toFRDomesticVRPRequest(obDomesticVRPPaymentRequest);

        // Then
        assertThat(frDomesticVRPRequest).isNotNull();
        assertThat(frDomesticVRPRequest.getData()).isNotNull();
        FRDomesticVRPRequestData frRequestData = frDomesticVRPRequest.getData();
        OBDomesticVRPRequestData obRequestData = obDomesticVRPPaymentRequest.getData();

        assertThat(frRequestData.getConsentId()).isNotNull();
        assertThat(frRequestData.getConsentId()).isEqualTo(obRequestData.getConsentId());

        assertThat(frRequestData.getPsuAuthenticationMethod()).isNotNull();
        assertThat(frRequestData.getPsuAuthenticationMethod()).isEqualTo(obRequestData.getPsUAuthenticationMethod());
        
        assertThat(frRequestData.getInitiation()).isNotNull();
        assertThat(frRequestData.getInstruction()).isNotNull();
    }

    private OBDomesticVRPRequest getOBDomesticVRPRequest(){
        return OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPRequest();
    }

    @Test
    public void testGetOBDomesticVRPDataInitiation(){
        // Given
        OBDomesticVRPInitiation obInitiation = getOBDomesticVRPDataInitiation();

        // When
        FRDomesticVRPDataInitiation frInitiation =
                FRWriteDomesticVRPDataInitiationConverter.toFRWriteDomesticVRPDataInitiation(obInitiation);

        // Then
        assertThat(frInitiation).isNotNull();
        assertThat(frInitiation.getCreditorAccount()).isNotNull();
        assertThat(frInitiation.getDebtorAccount()).isNotNull();
        assertThat(frInitiation.getCreditorAgent()).isNotNull();
        assertThat(frInitiation.getRemittanceInformation()).isNotNull();
    }

    private OBDomesticVRPInitiation getOBDomesticVRPDataInitiation() {
        return OBDomesticVRPCommonTestDataFactory.aValidOBDomesticVRPInitiation();
    }

    @Test
    public void success_toOBDomesticVRPRequestDataInstruction(){
        // Given
        OBDomesticVRPInstruction obInstruction = getOBDomesticVRPInstruction();

        // When
        FRDomesticVRPInstruction frInstruction =
                FRDomesticVRPConverters.toFRDomesticVRPInstruction(obInstruction);

        // Then
        assertThat(frInstruction).isNotNull();
        assertThat(frInstruction.getInstructedAmount()).isNotNull();
        assertThat(frInstruction.getInstructionIdentification()).isEqualTo(obInstruction.getInstructionIdentification());
        assertThat(frInstruction.getCreditorAccount()).isNotNull();
        assertThat(frInstruction.getCreditorAgent()).isNotNull();

    }

    private OBDomesticVRPInstruction getOBDomesticVRPInstruction() {
        return OBDomesticVRPRequestTestDataFactory.aValidOBDomesticVRPInstruction();
    }

    @Test
    public void success_toFRRDomesticVRPInstruction() {
        // Given
        OBDomesticVRPInstruction obInstruction = getOBDomesticVRPInstruction();

        // When
        FRDomesticVRPInstruction frInstruction = FRDomesticVRPConverters.toFRDomesticVRPInstruction(obInstruction);

        // Then
        assertThat(frInstruction).isNotNull();
        assertThat(frInstruction.getInstructionIdentification()).isNotNull();
        assertThat(frInstruction.getInstructionIdentification()).isEqualTo(obInstruction.getInstructionIdentification());
        assertThat(frInstruction.getInstructedAmount()).isNotNull();
        assertThat(frInstruction.getLocalInstrument()).isNotNull();
        assertThat(frInstruction.getLocalInstrument()).isEqualTo(obInstruction.getLocalInstrument());
        assertThat(frInstruction.getCreditorAgent()).isNotNull();
        assertThat(frInstruction.getCreditorAccount()).isNotNull();
        assertThat(frInstruction.getEndToEndIdentification()).isNotNull();
        assertThat(frInstruction.getEndToEndIdentification()).isEqualTo(obInstruction.getEndToEndIdentification());
        assertThat(frInstruction.getRemittanceInformation()).isNotNull();
        assertThat(frInstruction.getSupplementaryData()).isNotNull();
    }

}
