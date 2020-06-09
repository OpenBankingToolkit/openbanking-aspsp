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

import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent3;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent3Data;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticScheduledConsent3Data.PermissionEnum;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBDomesticScheduledConverter.toOBWriteDomesticScheduled2DataInitiation;

public class OBWriteDomesticScheduledConsentConverter {

    public static OBWriteDomesticScheduledConsent3 toOBWriteDomesticScheduledConsent3(OBWriteDomesticScheduledConsent2 obWriteDomesticScheduledConsent2) {
        return (new OBWriteDomesticScheduledConsent3())
                .data(toOBWriteDomesticScheduledConsent3Data(obWriteDomesticScheduledConsent2))
                .risk(obWriteDomesticScheduledConsent2.getRisk());
    }

    public static OBWriteDomesticScheduledConsent3Data toOBWriteDomesticScheduledConsent3Data(OBWriteDomesticScheduledConsent2 obWriteDomesticScheduledConsent2) {
        return obWriteDomesticScheduledConsent2.getData() == null ? null : (new OBWriteDomesticScheduledConsent3Data())
                .permission(toPermissionEnum(obWriteDomesticScheduledConsent2.getData()))
                .initiation(toOBWriteDomesticScheduled2DataInitiation(obWriteDomesticScheduledConsent2.getData().getInitiation()))
                .authorisation(toOBWriteDomesticConsent3DataAuthorisation(obWriteDomesticScheduledConsent2.getData().getAuthorisation()));
    }

    private static PermissionEnum toPermissionEnum(OBWriteDataDomesticScheduledConsent2 data) {
        return data.getPermission() == null ? null : PermissionEnum.valueOf(data.getPermission().name());
    }

}
