/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.AccountService;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRInternationalScheduledConsent2;
import com.forgerock.openbanking.commons.model.rcs.consentdetails.InternationalSchedulePaymentConsentDetails;
import com.forgerock.openbanking.commons.services.store.payment.InternationalScheduledPaymentService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
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
import uk.org.openbanking.datamodel.payment.OBExchangeRate2;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RCSInternationalScheduledPaymentDetailsApiTest {

    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private InternationalScheduledPaymentService paymentService;
    @Spy
    private AccountService accountService = new AccountService();
    @InjectMocks
    private RCSInternationalScheduledPaymentDetailsApi api;

    @Test
    public void shouldReturnAllAccountsWhenNoDebtor() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {
        });
        FRInternationalScheduledConsent2 consent = JMockData.mock(FRInternationalScheduledConsent2.class);
        consent.getInitiation().setDebtorAccount(null);
        given(paymentService.getPayment("")).willReturn(consent);
        String clientId = "clientId";
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(clientId).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("", accounts, "", "", clientId);

        // Then
        InternationalSchedulePaymentConsentDetails body = (InternationalSchedulePaymentConsentDetails) responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts()).isEqualTo(accounts);
    }

    @Test
    public void shouldReturnRequestedAccountWhenDebtor() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {
        });
        FRInternationalScheduledConsent2 consent = JMockData.mock(FRInternationalScheduledConsent2.class);
        OBCashAccount5 firstAccount = accounts.get(0).getAccount().getAccount().get(0);
        consent.getInitiation().getDebtorAccount().setIdentification(firstAccount.getIdentification());
        given(paymentService.getPayment("")).willReturn(consent);
        String clientId = "clientId";
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(clientId).build()));

        // When
        ResponseEntity responseEntity = api.consentDetails("", accounts, "", "", clientId);

        // Then
        InternationalSchedulePaymentConsentDetails body = (InternationalSchedulePaymentConsentDetails) responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getAccounts()).contains(accounts.get(0));
    }

    @Test
    public void shouldReturnExchangeRate() throws OBErrorException {
        // Given
        List<FRAccountWithBalance> accounts = JMockData.mock(new TypeReference<List<FRAccountWithBalance>>() {
        });
        FRInternationalScheduledConsent2 consent = JMockData.mock(FRInternationalScheduledConsent2.class);
        OBCashAccount5 firstAccount = accounts.get(0).getAccount().getAccount().get(0);
        consent.getInitiation().getDebtorAccount().setIdentification(firstAccount.getIdentification());
        given(paymentService.getPayment("")).willReturn(consent);
        String clientId = "clientId";
        given(tppStoreService.findById(consent.getPispId())).willReturn(Optional.of(Tpp.builder().clientId(clientId).build()));
        final OBExchangeRate2 exchangeRateInformation = consent.getCalculatedExchangeRate();

        // When
        ResponseEntity responseEntity = api.consentDetails("", accounts, "", "", clientId);

        // Then
        InternationalSchedulePaymentConsentDetails body = (InternationalSchedulePaymentConsentDetails) responseEntity.getBody();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body.getRate()).isEqualTo(new OBExchangeRate2()
                .exchangeRate(exchangeRateInformation.getExchangeRate())
                .rateType(exchangeRateInformation.getRateType())
                .contractIdentification(exchangeRateInformation.getContractIdentification())
                .unitCurrency(exchangeRateInformation.getUnitCurrency()));
    }
}