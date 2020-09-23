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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteDomesticDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRSupplementaryData;

import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountTestDataFactory.aValidFRAccount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAccountTestDataFactory.aValidFRAccount2;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRAmountTestDataFactory.aValidFRAmount;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRPostalAddressTestDataFactory.aValidFRPostalAddress;
import static com.forgerock.openbanking.aspsp.rs.store.api.openbanking.testsupport.domain.FRRemittanceInformationTestDataFactory.aValidFRRemittanceInformation;

public class FRWriteDomesticDataInitiationTestDataFactory {

    public static FRWriteDomesticDataInitiation.FRWriteDomesticDataInitiationBuilder aValidFRWriteDomesticDataInitiation() {
        return FRWriteDomesticDataInitiation.builder()
                .instructionIdentification("12345678")
                .endToEndIdentification("12345")
                .localInstrument("UK.OBIE.CHAPS")
                .instructedAmount(aValidFRAmount())
                .debtorAccount(aValidFRAccount())
                .creditorAccount(aValidFRAccount2())
                .creditorPostalAddress(aValidFRPostalAddress())
                .remittanceInformation(aValidFRRemittanceInformation())
                .supplementaryData(FRSupplementaryData.builder().build());
    }
}
