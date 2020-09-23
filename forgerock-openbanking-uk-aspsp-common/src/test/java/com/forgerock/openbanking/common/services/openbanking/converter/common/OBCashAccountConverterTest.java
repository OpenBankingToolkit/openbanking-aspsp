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

import org.junit.Test;
import uk.org.openbanking.datamodel.account.OBCashAccount1;
import uk.org.openbanking.datamodel.account.OBCashAccount5;
import uk.org.openbanking.datamodel.payment.OBExternalAccountIdentification2Code;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link OBCashAccountConverter}.
 */
public class OBCashAccountConverterTest {

    @Test
    public void shouldConvertToObCashAccount1GivenValidOBCashAccount5() {
        // Given
        OBCashAccount5 obCashAccount5 = new OBCashAccount5()
                .schemeName(OBExternalAccountIdentification2Code.SortCodeAccountNumber.getReference())
                .identification("identification")
                .name("name")
                .secondaryIdentification("secondaryIdentification");

        // When
        OBCashAccount1 obCashAccount1 = OBCashAccountConverter.toOBCashAccount1(obCashAccount5);

        // Then
        assertThat(obCashAccount1.getSchemeName()).isEqualTo(OBExternalAccountIdentification2Code.SortCodeAccountNumber);
        assertThat(obCashAccount1.getIdentification()).isEqualTo("identification");
        assertThat(obCashAccount1.getName()).isEqualTo("name");
        assertThat(obCashAccount1.getSecondaryIdentification()).isEqualTo("secondaryIdentification");
    }

    @Test
    public void shouldConvertToObCashAccount1GivenInvalidScheme() {
        // Given
        OBCashAccount5 obCashAccount5 = new OBCashAccount5()
                .schemeName("invalid scheme")
                .identification("identification")
                .name("name")
                .secondaryIdentification("secondaryIdentification");

        // When
        OBCashAccount1 obCashAccount1 = OBCashAccountConverter.toOBCashAccount1(obCashAccount5);

        // Then
        assertThat(obCashAccount1.getSchemeName()).isNull();
        assertThat(obCashAccount1.getIdentification()).isEqualTo("identification");
        assertThat(obCashAccount1.getName()).isEqualTo("name");
        assertThat(obCashAccount1.getSecondaryIdentification()).isEqualTo("secondaryIdentification");
    }

}
