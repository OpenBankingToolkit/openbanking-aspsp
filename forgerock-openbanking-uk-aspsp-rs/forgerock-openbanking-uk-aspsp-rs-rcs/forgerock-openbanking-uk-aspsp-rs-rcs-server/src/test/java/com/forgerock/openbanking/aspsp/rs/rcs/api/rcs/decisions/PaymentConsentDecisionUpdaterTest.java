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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.services.store.account.AccountStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentConsentDecisionUpdaterTest {

    private static final String ACCOUNT_ID = "acc123";
    private static final String USER_ID = "user1";

    @Mock
    private AccountStoreService accountStoreService;

    @InjectMocks
    private PaymentConsentDecisionUpdater paymentConsentDecisionUpdater;


    @Test
    public void paymentDecisionAllowed_applyUpdateToConsent() throws Exception {
        // Given
        FRAccount2 account = new FRAccount2();
        account.id = ACCOUNT_ID;
        when(accountStoreService.get(USER_ID)).thenReturn(Collections.singletonList(account));
        FRDomesticConsent paymentConsent = new FRDomesticConsent();

        // When
        paymentConsentDecisionUpdater.applyUpdate(USER_ID, ACCOUNT_ID, true, p -> {}, paymentConsent);

        // Then
        assertThat(paymentConsent.getStatus()).isEqualTo(ConsentStatusCode.AUTHORISED);
        assertThat(paymentConsent.getAccountId()).isEqualTo(ACCOUNT_ID);
    }

    @Test
    public void paymentDecisionAllowed_butUserDoesNotOwnAccount_rejectWithException() {
        // Given
        FRAccount2 account = new FRAccount2();
        account.id = "differentId";
        when(accountStoreService.get(USER_ID)).thenReturn(Collections.singletonList(account));
        FRDomesticConsent paymentConsent = new FRDomesticConsent();

        // When
        assertThatThrownBy(() ->
            paymentConsentDecisionUpdater.applyUpdate(USER_ID, ACCOUNT_ID, true, p -> {}, paymentConsent))
        // Then
        .isExactlyInstanceOf(OBErrorException.class)
        .hasMessage("The PSU user1 is trying to share an account 'acc123' he doesn't own. List of his accounts '[FRAccount2(id=differentId, userID=null, account=null, latestStatementId=null, created=null, updated=null)]'");

    }

    @Test
    public void paymentDecisionAllowed_missingAccountId_rejectWithException() {
        // Given
        FRDomesticConsent paymentConsent = new FRDomesticConsent();

        // When
        assertThatThrownBy(() ->
                paymentConsentDecisionUpdater.applyUpdate(USER_ID, null, true, p -> {
                }, paymentConsent))
                // Then
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Missing account id");
    }

    @Test
    public void paymentDecisionDeclined_applyRejectedUpdateToConsent() throws Exception {
        // Given
        FRDomesticConsent paymentConsent = new FRDomesticConsent();

        // When
        paymentConsentDecisionUpdater.applyUpdate(USER_ID, null, false, p -> {}, paymentConsent);

        // Then
        assertThat(paymentConsent.getStatus()).isEqualTo(ConsentStatusCode.REJECTED);
    }
}