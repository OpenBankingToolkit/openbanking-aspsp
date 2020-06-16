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
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;


@RunWith(MockitoJUnitRunner.class)
public class PaymentsApiEndpointWrapperTest {

    private PaymentsApiEndpointWrapper endpointWrapper;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void validatePaymentCodeContext_throwsException() throws OBErrorException {
        expectedEx.expect(OBErrorException.class);
        expectedEx.expectMessage("The 'OBRisk1.PaymentCodeContext' field must be set and be valid");
        OBRisk1Validator riskValidator = new OBRisk1Validator(true);
        endpointWrapper = new PaymentsApiEndpointWrapper(null, null, null, null, riskValidator);

        OBWriteDomesticConsent2 consent = new OBWriteDomesticConsent2();
        endpointWrapper.validateRisk(consent.getRisk());
    }


    @Test
    public void validatePaymentCodeContext_no_validator() throws OBErrorException {
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("validatePaymentCodeContext called but no validator present");
        OBRisk1Validator riskValidator = new OBRisk1Validator(true);
        endpointWrapper = new PaymentsApiEndpointWrapper(null, null, null, null, null);

        OBWriteDomesticConsent2 consent = new OBWriteDomesticConsent2();
        endpointWrapper.validateRisk(consent.getRisk());
    }
}