/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.rcs.consentdetails;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.forgerock.openbanking.common.model.openbanking.IntentType;

/**
 * Models the consent data that is used for
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        // Required to deserialise the RCS ConsentDetails response into the correct types in RS-API
        @JsonSubTypes.Type(value = DomesticPaymentConsentDetails.class, name = "domesticPaymentConsentDetails"),
        @JsonSubTypes.Type(value = DomesticSchedulePaymentConsentDetails.class, name = "domesticSchedulePaymentConsentDetails"),
        @JsonSubTypes.Type(value = DomesticStandingOrderPaymentConsentDetails.class, name = "domesticStandingOrderPaymentConsentDetails"),
        @JsonSubTypes.Type(value = InternationalPaymentConsentDetails.class, name = "internationalPaymentConsentDetails"),
        @JsonSubTypes.Type(value = InternationalSchedulePaymentConsentDetails.class, name = "internationalSchedulePaymentConsentDetails"),
        @JsonSubTypes.Type(value = InternationalStandingOrderPaymentConsentDetails.class, name = "internationalStandingOrderPaymentConsentDetails"),
        @JsonSubTypes.Type(value = SinglePaymentConsentDetails.class, name = "singlePaymentConsentDetails"),
        @JsonSubTypes.Type(value = FilePaymentConsentDetails.class, name = "filePaymentConsentDetails")
})
public abstract class ConsentDetails {

    public abstract IntentType getIntentType();

    public String getDecisionAPIUri() {
        return "/api/rcs/consent/decision/";
    }
}
