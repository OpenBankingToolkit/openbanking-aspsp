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

import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.fund.OBFundsConfirmation1DataInstructedAmount;

import static com.forgerock.openbanking.common.services.openbanking.converter.common.FRAmountConverter.toFRAmount;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class FRAmountConverterTest {

    static final String amount = "32.45";
    static final String currency = "GBP";

    @Test
    public void OBFundsConfirmation1DataInstructedAmount_toFRAmount() {
        // Given
        OBFundsConfirmation1DataInstructedAmount obFundsConfirmation1DataInstructedAmount =
                new OBFundsConfirmation1DataInstructedAmount();
        obFundsConfirmation1DataInstructedAmount.amount(amount)
                .currency(currency);

        // When
        FRAmount frAmount = toFRAmount(obFundsConfirmation1DataInstructedAmount);

        assertThat(frAmount.getAmount()).isEqualTo(amount);
        assertThat(frAmount.getCurrency()).isEqualTo(currency);

    }

    @Test
    public void toOBFundsConfirmation1DataInstructedAmount() {
        // Given
        FRAmount frAmount = new FRAmount();
        frAmount.setAmount(amount);
        frAmount.setCurrency(currency);

        // When
        OBFundsConfirmation1DataInstructedAmount obFundsConfirmation1DataInstructedAmount =
                FRAmountConverter.toOBFundsConfirmation1DataInstructedAmount(frAmount);

        // Then
        assertThat(obFundsConfirmation1DataInstructedAmount.getAmount()).isEqualTo(amount);
        assertThat(obFundsConfirmation1DataInstructedAmount.getCurrency()).isEqualTo(currency);
    }
}