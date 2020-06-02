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
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalScheduledConverter.toOBInternationalScheduled2;

public class OBWriteInternationalScheduledConsentConverter {

    public static OBWriteInternationalScheduledConsent2 toOBWriteInternationalScheduledConsent2(OBWriteInternationalScheduledConsent4 obWriteInternationalScheduledConsent4) {
        return (new OBWriteInternationalScheduledConsent2())
                .data(toOBWriteDataInternationalScheduledConsent2(obWriteInternationalScheduledConsent4.getData()))
                .risk(obWriteInternationalScheduledConsent4.getRisk());
    }

    public static OBWriteDataInternationalScheduledConsent2 toOBWriteDataInternationalScheduledConsent2(OBWriteInternationalScheduledConsent4Data data) {
        return (new OBWriteDataInternationalScheduledConsent2())
                .permission(OBExternalPermissions2Code.valueOf(data.getPermission().name()))
                .initiation(toOBInternationalScheduled2(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
    }
}
