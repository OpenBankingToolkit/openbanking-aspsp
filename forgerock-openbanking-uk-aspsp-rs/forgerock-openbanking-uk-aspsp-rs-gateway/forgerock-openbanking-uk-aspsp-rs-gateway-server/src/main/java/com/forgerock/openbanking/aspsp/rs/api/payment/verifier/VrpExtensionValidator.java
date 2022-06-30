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
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationRequest;


/**
 * Extension point to allow additional validation logic to be added to the VRP flow.
 *
 * The {@link NoOpVrpExtensionValidator} should be used when no additional validation is required.
 */
public interface VrpExtensionValidator {

    void validatePaymentRequest(OBDomesticVRPRequest request) throws OBErrorException;

    void validateConsent(OBDomesticVRPConsentRequest consentRequest) throws OBErrorException;

    void validateFundsConfirmationRequest(OBVRPFundsConfirmationRequest fundsConfirmationRequest) throws OBErrorException;

    /**
     * NoOp implementation, to be used when no additional validation is required.
     */
    class NoOpVrpExtensionValidator implements VrpExtensionValidator {
        @Override
        public void validatePaymentRequest(OBDomesticVRPRequest request) {
        }

        @Override
        public void validateConsent(OBDomesticVRPConsentRequest consentRequest) {
        }

        @Override
        public void validateFundsConfirmationRequest(OBVRPFundsConfirmationRequest fundsConfirmationRequest) {
        }
    }
}
