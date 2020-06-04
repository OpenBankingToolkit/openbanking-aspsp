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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import org.joda.time.DateTime;
import org.junit.Test;
import uk.org.openbanking.datamodel.payment.OBAuthorisation1;
import uk.org.openbanking.datamodel.payment.OBExternalAuthorisation1Code;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent3DataAuthorisation;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent3DataAuthorisation.AuthorisationTypeEnum;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBAuthorisation1;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBConsentAuthorisationConverter.toOBWriteDomesticConsent3DataAuthorisation;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link OBConsentAuthorisationConverter}.
 */
public class OBConsentAuthorisationConverterTest {

    @Test
    public void shouldConvertToOBWriteDomesticConsent3DataAuthorisation() {
        // Given
        DateTime completionDateTime = DateTime.now();
        OBAuthorisation1 obAuthorisation1 = new OBAuthorisation1();
        obAuthorisation1.setAuthorisationType(OBExternalAuthorisation1Code.ANY);
        obAuthorisation1.setCompletionDateTime(completionDateTime);

        // When
        OBWriteDomesticConsent3DataAuthorisation obWriteDomesticConsent3DataAuthorisation = toOBWriteDomesticConsent3DataAuthorisation(obAuthorisation1);

        // Then
        assertThat(obWriteDomesticConsent3DataAuthorisation.getAuthorisationType()).isEqualTo(AuthorisationTypeEnum.ANY);
        assertThat(obWriteDomesticConsent3DataAuthorisation.getCompletionDateTime()).isEqualTo(completionDateTime);
    }

    @Test
    public void shouldConvertToOBAuthorisation1() {
        // Given
        DateTime completionDateTime = DateTime.now();
        OBWriteDomesticConsent3DataAuthorisation obWriteDomesticConsent3DataAuthorisation = new OBWriteDomesticConsent3DataAuthorisation();
        obWriteDomesticConsent3DataAuthorisation.setAuthorisationType(AuthorisationTypeEnum.ANY);
        obWriteDomesticConsent3DataAuthorisation.setCompletionDateTime(completionDateTime);

        // When
        OBAuthorisation1 obAuthorisation1 = toOBAuthorisation1(obWriteDomesticConsent3DataAuthorisation);

        // Then
        assertThat(obAuthorisation1.getAuthorisationType()).isEqualTo(OBExternalAuthorisation1Code.ANY);
        assertThat(obAuthorisation1.getCompletionDateTime()).isEqualTo(completionDateTime);
    }

}