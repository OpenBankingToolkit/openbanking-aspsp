/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.decisions;

import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.v2_0.account.FRAccount2;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRDomesticConsent2;
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
        FRDomesticConsent2 paymentConsent = new FRDomesticConsent2();

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
        FRDomesticConsent2 paymentConsent = new FRDomesticConsent2();

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
        FRDomesticConsent2 paymentConsent = new FRDomesticConsent2();

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
        FRDomesticConsent2 paymentConsent = new FRDomesticConsent2();

        // When
        paymentConsentDecisionUpdater.applyUpdate(USER_ID, null, false, p -> {}, paymentConsent);

        // Then
        assertThat(paymentConsent.getStatus()).isEqualTo(ConsentStatusCode.REJECTED);
    }
}