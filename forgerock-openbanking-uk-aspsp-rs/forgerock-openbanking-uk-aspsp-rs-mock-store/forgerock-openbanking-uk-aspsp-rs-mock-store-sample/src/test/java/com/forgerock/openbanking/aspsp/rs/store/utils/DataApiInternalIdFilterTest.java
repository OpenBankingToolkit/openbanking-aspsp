/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.utils;

public class DataApiInternalIdFilterTest {
//    @Test
//    public void filterOff_noIdsRemoved() {
//        // Given
//        DataApiInternalIdFilter dataApiInternalIdFilterOff = new DataApiInternalIdFilter(true);
//        OBBeneficiary2 dataAccount = setupDataAccount();
//
//        // When
//        dataApiInternalIdFilterOff.apply(dataAccount);
//
//        // Then
//        assertThat(dataAccount.getTransactions().get(0).getTransactionId()).isEqualTo("12345");
//        assertThat(dataAccount.getBeneficiaries().get(0).getBeneficiaryId()).isEqualTo("12345");
//        assertThat(dataAccount.getStandingOrders().get(0).getStandingOrderId()).isEqualTo("12345");
//        assertThat(dataAccount.getStatements().get(0).getStatementId()).isEqualTo("12345");
//        assertThat(dataAccount.getScheduledPayments().get(0).getScheduledPaymentId()).isEqualTo("12345");
//        assertThat(dataAccount.getOffers().get(0).getOfferId()).isEqualTo("12345");
//        assertThat(dataAccount.getDirectDebits().get(0).getDirectDebitId()).isEqualTo("12345");
//    }
//
//    @Test
//    public void filterOn_idsFilteredOut() {
//        // Given
//        DataApiInternalIdFilter dataApiInternalIdFilterOn = new DataApiInternalIdFilter(false);
//        FRAccountData3 dataAccount = setupDataAccount();
//
//        // When
//        dataApiInternalIdFilterOn.apply(dataAccount);
//
//        // Then
//        assertThat(dataAccount.getTransactions().get(0).getTransactionId()).isNull();
//        assertThat(dataAccount.getBeneficiaries().get(0).getBeneficiaryId()).isNull();
//        assertThat(dataAccount.getStandingOrders().get(0).getStandingOrderId()).isNull();
//        assertThat(dataAccount.getStatements().get(0).getStatementId()).isNull();
//        assertThat(dataAccount.getScheduledPayments().get(0).getScheduledPaymentId()).isNull();
//        assertThat(dataAccount.getOffers().get(0).getOfferId()).isNull();
//        assertThat(dataAccount.getDirectDebits().get(0).getDirectDebitId()).isNull();
//    }
//
//    @Test
//    public void filterOff_nullSafe() {
//        // Given
//        DataApiInternalIdFilter dataApiInternalIdFilterOff = new DataApiInternalIdFilter(false);
//        FRAccountData3 dataAccount = new FRAccountData3();
//        dataAccount.setTransactions(null);
//        dataAccount.setBeneficiaries(null);
//        dataAccount.setStandingOrders(null);
//        dataAccount.setStatements(null);
//        dataAccount.setScheduledPayments(null);
//        dataAccount.setOffers(null);
//        dataAccount.setDirectDebits(null);
//
//        // When
//        dataApiInternalIdFilterOff.apply(dataAccount);
//
//        // Then
//        assertThat(dataAccount.getTransactions()).isNull();
//        assertThat(dataAccount.getBeneficiaries()).isNull();
//        assertThat(dataAccount.getStandingOrders()).isNull();
//        assertThat(dataAccount.getStatements()).isNull();
//        assertThat(dataAccount.getScheduledPayments()).isNull();
//        assertThat(dataAccount.getOffers()).isNull();
//        assertThat(dataAccount.getDirectDebits()).isNull();
//    }
//
//    private FRAccountData3 setupDataAccount() {
//        FRAccountData3 dataAccount = new FRAccountData3();
//        dataAccount.setAccount(new OBAccount2().accountId("555"));
//
//        dataAccount.setTransactions(Collections.singletonList(
//                new OBTransaction4()
//                        .transactionId("12345")
//        ));
//        dataAccount.setBeneficiaries(Collections.singletonList(
//                new OBBeneficiary2()
//                        .beneficiaryId("12345")
//        ));
//        dataAccount.setStandingOrders(Collections.singletonList(
//                new OBStandingOrder4()
//                        .standingOrderId("12345")
//        ));
//        dataAccount.setStatements(Collections.singletonList(
//                new OBStatement1()
//                        .statementId("12345")
//        ));
//        dataAccount.setScheduledPayments(Collections.singletonList(
//                new OBScheduledPayment1()
//                        .scheduledPaymentId("12345")
//        ));
//        dataAccount.setOffers(Collections.singletonList(
//                new OBOffer1()
//                        .offerId("12345")
//        ));
//        dataAccount.setDirectDebits(Collections.singletonList(
//                new OBDirectDebit1()
//                        .directDebitId("12345")
//        ));
//        return dataAccount;
//    }
}