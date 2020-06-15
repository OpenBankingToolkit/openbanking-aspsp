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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;


public class OBRisk1ValidatorTest {

    @Test
    public void validate_no_side_effects_when_disabled_null_risk() throws OBErrorException {
        // Given
        OBRisk1Validator disabledValidator = new OBRisk1Validator(false);

        // When
        disabledValidator.validate(null);
    }

    @Test(expected = OBErrorException.class)
    public void validate_fails_when_null_risk() throws OBErrorException {
        // Given
        OBRisk1Validator enabledValidator = new OBRisk1Validator(true);

        // When
        enabledValidator.validate(null);
    }

    @Test
    public void validate_no_side_effects_when_disabled_null_pcc() throws OBErrorException {
        // Given
        OBRisk1Validator disabledValidator = new OBRisk1Validator(false);

        // When
        OBRisk1 risk = new OBRisk1();
        disabledValidator.validate(risk);
    }

    @Test(expected = OBErrorException.class)
    public void validate_fails_when_null_pcc() throws OBErrorException {
        // Given
        OBRisk1Validator disabledValidator = new OBRisk1Validator(true);

        // When
        OBRisk1 risk = new OBRisk1();
        disabledValidator.validate(risk);
    }

    @Test
    public void validate_no_throw_when_valid_pcc() throws OBErrorException {
        // Given
        OBRisk1Validator disabledValidator = new OBRisk1Validator(true);

        // When
        OBRisk1 risk = new OBRisk1();
        risk.setPaymentContextCode(OBExternalPaymentContext1Code.BILLPAYMENT);
        disabledValidator.validate(risk);
    }
}