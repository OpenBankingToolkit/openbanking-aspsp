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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.vrp.v3_1_8;

import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPConsentResponse;
import uk.org.openbanking.datamodel.vrp.OBVRPFundsConfirmationRequest;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-27T13:44:23.551801+01:00[Europe/London]")

@Controller("DomesticVrpConsentsApiControllerV3.1.8")
public class DomesticVrpConsentsApiController implements DomesticVrpConsentsApi {

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> createDomesticVrpConsent(
            OBDomesticVRPConsentRequest domesticVRPConsentRequest
    ) throws OBErrorResponseException {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<OBDomesticVRPConsentResponse> getDomesticVrpConsent() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Void> deleteDomesticVrpConsent() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


    @Override
    public ResponseEntity<Object> createDomesticVrpConsentsConsentFundsConfirmation(
            OBVRPFundsConfirmationRequest vrpFundsConfirmationRequest
    ) throws OBErrorResponseException {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
