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

import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import uk.org.openbanking.datamodel.payment.*;

public class ResponseStatusCodeConverter {
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

    public static OBWriteDomesticScheduledConsentResponse4Data.StatusEnum toOBWriteDomesticScheduledConsentResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticScheduledConsentResponse4Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticScheduledConsentResponse4Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticScheduledConsentResponse4Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticScheduledConsentResponse4Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteDomesticScheduledResponse4Data.StatusEnum toOBWriteDomesticScheduledResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteDomesticScheduledResponse4Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteDomesticScheduledResponse4Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteDomesticScheduledResponse4Data.StatusEnum.CANCELLED;
            default:
                return OBWriteDomesticScheduledResponse4Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteDomesticStandingOrderConsentResponse5Data.StatusEnum toOBWriteDomesticStandingOrderConsentResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticStandingOrderConsentResponse5Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticStandingOrderConsentResponse5Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticStandingOrderConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticStandingOrderConsentResponse5Data.StatusEnum.REJECTED;

        }
    }

    public static OBWriteDomesticStandingOrderResponse5Data.StatusEnum toOBWriteDomesticStandingOrderResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteDomesticStandingOrderResponse5Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteDomesticStandingOrderResponse5Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteDomesticStandingOrderResponse5Data.StatusEnum.CANCELLED;
            default:
                return OBWriteDomesticStandingOrderResponse5Data.StatusEnum.INITIATIONFAILED;
        }
    }


    public static OBWriteInternationalConsentResponse5Data.StatusEnum toOBWriteInternationalConsentResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalConsentResponse5Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalConsentResponse5Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalConsentResponse5Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalResponse5Data.StatusEnum toOBWriteInternationalResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            // TODO #216 - check these values are correct
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
                return OBWriteInternationalResponse5Data.StatusEnum.ACCEPTEDSETTLEMENTCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
                return OBWriteInternationalResponse5Data.StatusEnum.ACCEPTEDSETTLEMENTINPROCESS;
            case REJECTED:
            case REVOKED:
                return OBWriteInternationalResponse5Data.StatusEnum.REJECTED;
            default:
                return OBWriteInternationalResponse5Data.StatusEnum.PENDING;
        }
    }

    public static OBWriteInternationalScheduledConsentResponse5Data.StatusEnum toOBWriteInternationalScheduledConsentResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalScheduledConsentResponse5Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalScheduledConsentResponse5Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalScheduledConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalScheduledConsentResponse5Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalScheduledResponse5Data.StatusEnum toOBWriteInternationalScheduledResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteInternationalScheduledResponse5Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteInternationalScheduledResponse5Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteInternationalScheduledResponse5Data.StatusEnum.CANCELLED;
            default:
                return OBWriteInternationalScheduledResponse5Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteInternationalStandingOrderConsentResponse6Data.StatusEnum toOBWriteInternationalStandingOrderConsentResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalStandingOrderConsentResponse6Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalStandingOrderConsentResponse6Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalStandingOrderConsentResponse6Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalStandingOrderConsentResponse6Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalStandingOrderResponse6Data.StatusEnum toOBWriteInternationalStandingOrderResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteInternationalStandingOrderResponse6Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteInternationalStandingOrderResponse6Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteInternationalStandingOrderResponse6Data.StatusEnum.CANCELLED;
            default:
                return OBWriteInternationalStandingOrderResponse6Data.StatusEnum.INITIATIONFAILED;
        }
    }
}
