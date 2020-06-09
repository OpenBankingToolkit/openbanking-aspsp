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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import uk.org.openbanking.datamodel.payment.OBWriteDataInternationalConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent4;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalConsent4Data;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalConverter.toOBWriteInternational3DataInitiation;

public class OBWriteInternationalConsentConverter {

    public static OBWriteInternationalConsent4 toOBWriteInternationalConsent4(OBWriteInternationalConsent2 obWriteInternationalConsent2) {
        return (new OBWriteInternationalConsent4())
                .data(toOBWriteInternationalConsent4Data(obWriteInternationalConsent2.getData()))
                .risk(obWriteInternationalConsent2.getRisk());
    }

    public static OBWriteInternationalConsent4Data toOBWriteInternationalConsent4Data(OBWriteDataInternationalConsent2 data) {
        return data == null ? null : (new OBWriteInternationalConsent4Data())
                .initiation(toOBWriteInternational3DataInitiation(data.getInitiation()))
                .authorisation(toOBWriteDomesticConsent3DataAuthorisation(data.getAuthorisation()));
    }
}
