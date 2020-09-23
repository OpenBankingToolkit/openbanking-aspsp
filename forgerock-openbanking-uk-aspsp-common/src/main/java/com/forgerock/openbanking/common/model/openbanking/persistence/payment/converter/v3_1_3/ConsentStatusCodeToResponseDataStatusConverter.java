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
package com.forgerock.openbanking.common.model.openbanking.persistence.payment.converter.v3_1_3;

import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import uk.org.openbanking.datamodel.payment.*;

public class ConsentStatusCodeToResponseDataStatusConverter {

    public static OBWriteDomesticStandingOrderConsentResponse4Data.StatusEnum toOBWriteDomesticStandingOrderConsentResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticStandingOrderConsentResponse4Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticStandingOrderConsentResponse4Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticStandingOrderConsentResponse4Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticStandingOrderConsentResponse4Data.StatusEnum.REJECTED;

        }
    }

    public static OBWriteDomesticResponse3Data.StatusEnum toOBWriteDomesticResponse3DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            // TODO #216 - check these values are correct
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
                return OBWriteDomesticResponse3Data.StatusEnum.ACCEPTEDSETTLEMENTCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
                return OBWriteDomesticResponse3Data.StatusEnum.ACCEPTEDSETTLEMENTINPROCESS;
            case REJECTED:
            case REVOKED:
                return OBWriteDomesticResponse3Data.StatusEnum.REJECTED;
            default:
                return OBWriteDomesticResponse3Data.StatusEnum.PENDING;
        }
    }

    public static OBWriteDomesticScheduledResponse3Data.StatusEnum toOBWriteDomesticScheduledResponse3DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteDomesticScheduledResponse3Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteDomesticScheduledResponse3Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteDomesticScheduledResponse3Data.StatusEnum.CANCELLED;
            default:
                return OBWriteDomesticScheduledResponse3Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteDomesticStandingOrderResponse4Data.StatusEnum toOBWriteDomesticStandingOrderResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteDomesticStandingOrderResponse4Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteDomesticStandingOrderResponse4Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteDomesticStandingOrderResponse4Data.StatusEnum.CANCELLED;
            default:
                return OBWriteDomesticStandingOrderResponse4Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteFileConsentResponse3Data.StatusEnum toOBWriteFileConsentResponse3DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case AWAITINGUPLOAD:
                return OBWriteFileConsentResponse3Data.StatusEnum.AWAITINGUPLOAD;
            case AWAITINGAUTHORISATION:
                return OBWriteFileConsentResponse3Data.StatusEnum.AWAITINGAUTHORISATION;
            case AUTHORISED:
                return OBWriteFileConsentResponse3Data.StatusEnum.AUTHORISED;
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteFileConsentResponse3Data.StatusEnum.CONSUMED;
            default:
                return OBWriteFileConsentResponse3Data.StatusEnum.REJECTED;

        }
    }

    public static OBWriteDomesticConsentResponse3Data.StatusEnum toOBWriteDomesticConsentResponse3DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticConsentResponse3Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticConsentResponse3Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticConsentResponse3Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticConsentResponse3Data.StatusEnum.REJECTED;
        }
    }

    public static  OBWriteDomesticScheduledConsentResponse3Data.StatusEnum toOBWriteDomesticScheduledConsentResponse3DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticScheduledConsentResponse3Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticScheduledConsentResponse3Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticScheduledConsentResponse3Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticScheduledConsentResponse3Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalConsentResponse4Data.StatusEnum toOBWriteInternationalConsentResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalConsentResponse4Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalConsentResponse4Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalConsentResponse4Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalConsentResponse4Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalResponse4Data.StatusEnum toOBWriteInternationalResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            // TODO #216 - check these values are correct
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
                return OBWriteInternationalResponse4Data.StatusEnum.ACCEPTEDSETTLEMENTCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
                return OBWriteInternationalResponse4Data.StatusEnum.ACCEPTEDSETTLEMENTINPROCESS;
            case REJECTED:
            case REVOKED:
                return OBWriteInternationalResponse4Data.StatusEnum.REJECTED;
            default:
                return OBWriteInternationalResponse4Data.StatusEnum.PENDING;
        }
    }

    public static OBWriteInternationalScheduledConsentResponse4Data.StatusEnum toOBWriteInternationalScheduledConsentResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalScheduledConsentResponse4Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalScheduledConsentResponse4Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalScheduledConsentResponse4Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalScheduledConsentResponse4Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalScheduledResponse4Data.StatusEnum toOBWriteInternationalScheduledResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteInternationalScheduledResponse4Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteInternationalScheduledResponse4Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteInternationalScheduledResponse4Data.StatusEnum.CANCELLED;
            default:
                return OBWriteInternationalScheduledResponse4Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteInternationalStandingOrderConsentResponse5Data.StatusEnum toOBWriteInternationalStandingOrderConsentResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalStandingOrderConsentResponse5Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalStandingOrderConsentResponse5Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalStandingOrderConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalStandingOrderConsentResponse5Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalStandingOrderResponse5Data.StatusEnum toOBWriteInternationalStandingOrderResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteInternationalStandingOrderResponse5Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteInternationalStandingOrderResponse5Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteInternationalStandingOrderResponse5Data.StatusEnum.CANCELLED;
            default:
                return OBWriteInternationalStandingOrderResponse5Data.StatusEnum.INITIATIONFAILED;
        }
    }
}
