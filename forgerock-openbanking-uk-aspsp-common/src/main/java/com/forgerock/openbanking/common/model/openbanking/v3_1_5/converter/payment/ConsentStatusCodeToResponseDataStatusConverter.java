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
package com.forgerock.openbanking.common.model.openbanking.v3_1_5.converter.payment;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import uk.org.openbanking.datamodel.payment.*;

public class ConsentStatusCodeToResponseDataStatusConverter {

    public static OBWriteDomesticConsentResponse5Data.StatusEnum toOBWriteDomesticConsentResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticConsentResponse5Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticConsentResponse5Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticConsentResponse5Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteDomesticResponse5Data.StatusEnum toOBWriteDomesticResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            // TODO #216 - check these values are correct
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
                return OBWriteDomesticResponse5Data.StatusEnum.ACCEPTEDSETTLEMENTCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
                return OBWriteDomesticResponse5Data.StatusEnum.ACCEPTEDSETTLEMENTINPROCESS;
            case REJECTED:
            case REVOKED:
                return OBWriteDomesticResponse5Data.StatusEnum.REJECTED;
            default:
                return OBWriteDomesticResponse5Data.StatusEnum.PENDING;
        }
    }

    public static  OBWriteDomesticScheduledConsentResponse5Data.StatusEnum toOBWriteDomesticScheduledConsentResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticScheduledConsentResponse5Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticScheduledConsentResponse5Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticScheduledConsentResponse5Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticScheduledConsentResponse5Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteDomesticScheduledResponse5Data.StatusEnum toOBWriteDomesticScheduledResponse5DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteDomesticScheduledResponse5Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteDomesticScheduledResponse5Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteDomesticScheduledResponse5Data.StatusEnum.CANCELLED;
            default:
                return OBWriteDomesticScheduledResponse5Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteDomesticStandingOrderConsentResponse6Data.StatusEnum toOBWriteDomesticStandingOrderConsentResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteDomesticStandingOrderConsentResponse6Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteDomesticStandingOrderConsentResponse6Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteDomesticStandingOrderConsentResponse6Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteDomesticStandingOrderConsentResponse6Data.StatusEnum.REJECTED;

        }
    }

    public static OBWriteDomesticStandingOrderResponse6Data.StatusEnum toOBWriteDomesticStandingOrderResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteDomesticStandingOrderResponse6Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteDomesticStandingOrderResponse6Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteDomesticStandingOrderResponse6Data.StatusEnum.CANCELLED;
            default:
                return OBWriteDomesticStandingOrderResponse6Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteFileConsentResponse4Data.StatusEnum toOBWriteFileConsentResponse4DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case AWAITINGUPLOAD:
                return OBWriteFileConsentResponse4Data.StatusEnum.AWAITINGUPLOAD;
            case AWAITINGAUTHORISATION:
                return OBWriteFileConsentResponse4Data.StatusEnum.AWAITINGAUTHORISATION;
            case AUTHORISED:
                return OBWriteFileConsentResponse4Data.StatusEnum.AUTHORISED;
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteFileConsentResponse4Data.StatusEnum.CONSUMED;
            default:
                return OBWriteFileConsentResponse4Data.StatusEnum.REJECTED;

        }
    }

    public static OBWriteFileResponse3Data.StatusEnum toOBWriteFileResponse3DataStatus(ConsentStatusCode status) {
        switch (status) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteFileResponse3Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteFileResponse3Data.StatusEnum.INITIATIONPENDING;
            default:
                return OBWriteFileResponse3Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteInternationalConsentResponse6Data.StatusEnum toOBWriteInternationalConsentResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalConsentResponse6Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalConsentResponse6Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalConsentResponse6Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalConsentResponse6Data.StatusEnum.REJECTED;
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

    public static OBWriteInternationalScheduledConsentResponse6Data.StatusEnum toOBWriteInternationalScheduledConsentResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalScheduledConsentResponse6Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalScheduledConsentResponse6Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalScheduledConsentResponse6Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalScheduledConsentResponse6Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalScheduledResponse6Data.StatusEnum toOBWriteInternationalScheduledResponse6DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteInternationalScheduledResponse6Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteInternationalScheduledResponse6Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteInternationalScheduledResponse6Data.StatusEnum.CANCELLED;
            default:
                return OBWriteInternationalScheduledResponse6Data.StatusEnum.INITIATIONFAILED;
        }
    }

    public static OBWriteInternationalStandingOrderConsentResponse7Data.StatusEnum toOBWriteInternationalStandingOrderConsentResponse7DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBWriteInternationalStandingOrderConsentResponse7Data.StatusEnum.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBWriteInternationalStandingOrderConsentResponse7Data.StatusEnum.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBWriteInternationalStandingOrderConsentResponse7Data.StatusEnum.AWAITINGAUTHORISATION;
            default:
                return OBWriteInternationalStandingOrderConsentResponse7Data.StatusEnum.REJECTED;
        }
    }

    public static OBWriteInternationalStandingOrderResponse7Data.StatusEnum toOBWriteInternationalStandingOrderResponse7DataStatus(ConsentStatusCode consentStatusCode) {
        switch (consentStatusCode) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBWriteInternationalStandingOrderResponse7Data.StatusEnum.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBWriteInternationalStandingOrderResponse7Data.StatusEnum.INITIATIONPENDING;
            case REVOKED:
                return OBWriteInternationalStandingOrderResponse7Data.StatusEnum.CANCELLED;
            default:
                return OBWriteInternationalStandingOrderResponse7Data.StatusEnum.INITIATIONFAILED;
        }
    }

}
