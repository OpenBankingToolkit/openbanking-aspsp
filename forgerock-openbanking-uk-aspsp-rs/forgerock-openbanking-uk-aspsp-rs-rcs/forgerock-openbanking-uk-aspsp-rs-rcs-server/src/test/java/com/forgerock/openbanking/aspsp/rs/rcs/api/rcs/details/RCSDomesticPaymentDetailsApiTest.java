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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.AccountService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
import com.forgerock.openbanking.common.model.rcs.consentdetails.DomesticPaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.DomesticPaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.OBCashAccount5;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RCSDomesticPaymentDetailsApiTest {
    private static final String CLIENT_ID = "client123";

    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private DomesticPaymentService paymentService;
    @Spy
    private AccountService accountService = new AccountService();
    @InjectMocks
    private RCSDomesticPaymentDetailsApi api;

    @Test
    public void shouldReturnAllAccountsWhenNoDebtor() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {});
        FRDomesticConsent2 consent = JMockData.mock(FRDomesticConsent2.class);
        consent.getInitiation().setDebtorAccount(null);
        given(paymentService.getPayment(any())).willReturn(consent);
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(CLIENT_ID).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("abcd", accounts, "testuser", "c123", CLIENT_ID);

        // Then
        DomesticPaymentConsentDetails body = (DomesticPaymentConsentDetails) Objects.requireNonNull(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts()).isEqualTo(accounts);
    }

    @Test
    public void shouldReturnRequestedAccountWhenDebtor() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {
        });
        FRDomesticConsent2 consent = JMockData.mock(FRDomesticConsent2.class);
        OBCashAccount5 firstAccount = accounts.get(0).getAccount().getAccount().get(0);
        consent.getInitiation().getDebtorAccount().setIdentification(firstAccount.getIdentification());
        given(paymentService.getPayment(any())).willReturn(consent);
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(CLIENT_ID).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("abcd", accounts, "user1", "c123", CLIENT_ID);

        // Then
        DomesticPaymentConsentDetails body = (DomesticPaymentConsentDetails) Objects.requireNonNull(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts().size()).isEqualTo(1);
        assertThat(body.getAccounts()).containsExactly(accounts.get(0));
    }
}