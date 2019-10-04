/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.AccountService;
import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.forgerock.filepayment.v3_0.FRFilePayment;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRFileConsent2;
import com.forgerock.openbanking.common.model.openbanking.v3_1_1.account.FRAccount3;
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
import uk.org.openbanking.datamodel.account.OBAccount3;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.account.OBCashAccount5;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBFile2;
import uk.org.openbanking.datamodel.payment.OBWriteDataFileConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteFileConsent2;

import java.math.BigDecimal;
import java.util.*;

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

    private static final FRAccountWithBalance DEBTOR_ACCOUNT = new FRAccountWithBalance(FRAccount3.builder()
            .id("111")
            .account(new OBAccount3().account(Collections.singletonList(
                    new OBCashAccount5().identification("123")
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


    private OBFile2 getValidOBFile() {
        return new OBFile2()
                .fileHash("ksjkjsahd")
                .fileReference("UnitTest")
                .fileType("OBIE-Test")
                .controlSum(new BigDecimal("101.0"))
                .numberOfTransactions("19")
                ;
    }

    @Test
    public void validFilePayment_noAccountSpecified_createConsentDetailsWithAllAccounts() throws Exception{
        // Given
        List<FRAccountWithBalance> accounts = Collections.singletonList(DEBTOR_ACCOUNT);
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent2.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(getValidOBFile())))
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .payments(Arrays.asList(FRFilePayment.builder().instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().currency("GBP")).build()))
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
        List<FRAccountWithBalance> accounts = Collections.singletonList(DEBTOR_ACCOUNT);
        final OBFile2 validOBFileWithAccount = getValidOBFile()
                .debtorAccount(new OBCashAccount3().identification("123"));
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent2.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(validOBFileWithAccount)))
                        .pispId(PISP_ID)
                        .pispName(PISP_NAME)
                        .payments(Arrays.asList(FRFilePayment.builder().instructedAmount(new OBActiveOrHistoricCurrencyAndAmount().currency("GBP")).build()))
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
        List<FRAccountWithBalance> accounts = Collections.emptyList();

        final OBFile2 validOBFileWithAccount = getValidOBFile()
                .debtorAccount(new OBCashAccount3().identification("123"));
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent2.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(validOBFileWithAccount)))
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
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent2.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(getValidOBFile())))
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
        given(paymentService.getPayment(eq(CONSENT_ID))).willReturn(
                FRFileConsent2.builder()
                        .id(CONSENT_ID)
                        .writeFileConsent(new OBWriteFileConsent2().data(new OBWriteDataFileConsent2().initiation(getValidOBFile())))
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