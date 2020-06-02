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

import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalStandingOrderConverter.toOBInternationalStandingOrder3;

public class OBWriteInternationalStandingOrderConsentConverter {

    public static OBWriteInternationalStandingOrderConsent3 toOBWriteInternationalStandingOrderConsent3(OBWriteInternationalStandingOrderConsent5 obWriteInternationalStandingOrderConsent5) {
        return (new OBWriteInternationalStandingOrderConsent3())
                .data(toOBWriteDataInternationalStandingOrderConsent3(obWriteInternationalStandingOrderConsent5.getData()))
                .risk(obWriteInternationalStandingOrderConsent5.getRisk());
    }

    public static OBWriteDataInternationalStandingOrderConsent3 toOBWriteDataInternationalStandingOrderConsent3(OBWriteInternationalStandingOrderConsent5Data data) {
        return (new OBWriteDataInternationalStandingOrderConsent3())
                .permission(OBExternalPermissions2Code.valueOf(data.getPermission().name()))
                .initiation(toOBInternationalStandingOrder3(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }

    public static OBWriteInternationalStandingOrder3 toOBWriteInternationalStandingOrder3(OBWriteInternationalStandingOrder4 obWriteInternationalStandingOrder4) {
        return (new OBWriteInternationalStandingOrder3())
                .data(toOBWriteDataInternationalStandingOrder3(obWriteInternationalStandingOrder4.getData()))
                .risk(obWriteInternationalStandingOrder4.getRisk());
    }

    public static OBWriteDataInternationalStandingOrder3 toOBWriteDataInternationalStandingOrder3(OBWriteInternationalStandingOrder4Data data) {
        return (new OBWriteDataInternationalStandingOrder3())
                .consentId(data.getConsentId())
                .initiation(toOBInternationalStandingOrder3(data.getInitiation()));
    }
}
