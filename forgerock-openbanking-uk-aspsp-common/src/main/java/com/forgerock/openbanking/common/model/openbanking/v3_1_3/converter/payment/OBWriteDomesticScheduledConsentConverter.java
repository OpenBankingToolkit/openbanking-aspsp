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
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBDomesticScheduledConverter.toOBDomesticScheduled2;

public class OBWriteDomesticScheduledConsentConverter {

    public static OBWriteDomesticScheduledConsent2 toOBWriteDomesticScheduledConsent2(OBWriteDomesticScheduledConsent3 obWriteDomesticScheduledConsent3) {
        return (new OBWriteDomesticScheduledConsent2())
                .data(toOBWriteDataDomesticScheduledConsent2(obWriteDomesticScheduledConsent3.getData()))
                .risk(obWriteDomesticScheduledConsent3.getRisk());
    }

    public static OBWriteDataDomesticScheduledConsent2 toOBWriteDataDomesticScheduledConsent2(OBWriteDomesticScheduledConsent3Data data) {
        return (new OBWriteDataDomesticScheduledConsent2())
                .permission(OBExternalPermissions2Code.fromValue(data.getPermission().getValue()))
                .initiation(toOBDomesticScheduled2(data.getInitiation()))
                .authorisation(toOBAuthorisation1(data.getAuthorisation()));
        //.scASupportData(data.getScASupportData()); - doesn't exit in old version - not sure if this is a problem?
    }
}
