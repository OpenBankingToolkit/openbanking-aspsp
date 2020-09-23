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
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAccount;
import com.forgerock.openbanking.common.model.openbanking.domain.common.FRAmount;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFileConsent;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFileConsentData;
import com.forgerock.openbanking.common.model.openbanking.domain.payment.FRWriteFileDataInitiation;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.v3_1_3.FRAccount4;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRFileConsent;
import com.forgerock.openbanking.common.model.rcs.consentdetails.FilePaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.FilePaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.OBAccount3Account;
import uk.org.openbanking.datamodel.account.OBAccount6;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RCSFilePaymentDetailsApiTest {

    private static final String CLIENT_ID = "MyApp12345";
    private static final String USERNAME = "user.1";
    private static final String PISP_ID = "PISP1";
    private static final String PISP_NAME = "MyApp";
    private static final String CONSENT_ID = "PFC_123";

    private static final AccountWithBalance DEBTOR_ACCOUNT = new AccountWithBalance(FRAccount4.builder()
            .id("111")
            .account(new OBAccount6().account(singletonList(
                    new OBAccount3Account().identification("123")
            )))
            .build(), Collections.EMPTY_LIST);

    @InjectMocks
    private RCSFilePaymentDetailsApi rcsFilePaymentDetailsApi;

    @Mock
    private RCSErrorService rcsErrorService;
    @Mock
    private FilePaymentService paymentService;
    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private AccountService accountService;

    @Before
    public void setup() throws Exception{
    }

    private FRWriteFileDataInitiation.FRWriteFileDataInitiationBuilder getValidOBFile() {
        return FRWriteFileDataInitiation.builder()
                .fileHash("ksjkjsahd")
                .fileReference("UnitTest")
                .fileType("OBIE-Test")
                .controlSum(new BigDecimal("101.0"))
                .numberOfTransactions("19");
    }

    @Test
    public void validFilePayment_noAccountSpecified_createConsentDetailsWithAllAccounts() throws Exception{
        // Given
        List<AccountWithBalance> accounts = singletonList(DEBTOR_ACCOUNT);
        FRWriteFileConsentData data = FRWriteFileConsentData.builder().initiation(getValidOBFile().build()).build();
        FRWriteFileConsent writeFileConsent = FRWriteFileConsent.builder().data(data).build();
        FRAmount amount = FRAmount.builder().currency("GBP").build();
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(writeFileConsent)
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .payments(Arrays.asList(FRFilePayment.builder().instructedAmount(amount).build()))
                        .build()
        );
        givenTppExists();

        // When
        ResponseEntity response = rcsFilePaymentDetailsApi.consentDetails("", accounts, USERNAME, CONSENT_ID, CLIENT_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        FilePaymentConsentDetails consentDetails = (FilePaymentConsentDetails) Objects.requireNonNull(response.getBody());
        assertThat(consentDetails.getAccounts()).isEqualTo(accounts);
        assertThat(consentDetails.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(consentDetails.getNumberOfTransactions()).isEqualTo("19");
        assertThat(consentDetails.getMerchantName()).isEqualTo(PISP_NAME);

        verify(paymentService, times(1)).getPayment(any());
    }

    @Test
    public void validFilePayment_accountSpecifiedAndFound_createConsentDetailsWithAllAccounts() throws Exception{
        // Given
        List<AccountWithBalance> accounts = singletonList(DEBTOR_ACCOUNT);
        FRWriteFileDataInitiation validOBFileWithAccount = getValidOBFile()
                .debtorAccount(FRAccount.builder().identification("123").build())
                .build();
        FRWriteFileConsent frWriteFileConsent = FRWriteFileConsent.builder()
                .data(FRWriteFileConsentData.builder().initiation(validOBFileWithAccount).build())
                .build();
        FRAmount amount = FRAmount.builder().currency("GBP").build();
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(frWriteFileConsent)
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .payments(singletonList(FRFilePayment.builder().instructedAmount(amount).build()))
                        .build()
        );
        givenTppExists();
        given(accountService.findAccountByIdentification(any(), any())).willReturn(Optional.of(DEBTOR_ACCOUNT));

        // When
        ResponseEntity response = rcsFilePaymentDetailsApi.consentDetails("", accounts, USERNAME, CONSENT_ID, CLIENT_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        FilePaymentConsentDetails consentDetails = (FilePaymentConsentDetails) Objects.requireNonNull(response.getBody());
        assertThat(consentDetails.getAccounts()).isEqualTo(accounts);
        assertThat(consentDetails.getClientId()).isEqualTo(CLIENT_ID);
        assertThat(consentDetails.getNumberOfTransactions()).isEqualTo("19");
        assertThat(consentDetails.getMerchantName()).isEqualTo(PISP_NAME);

        verify(paymentService, times(1)).getPayment(any());
    }

    @Test
    public void validFilePayment_accountSpecifiedButNotFound_getErrorRedirect() throws Exception{
        // Given
        List<AccountWithBalance> accounts = Collections.emptyList();
        FRWriteFileDataInitiation validOBFileWithAccount = getValidOBFile()
                .debtorAccount(FRAccount.builder().identification("123").build())
                .build();
        FRWriteFileConsent writeFileConsent = FRWriteFileConsent.builder()
                .data(FRWriteFileConsentData.builder().initiation(validOBFileWithAccount).build())
                .build();
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(writeFileConsent)
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .build()
        );
        givenTppExists();
        given(accountService.findAccountByIdentification(any(), any())).willReturn(Optional.empty());
        given(rcsErrorService.invalidConsentError(any(), any(), any())).willReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).build());

        // When
        ResponseEntity response = rcsFilePaymentDetailsApi.consentDetails("", accounts, USERNAME, CONSENT_ID, CLIENT_ID);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void validFilePayment_tppNotFound() throws Exception {
        // Given
        FRWriteFileDataInitiation validOBFile = getValidOBFile().build();
        FRWriteFileConsentData writeFileConsentData = FRWriteFileConsentData.builder()
                .initiation(validOBFile)
                .build();
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(FRWriteFileConsent.builder().data(writeFileConsentData).build())
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .build()
        );
        given(tppStoreService.findById(any())).willReturn(Optional.empty());

        // When
        assertThatThrownBy(() ->
                rcsFilePaymentDetailsApi.consentDetails("", Collections.emptyList(), USERNAME, CONSENT_ID, CLIENT_ID)
        // Then
        ).isInstanceOf(OBErrorException.class)
         .hasMessage("The TPP 'PISP1' that created this intent id 'PFC_123' doesn't exist anymore.");
    }

    @Test
    public void validFilePayment_wrongPisp() throws Exception {
        // Given
        FRWriteFileDataInitiation validOBFile = getValidOBFile().build();
        FRWriteFileConsentData writeFileConsentData = FRWriteFileConsentData.builder()
                .initiation(validOBFile)
                .build();
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(FRWriteFileConsent.builder().data(writeFileConsentData).build())
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .build()
        );
        givenTppExists();

        // When
        assertThatThrownBy(() ->
                        rcsFilePaymentDetailsApi.consentDetails("", Collections.emptyList(), USERNAME, CONSENT_ID,
                                "Wrong Client Id")
                // Then
        ).isInstanceOf(OBErrorException.class)
                .hasMessage("The PISP '"+CLIENT_ID+"' created the payment request '"+CONSENT_ID+"' but it's PISP 'Wrong Client Id' that is requesting consent for it.");
    }

    private void givenTppExists() {
        Tpp tpp = new Tpp();
        tpp.setClientId(CLIENT_ID);
        given(tppStoreService.findById(eq(PISP_ID))).willReturn(Optional.of(tpp));
    }
}