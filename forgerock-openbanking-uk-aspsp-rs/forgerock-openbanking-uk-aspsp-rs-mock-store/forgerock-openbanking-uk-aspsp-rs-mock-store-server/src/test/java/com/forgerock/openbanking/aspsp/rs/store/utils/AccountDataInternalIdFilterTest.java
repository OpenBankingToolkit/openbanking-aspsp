/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.utils;

import org.junit.Test;
import uk.org.openbanking.datamodel.account.*;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDataInternalIdFilterTest {
    private AccountDataInternalIdFilter accountDataInternalIdFilter_showId = new AccountDataInternalIdFilter(true);
    private AccountDataInternalIdFilter accountDataInternalIdFilter_hideId = new AccountDataInternalIdFilter(false);

    @Test
    public void applyTransaction1() {
        OBTransaction1 data = new OBTransaction1().transactionId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getTransactionId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getTransactionId()).isNull();
    }

    @Test
    public void applyTransaction2() {
        OBTransaction2 data = new OBTransaction2().transactionId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getTransactionId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getTransactionId()).isNull();
    }

    @Test
    public void applyTransaction3() {
        OBTransaction3 data = new OBTransaction3().transactionId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getTransactionId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getTransactionId()).isNull();
    }

    @Test
    public void applyTransaction4() {
        OBTransaction4 data = new OBTransaction4().transactionId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getTransactionId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getTransactionId()).isNull();
    }

    @Test
    public void applyBeneficiary2() {
        OBBeneficiary2 data = new OBBeneficiary2().beneficiaryId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getBeneficiaryId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getBeneficiaryId()).isNull();
    }

    @Test
    public void applyDirectDebit1() {
        OBDirectDebit1 data = new OBDirectDebit1().directDebitId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getDirectDebitId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getDirectDebitId()).isNull();
    }

    @Test
    public void applyOffer1() {
        OBOffer1 data = new OBOffer1().offerId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getOfferId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getOfferId()).isNull();
    }

    @Test
    public void applyScheduledPayment1() {
        OBScheduledPayment1 data = new OBScheduledPayment1().scheduledPaymentId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getScheduledPaymentId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getScheduledPaymentId()).isNull();
    }

    @Test
    public void applyStandingOrder1() {
        OBStandingOrder1 data = new OBStandingOrder1().standingOrderId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getStandingOrderId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getStandingOrderId()).isNull();
    }

    @Test
    public void applyStandingOrder2() {
        OBStandingOrder2 data = new OBStandingOrder2().standingOrderId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getStandingOrderId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getStandingOrderId()).isNull();
    }

    @Test
    public void applyStandingOrder3() {
        OBStandingOrder3 data = new OBStandingOrder3().standingOrderId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getStandingOrderId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getStandingOrderId()).isNull();
    }

    @Test
    public void applyStandingOrder4() {
        OBStandingOrder4 data = new OBStandingOrder4().standingOrderId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getStandingOrderId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getStandingOrderId()).isNull();
    }

    @Test
    public void applyStatement1() {
        OBStatement1 data = new OBStatement1().statementId("12345");
        accountDataInternalIdFilter_showId.apply(data);
        assertThat(data.getStatementId()).isNotNull();

        accountDataInternalIdFilter_hideId.apply(data);
        assertThat(data.getStatementId()).isNull();
    }
}