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
package com.forgerock.openbanking.common.model.openbanking.data.payment;

import lombok.Builder;
import lombok.Data;
import uk.org.openbanking.datamodel.payment.*;

@Data
@Builder
public class FRWriteInternationalDataInitiation {

    private String instructionIdentification;
    private String endToEndIdentification;
    private String localInstrument;
    private OBWriteInternational3DataInitiation.InstructionPriorityEnum instructionPriority;
    private String purpose;
    private String extendedPurpose;
    private OBChargeBearerType1Code chargeBearer;
    private String currencyOfTransfer;
    private String destinationCountryCode;
    private OBWriteDomestic2DataInitiationInstructedAmount instructedAmount;
    private OBWriteInternational3DataInitiationExchangeRateInformation exchangeRateInformation;
    private OBWriteDomestic2DataInitiationDebtorAccount debtorAccount;
    private OBWriteInternational3DataInitiationCreditor creditor;
    private OBWriteInternational3DataInitiationCreditorAgent creditorAgent;
    private OBWriteDomestic2DataInitiationCreditorAccount creditorAccount;
    private OBWriteDomestic2DataInitiationRemittanceInformation remittanceInformation;
    private OBSupplementaryData1 supplementaryData;
}
