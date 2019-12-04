/**
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
