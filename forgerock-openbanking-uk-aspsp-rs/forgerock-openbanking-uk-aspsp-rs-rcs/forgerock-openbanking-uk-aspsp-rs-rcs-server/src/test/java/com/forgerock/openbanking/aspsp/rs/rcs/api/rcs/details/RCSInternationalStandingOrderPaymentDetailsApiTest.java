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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.AccountService;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccountIdentifier;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRInternationalStandingOrderConsent;
import com.forgerock.openbanking.common.model.rcs.consentdetails.InternationalStandingOrderPaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.InternationalStandingOrderService;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RCSInternationalStandingOrderPaymentDetailsApiTest {

    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private InternationalStandingOrderService paymentService;
    @Spy
    private AccountService accountService = new AccountService();
    @InjectMocks
    private RCSInternationalStandingOrderPaymentDetailsApi api;

    @Test
    public void shouldReturnAllAccountsWhenNoDebtor() throws OBErrorException {
        // Given
        List<AccountWithBalance> accounts = JMockData.mock(new TypeReference<List<AccountWithBalance>>() {
        });
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        consent.getInitiation().setDebtorAccount(null);
        given(paymentService.getPayment("")).willReturn(consent);
        String clientId = "clientId";
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(clientId).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("", accounts, "", "", clientId);

        // Then
        InternationalStandingOrderPaymentConsentDetails body = (InternationalStandingOrderPaymentConsentDetails) responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts()).isEqualTo(accounts);
    }

    @Test
    public void shouldReturnRequestedAccountWhenDebtor() throws OBErrorException {
        // Given
        List<AccountWithBalance> accounts = JMockData.mock(new TypeReference<>() {});
        FRInternationalStandingOrderConsent consent = JMockData.mock(FRInternationalStandingOrderConsent.class);
        FRAccountIdentifier firstAccount = accounts.get(0).getAccount().getAccounts().get(0);
        consent.getInitiation().getDebtorAccount().setIdentification(firstAccount.getIdentification());
        given(paymentService.getPayment("")).willReturn(consent);
        String clientId = "clientId";
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(clientId).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("", accounts, "", "", clientId);

        // Then
        InternationalStandingOrderPaymentConsentDetails body = (InternationalStandingOrderPaymentConsentDetails) responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts()).contains(accounts.get(0));
    }
}