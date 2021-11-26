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
package com.forgerock.openbanking.common.services.openbanking.converter.common;

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmationConsent1DataDebtorAccount;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toOBFundsConfirmationConsent1DataDebtorAccount;
import static org.assertj.core.api.Assertions.assertThat;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAccountIdentifierConverter.toFRAccountIdentifier;


@RunWith(MockitoJUnitRunner.class)
public class FRAccountIdentifierConverterTest {


    private final String schemeName = "schemeName";
    private final String identification = "32436tgf";
    private final String name = "fred titmus";
    private final String secondaryIdentification = "34326dljf";


    @Test
    public void OBFundsConfirmationConsent1DataDebtorAccount_toFRAccountIdentifier() {
        // Given
        OBFundsConfirmationConsent1DataDebtorAccount obFundsConfirmationConsent1DataDebtorAccount =
                new OBFundsConfirmationConsent1DataDebtorAccount();
        obFundsConfirmationConsent1DataDebtorAccount.schemeName(this.schemeName)
                .identification(this.identification)
                .name(this.name)
                .secondaryIdentification(this.secondaryIdentification);

        // When
        FRAccountIdentifier accountIdentifier = toFRAccountIdentifier(obFundsConfirmationConsent1DataDebtorAccount);

        // Then
        assertThat(accountIdentifier.getSchemeName()).isEqualTo(this.schemeName);
        assertThat(accountIdentifier.getIdentification()).isEqualTo(this.identification);
        assertThat(accountIdentifier.getName()).isEqualTo(this.name);
        assertThat(accountIdentifier.getSecondaryIdentification()).isEqualTo(this.secondaryIdentification);
    }

    @Test
    public void FRAccountIdentifier_toOBFundsConfirmationConsent1DataDebtorAccount() {
        // Given
        FRAccountIdentifier accountIdentifier = new FRAccountIdentifier();
        accountIdentifier.setName(this.name);
        accountIdentifier.setIdentification(this.identification);
        accountIdentifier.setSchemeName(this.schemeName);
        accountIdentifier.setSecondaryIdentification(this.secondaryIdentification);

        // When
        OBFundsConfirmationConsent1DataDebtorAccount account =
                toOBFundsConfirmationConsent1DataDebtorAccount(accountIdentifier);

        // Then
        assertThat(account.getName()).isEqualTo(this.name);
        assertThat(account.getIdentification()).isEqualTo(this.identification);
        assertThat(account.getSchemeName()).isEqualTo(this.schemeName);
        assertThat(account.getSecondaryIdentification()).isEqualTo(this.secondaryIdentification);
    }
}