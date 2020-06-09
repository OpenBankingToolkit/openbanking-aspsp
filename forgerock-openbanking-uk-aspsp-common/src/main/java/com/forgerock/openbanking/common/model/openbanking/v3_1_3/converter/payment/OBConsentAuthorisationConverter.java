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

import uk.org.openbanking.datamodel.payment.OBAuthorisation1;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent3DataAuthorisation;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAuthorisationCodeConverter.toAuthorisationTypeEnum;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAuthorisationCodeConverter.toOBExternalAuthorisation1Code;

public class OBConsentAuthorisationConverter {

    public static OBWriteDomesticConsent3DataAuthorisation toOBWriteDomesticConsent3DataAuthorisation(OBAuthorisation1 authorisation) {
        return authorisation == null ? null : (new OBWriteDomesticConsent3DataAuthorisation())
                .authorisationType(toAuthorisationTypeEnum(authorisation.getAuthorisationType()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }

    public static OBAuthorisation1 toOBAuthorisation1(OBWriteDomesticConsent3DataAuthorisation authorisation) {
        return authorisation == null ? null : (new OBAuthorisation1())
                .authorisationType(toOBExternalAuthorisation1Code(authorisation.getAuthorisationType()))
                .completionDateTime(authorisation.getCompletionDateTime());
    }

}
