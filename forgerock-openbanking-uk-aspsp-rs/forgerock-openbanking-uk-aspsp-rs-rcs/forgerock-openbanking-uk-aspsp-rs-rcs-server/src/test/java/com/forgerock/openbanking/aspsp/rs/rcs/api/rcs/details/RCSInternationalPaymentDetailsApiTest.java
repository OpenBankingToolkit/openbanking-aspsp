/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.AccountService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRInternationalConsent2;
import com.forgerock.openbanking.common.model.rcs.consentdetails.InternationalPaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.InternationalPaymentService;
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
public class RCSInternationalPaymentDetailsApiTest {
    private static final String CLIENT_ID = "client123";

    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private InternationalPaymentService paymentService;
    @Spy
    private AccountService accountService = new AccountService();
    @InjectMocks
    private RCSInternationalPaymentDetailsApi api;

    @Test
    public void shouldReturnAllAccountsWhenNoDebtor() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {});
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        consent.getInitiation().setDebtorAccount(null);
        given(paymentService.getPayment(any())).willReturn(consent);
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(CLIENT_ID).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("abcd", accounts, "testuser", "c123", CLIENT_ID);

        // Then
        InternationalPaymentConsentDetails body = (InternationalPaymentConsentDetails) Objects.requireNonNull(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts()).isEqualTo(accounts);
    }

    @Test
    public void shouldReturnRequestedAccountWhenDebtor() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {
        });
        FRInternationalConsent2 consent = JMockData.mock(FRInternationalConsent2.class);
        OBCashAccount5 firstAccount = accounts.get(0).getAccount().getAccount().get(0);
        consent.getInitiation().getDebtorAccount().setIdentification(firstAccount.getIdentification());
        given(paymentService.getPayment(any())).willReturn(consent);
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(CLIENT_ID).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("abcd", accounts, "user1", "c123", CLIENT_ID);

        // Then
        InternationalPaymentConsentDetails body = (InternationalPaymentConsentDetails) Objects.requireNonNull(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts().size()).isEqualTo(1);
        assertThat(body.getAccounts()).containsExactly(accounts.get(0));
    }
}