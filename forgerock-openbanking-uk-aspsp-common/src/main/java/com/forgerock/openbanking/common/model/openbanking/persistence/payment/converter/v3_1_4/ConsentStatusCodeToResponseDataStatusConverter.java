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
package com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_4;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRReadRefundAccount;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import uk.org.openbanking.datamodel.payment.*;

public class ConsentStatusCodeToResponseDataStatusConverter {

    public static OBWriteDomesticConsentResponse4Data.StatusEnum toOBWriteDomesticConsentResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticConsentResponse4Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticConsentResponse4Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticConsentResponse4Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticConsentResponse4Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteDomesticResponse4Data.StatusEnum toOBWriteDomesticResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            // TODO #216 - check these values are correct
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
                return OBWriteDomesticResponse4Data.StatusEnum.ACCEPTEDSETTLEMENTCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
                return OBWriteDomesticResponse4Data.StatusEnum.ACCEPTEDSETTLEMENTINPROCESS;
            case REJECTED:
            case REVOKED:
                return OBWriteDomesticResponse4Data.StatusEnum.REJECTED;
            default:
                return OBWriteDomesticResponse4Data.StatusEnum.PENDING;
        }
    }

    public static OBWriteDomesticConsentResponse4Data.ReadRefundAccountEnum toOBWriteDomesticConsentResponse4DataReadRefundAccount(FRReadRefundAccount frReadRefundAccount){
        switch (frReadRefundAccount) {
            case YES:
                return OBWriteDomesticConsentResponse4Data.ReadRefundAccountEnum.YES;
            default:
                return OBWriteDomesticConsentResponse4Data.ReadRefundAccountEnum.NO;
        }
    }

}
