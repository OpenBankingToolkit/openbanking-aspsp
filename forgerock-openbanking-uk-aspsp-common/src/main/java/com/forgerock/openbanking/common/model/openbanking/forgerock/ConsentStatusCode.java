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
package com.forgerock.openbanking.common.model.openbanking.forgerock;

import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.payment.*;

public enum ConsentStatusCode {

    AUTHORISED("Authorised"),
    AWAITINGAUTHORISATION("AwaitingAuthorisation"),
    CONSUMED("Consumed"),
    REJECTED("Rejected"),
    ACCEPTEDCUSTOMERPROFILE("AcceptedCustomerProfile"),
    ACCEPTEDSETTLEMENTCOMPLETED("AcceptedSettlementCompleted"),
    ACCEPTEDSETTLEMENTINPROCESS("AcceptedSettlementInProcess"),
    ACCEPTEDTECHNICALVALIDATION("AcceptedTechnicalValidation"),
    PENDING("Pending"),
    REVOKED("Revoked"),
    AWAITINGUPLOAD("AwaitingUpload");

    private final String value;

    ConsentStatusCode(String value) {
        this.value = value;
    }

    public static ConsentStatusCode fromValue(String value) {
        for (ConsentStatusCode consentStatusCode : ConsentStatusCode.values()) {
            if (consentStatusCode.value.equals(value)) {
                return consentStatusCode;
            }
        }
        throw new IllegalArgumentException("No enum constant '" + value + "'");
    }

    public OBExternalConsentStatus1Code toOBExternalConsentStatus1Code() {
        switch (this) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBExternalConsentStatus1Code.CONSUMED;
            case ACCEPTEDCUSTOMERPROFILE:
            case ACCEPTEDTECHNICALVALIDATION:
            case AUTHORISED:
                return OBExternalConsentStatus1Code.AUTHORISED;
            case AWAITINGAUTHORISATION:
                return OBExternalConsentStatus1Code.AWAITINGAUTHORISATION;
            default:
                return OBExternalConsentStatus1Code.REJECTED;

        }
    }

    public OBWriteDomesticStandingOrderConsentResponse4Data.StatusEnum toOBWriteDomesticStandingOrderConsentResponse4DataStatus() {
        switch (this) {
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

    public OBTransactionIndividualStatus1Code toOBTransactionIndividualStatus1Code() {
        return OBTransactionIndividualStatus1Code.valueOf(name());
    }

    public OBExternalStatus1Code toOBExternalStatusCode1() {
        switch (this) {
            case ACCEPTEDSETTLEMENTCOMPLETED:
                return OBExternalStatus1Code.INITIATIONCOMPLETED;
            case ACCEPTEDSETTLEMENTINPROCESS:
            case PENDING:
                return OBExternalStatus1Code.INITIATIONPENDING;
            default:
                return OBExternalStatus1Code.INITIATIONFAILED;
        }
    }

    public OBExternalConsentStatus2Code toOBExternalConsentStatus2Code() {
        switch (this) {
            case AWAITINGUPLOAD:
                return OBExternalConsentStatus2Code.AWAITINGUPLOAD;
            case AWAITINGAUTHORISATION:
                return OBExternalConsentStatus2Code.AWAITINGAUTHORISATION;
            case AUTHORISED:
                return OBExternalConsentStatus2Code.AUTHORISED;
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
            case CONSUMED:
                return OBExternalConsentStatus2Code.CONSUMED;
            default:
                return OBExternalConsentStatus2Code.REJECTED;

        }
    }

    public OBWriteDomesticConsentResponse3Data.StatusEnum toOBWriteDomesticConsentResponse3DataStatus() {
        switch (this) {
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

    public OBWriteDomesticScheduledConsentResponse3Data.StatusEnum toOBWriteDomesticScheduledConsentResponse3DataStatus() {
        switch (this) {
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

    public OBWriteDomesticResponse3Data.StatusEnum toOBWriteDomesticResponse3DataStatus() {
        switch (this) {
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

    public OBWriteDomesticScheduledResponse3Data.StatusEnum toOBWriteDomesticScheduledResponse3DataStatus() {
        switch (this) {
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

    public OBWriteDomesticStandingOrderResponse4Data.StatusEnum toOBWriteDomesticStandingOrderResponse4DataStatus() {
        switch (this) {
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

    public OBWriteFileConsentResponse3Data.StatusEnum toOBWriteFileConsentResponse3DataStatus() {
        switch (this) {
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

    public OBExternalRequestStatus1Code toOBExternalRequestStatus1Code() {
        return OBExternalRequestStatus1Code.valueOf(name());
    }

}
