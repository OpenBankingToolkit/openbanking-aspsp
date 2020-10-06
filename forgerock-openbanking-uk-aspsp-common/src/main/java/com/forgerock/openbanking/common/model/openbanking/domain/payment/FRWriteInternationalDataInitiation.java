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

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRFinancialAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRChargeBearerType;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDataInitiationCreditor;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRDataInitiationCreditorAgent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRExchangeRateInformation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRInstructionPriority;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRemittanceInformation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an equivalent object in the OB data model. It is stored within mongo (instead of the OB object), in order to make it easier to introduce new
 * versions of the Read/Write API.
 *
 * <p>
 * Note that this object is used across multiple versions of the Read/Write API, meaning that some values won't be populated. For this reason it is
 * a mutable {@link lombok.Data} rather than an immutable {@link lombok.Value} one.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRWriteInternationalDataInitiation {

    private String instructionIdentification;
    private String endToEndIdentification;
    private String localInstrument;
    private FRInstructionPriority instructionPriority;
    private String purpose;
    private String extendedPurpose;
    private FRChargeBearerType chargeBearer;
    private String currencyOfTransfer;
    private String destinationCountryCode;
    private FRAmount instructedAmount;
    private FRExchangeRateInformation exchangeRateInformation;
    private FRFinancialAccount debtorAccount;
    private FRDataInitiationCreditor creditor;
    private FRDataInitiationCreditorAgent creditorAgent;
    private FRFinancialAccount creditorAccount;
    private FRRemittanceInformation remittanceInformation;
    private FRSupplementaryData supplementaryData;
}
