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

import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent3;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBDomesticConverter.toOBDomestic2;

public class OBWriteDomesticConsentConverter {

    public static OBWriteDomesticConsent2 toOBWriteDomesticConsent2(OBWriteDomesticConsent3 obWriteDomesticConsent3) {
        return (new OBWriteDomesticConsent2())
                .data((new OBWriteDataDomesticConsent2())
                        .authorisation(toOBAuthorisation1(obWriteDomesticConsent3.getData().getAuthorisation()))
                        .initiation(toOBDomestic2(obWriteDomesticConsent3.getData().getInitiation())))
                .risk(obWriteDomesticConsent3.getRisk());
    }

}
