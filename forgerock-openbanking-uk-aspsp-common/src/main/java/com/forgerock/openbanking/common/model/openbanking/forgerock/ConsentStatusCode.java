/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.forgerock;

import uk.org.openbanking.datamodel.account.OBExternalRequestStatus1Code;
import uk.org.openbanking.datamodel.payment.OBExternalConsentStatus1Code;
import uk.org.openbanking.datamodel.payment.OBExternalConsentStatus2Code;
import uk.org.openbanking.datamodel.payment.OBExternalStatus1Code;
import uk.org.openbanking.datamodel.payment.OBTransactionIndividualStatus1Code;

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
    AWAITINGUPLOAD("AwaitingUpload")
    ;

    private final String value;

    ConsentStatusCode(String value) {
        this.value = value;
    }

    public static ConsentStatusCode fromValue(String value) {
        for(ConsentStatusCode consentStatusCode: ConsentStatusCode.values()) {
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

    public OBExternalRequestStatus1Code toOBExternalRequestStatus1Code() {
        return OBExternalRequestStatus1Code.valueOf(name());
    }

}
