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
package com.forgerock.openbanking.common.model.openbanking.domain.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialCreditor;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRChargeBearerType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;

/**
 * Represents the values that are common across the various international payment "initiation" objects.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @Type(value = FRWriteInternationalDataInitiation.class, name = "FRWriteInternationalDataInitiation"),
        @Type(value = FRWriteInternationalScheduledDataInitiation.class, name = "FRWriteInternationalScheduledDataInitiation"),
        @Type(value = FRWriteInternationalStandingOrderDataInitiation.class, name = "FRWriteInternationalStandingOrderDataInitiation")
})
public interface FRInternationalDataInitiation {

    String getPurpose();

    String getExtendedPurpose();

    FRChargeBearerType getChargeBearer();

    String getCurrencyOfTransfer();

    String getDestinationCountryCode();

    FRAmount getInstructedAmount();

    FRAccountIdentifier getDebtorAccount();

    FRFinancialCreditor getCreditor();

    FRFinancialAgent getCreditorAgent();

    FRAccountIdentifier getCreditorAccount();

    FRSupplementaryData getSupplementaryData();
}
