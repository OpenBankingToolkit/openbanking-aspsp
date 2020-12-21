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
package com.forgerock.openbanking.aspsp.rs.store.utils;

import org.junit.Test;
import uk.org.openbanking.datamodel.account.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link AccountDataInternalIdFilter}.
 */
public class AccountDataInternalIdFilterTest {

    private static final String INTERNAL_ID = "12345";
    private static final String DOMESTIC_SCHEME = OBExternalAccountIdentification4Code.SORTCODEACCOUNTNUMBER.toString();
    private static final String INTERNATIONAL_SCHEME = OBExternalAccountIdentification4Code.IBAN.toString();
    private static final OBCashAccount3 DOMESTIC_ACCOUNT = new OBCashAccount3().schemeName(DOMESTIC_SCHEME);
    private static final OBCashAccount3 INTERNATIONAL_ACCOUNT = new OBCashAccount3().schemeName(INTERNATIONAL_SCHEME);

    private final OBDirectDebit1 directDebit = new OBDirectDebit1().directDebitId(INTERNAL_ID);
    private final OBOffer1 offer = new OBOffer1().offerId(INTERNAL_ID);
    private final OBReadProduct2DataProduct product = new OBReadProduct2DataProduct().productId(INTERNAL_ID);
    private final OBStatement1 statement = new OBStatement1().statementId(INTERNAL_ID);
    private final OBTransaction4 domesticTransaction = new OBTransaction4().transactionId(INTERNAL_ID).creditorAccount(DOMESTIC_ACCOUNT);
    private final OBTransaction4 internationalTransaction = new OBTransaction4().transactionId(INTERNAL_ID).creditorAccount(INTERNATIONAL_ACCOUNT);
    private final OBBeneficiary2 domesticBeneficiary = new OBBeneficiary2().beneficiaryId(INTERNAL_ID).creditorAccount(DOMESTIC_ACCOUNT);
    private final OBBeneficiary2 internationalBeneficiary = new OBBeneficiary2().beneficiaryId(INTERNAL_ID).creditorAccount(INTERNATIONAL_ACCOUNT);
    private final OBScheduledPayment1 domesticScheduledPayment = new OBScheduledPayment1().scheduledPaymentId(INTERNAL_ID).creditorAccount(DOMESTIC_ACCOUNT);
    private final OBScheduledPayment1 internationalScheduledPayment = new OBScheduledPayment1().scheduledPaymentId(INTERNAL_ID).creditorAccount(INTERNATIONAL_ACCOUNT);
    private final OBStandingOrder4 domesticStandingOrder = new OBStandingOrder4().standingOrderId(INTERNAL_ID).creditorAccount(DOMESTIC_ACCOUNT);
    private final OBStandingOrder4 internationalStandingOrder = new OBStandingOrder4().standingOrderId(INTERNAL_ID).creditorAccount(INTERNATIONAL_ACCOUNT);

    @Test
    public void shouldIncludeAllIdsGivenShowAllAccountDataInternalIdsIsTrue() {
        // Given
        AccountDataInternalIdFilter filter = filterWithAllIdsDisplayed();

        // When
        filter.apply(directDebit);
        filter.apply(offer);
        filter.apply(product);
        filter.apply(statement);
        filter.apply(domesticTransaction);
        filter.apply(internationalTransaction);
        filter.apply(domesticBeneficiary);
        filter.apply(internationalBeneficiary);
        filter.apply(domesticScheduledPayment);
        filter.apply(internationalScheduledPayment);
        filter.apply(domesticStandingOrder);
        filter.apply(internationalStandingOrder);

        // Then
        assertThat(directDebit.getDirectDebitId()).isNotNull();
        assertThat(offer.getOfferId()).isNotNull();
        assertThat(product.getProductId()).isNotNull();
        assertThat(statement.getStatementId()).isNotNull();
        assertThat(domesticTransaction.getTransactionId()).isNotNull();
        assertThat(internationalTransaction.getTransactionId()).isNotNull();
        assertThat(domesticBeneficiary.getBeneficiaryId()).isNotNull();
        assertThat(internationalBeneficiary.getBeneficiaryId()).isNotNull();
        assertThat(domesticScheduledPayment.getScheduledPaymentId()).isNotNull();
        assertThat(internationalScheduledPayment.getScheduledPaymentId()).isNotNull();
        assertThat(domesticStandingOrder.getStandingOrderId()).isNotNull();
        assertThat(internationalStandingOrder.getStandingOrderId()).isNotNull();
    }

    @Test
    public void shouldNotIncludeIdsGivenShowAllAccountDataInternalIdsIsFalse() {
        // Given
        AccountDataInternalIdFilter filter = filterWithAllIdsHidden();

        // When
        filter.apply(directDebit);
        filter.apply(offer);
        filter.apply(product);
        filter.apply(statement);
        filter.apply(domesticTransaction);
        filter.apply(internationalTransaction);
        filter.apply(domesticBeneficiary);
        filter.apply(internationalBeneficiary);
        filter.apply(domesticScheduledPayment);
        filter.apply(internationalScheduledPayment);
        filter.apply(domesticStandingOrder);
        filter.apply(internationalStandingOrder);

        // Then
        assertThat(directDebit.getDirectDebitId()).isNull();
        assertThat(offer.getOfferId()).isNull();
        assertThat(product.getProductId()).isNull();
        assertThat(statement.getStatementId()).isNull();
        assertThat(domesticTransaction.getTransactionId()).isNull();
        assertThat(internationalTransaction.getTransactionId()).isNull();
        assertThat(domesticBeneficiary.getBeneficiaryId()).isNull();
        assertThat(internationalBeneficiary.getBeneficiaryId()).isNull();
        assertThat(domesticScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(internationalScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(domesticStandingOrder.getStandingOrderId()).isNull();
        assertThat(internationalStandingOrder.getStandingOrderId()).isNull();
    }

    @Test
    public void shouldOnlyIncludeDirectDebitId() {
        // Given
        AccountDataInternalIdFilter filter = filterWithOnlyDirectDebitIdsDisplayed();

        // When
        filter.apply(directDebit);
        filter.apply(offer);
        filter.apply(product);
        filter.apply(statement);
        filter.apply(domesticTransaction);
        filter.apply(internationalTransaction);
        filter.apply(domesticBeneficiary);
        filter.apply(internationalBeneficiary);
        filter.apply(domesticScheduledPayment);
        filter.apply(internationalScheduledPayment);
        filter.apply(domesticStandingOrder);
        filter.apply(internationalStandingOrder);

        // Then
        assertThat(directDebit.getDirectDebitId()).isNotNull();
        assertThat(offer.getOfferId()).isNull();
        assertThat(product.getProductId()).isNull();
        assertThat(statement.getStatementId()).isNull();
        assertThat(domesticTransaction.getTransactionId()).isNull();
        assertThat(internationalTransaction.getTransactionId()).isNull();
        assertThat(domesticBeneficiary.getBeneficiaryId()).isNull();
        assertThat(internationalBeneficiary.getBeneficiaryId()).isNull();
        assertThat(domesticScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(internationalScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(domesticStandingOrder.getStandingOrderId()).isNull();
        assertThat(internationalStandingOrder.getStandingOrderId()).isNull();
    }

    @Test
    public void shouldIncludeAllExceptDirectDebitId() {
        // Given
        AccountDataInternalIdFilter filter = filterWithOnlyDirectDebitIdsHidden();

        // When
        filter.apply(directDebit);
        filter.apply(offer);
        filter.apply(product);
        filter.apply(statement);
        filter.apply(domesticTransaction);
        filter.apply(internationalTransaction);
        filter.apply(domesticBeneficiary);
        filter.apply(internationalBeneficiary);
        filter.apply(domesticScheduledPayment);
        filter.apply(internationalScheduledPayment);
        filter.apply(domesticStandingOrder);
        filter.apply(internationalStandingOrder);

        // Then
        assertThat(directDebit.getDirectDebitId()).isNull();
        assertThat(offer.getOfferId()).isNotNull();
        assertThat(product.getProductId()).isNotNull();
        assertThat(statement.getStatementId()).isNotNull();
        assertThat(domesticTransaction.getTransactionId()).isNotNull();
        assertThat(internationalTransaction.getTransactionId()).isNotNull();
        assertThat(domesticBeneficiary.getBeneficiaryId()).isNotNull();
        assertThat(internationalBeneficiary.getBeneficiaryId()).isNotNull();
        assertThat(domesticScheduledPayment.getScheduledPaymentId()).isNotNull();
        assertThat(internationalScheduledPayment.getScheduledPaymentId()).isNotNull();
        assertThat(domesticStandingOrder.getStandingOrderId()).isNotNull();
        assertThat(internationalStandingOrder.getStandingOrderId()).isNotNull();
    }

    @Test
    public void shouldOnlyIncludeDomesticTransactionId() {
        // Given
        AccountDataInternalIdFilter filter = filterWithOnlyDomesticTransactionIdsDisplayed();

        // When
        filter.apply(directDebit);
        filter.apply(offer);
        filter.apply(product);
        filter.apply(statement);
        filter.apply(domesticTransaction);
        filter.apply(internationalTransaction);
        filter.apply(domesticBeneficiary);
        filter.apply(internationalBeneficiary);
        filter.apply(domesticScheduledPayment);
        filter.apply(internationalScheduledPayment);
        filter.apply(domesticStandingOrder);
        filter.apply(internationalStandingOrder);

        // Then
        assertThat(directDebit.getDirectDebitId()).isNull();
        assertThat(offer.getOfferId()).isNull();
        assertThat(product.getProductId()).isNull();
        assertThat(statement.getStatementId()).isNull();
        assertThat(domesticTransaction.getTransactionId()).isNotNull();
        assertThat(internationalTransaction.getTransactionId()).isNull();
        assertThat(domesticBeneficiary.getBeneficiaryId()).isNull();
        assertThat(internationalBeneficiary.getBeneficiaryId()).isNull();
        assertThat(domesticScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(internationalScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(domesticStandingOrder.getStandingOrderId()).isNull();
        assertThat(internationalStandingOrder.getStandingOrderId()).isNull();
    }

    @Test
    public void shouldOnlyIncludeInternationalTransactionId() {
        // Given
        AccountDataInternalIdFilter filter = filterWithOnlyInternationalTransactionIdsDisplayed();

        // When
        filter.apply(directDebit);
        filter.apply(offer);
        filter.apply(product);
        filter.apply(statement);
        filter.apply(domesticTransaction);
        filter.apply(internationalTransaction);
        filter.apply(domesticBeneficiary);
        filter.apply(internationalBeneficiary);
        filter.apply(domesticScheduledPayment);
        filter.apply(internationalScheduledPayment);
        filter.apply(domesticStandingOrder);
        filter.apply(internationalStandingOrder);

        // Then
        assertThat(directDebit.getDirectDebitId()).isNull();
        assertThat(offer.getOfferId()).isNull();
        assertThat(product.getProductId()).isNull();
        assertThat(statement.getStatementId()).isNull();
        assertThat(domesticTransaction.getTransactionId()).isNull();
        assertThat(internationalTransaction.getTransactionId()).isNotNull();
        assertThat(domesticBeneficiary.getBeneficiaryId()).isNull();
        assertThat(internationalBeneficiary.getBeneficiaryId()).isNull();
        assertThat(domesticScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(internationalScheduledPayment.getScheduledPaymentId()).isNull();
        assertThat(domesticStandingOrder.getStandingOrderId()).isNull();
        assertThat(internationalStandingOrder.getStandingOrderId()).isNull();
    }

    private AccountDataInternalIdFilter filterWithAllIdsDisplayed() {
        return new AccountDataInternalIdFilter(
                true, //showAllAccountDataInternalIds - overrides others below
                false, //showDirectDebitIds
                false, //showOfferIds
                false, //showProductIds
                false, //showStatementIds
                false, //showDomesticTransactionIds
                false, //showInternationalTransactionIds
                false, //showDomesticBeneficiaryIds
                false, //showInternationalBeneficiaryIds
                false, //showDomesticScheduledPaymentIds
                false, //showInternationalScheduledPaymentIds
                false, //showDomesticStandingOrderIds
                false); //showInternationalStandingOrderIds
    }

    private AccountDataInternalIdFilter filterWithAllIdsHidden() {
        return new AccountDataInternalIdFilter(
                false, //showAllAccountDataInternalIds
                false, //showDirectDebitIds
                false, //showOfferIds
                false, //showProductIds
                false, //showStatementIds
                false, //showDomesticTransactionIds
                false, //showInternationalTransactionIds
                false, //showDomesticBeneficiaryIds
                false, //showInternationalBeneficiaryIds
                false, //showDomesticScheduledPaymentIds
                false, //showInternationalScheduledPaymentIds
                false, //showDomesticStandingOrderIds
                false); //showInternationalStandingOrderIds
    }

    private AccountDataInternalIdFilter filterWithOnlyDirectDebitIdsDisplayed() {
        return new AccountDataInternalIdFilter(
                false, //showAllAccountDataInternalIds
                true, //showDirectDebitIds
                false, //showOfferIds
                false, //showProductIds
                false, //showStatementIds
                false, //showDomesticTransactionIds
                false, //showInternationalTransactionIds
                false, //showDomesticBeneficiaryIds
                false, //showInternationalBeneficiaryIds
                false, //showDomesticScheduledPaymentIds
                false, //showInternationalScheduledPaymentIds
                false, //showDomesticStandingOrderIds
                false); //showInternationalStandingOrderIds
    }

    private AccountDataInternalIdFilter filterWithOnlyDirectDebitIdsHidden() {
        return new AccountDataInternalIdFilter(
                false, //showAllAccountDataInternalIds
                false, //showDirectDebitIds
                true, //showOfferIds
                true, //showProductIds
                true, //showStatementIds
                true, //showDomesticTransactionIds
                true, //showInternationalTransactionIds
                true, //showDomesticBeneficiaryIds
                true, //showInternationalBeneficiaryIds
                true, //showDomesticScheduledPaymentIds
                true, //showInternationalScheduledPaymentIds
                true, //showDomesticStandingOrderIds
                true); //showInternationalStandingOrderIds
    }

    private AccountDataInternalIdFilter filterWithOnlyDomesticTransactionIdsDisplayed() {
        return new AccountDataInternalIdFilter(
                false, //showAllAccountDataInternalIds
                false, //showDirectDebitIds
                false, //showOfferIds
                false, //showProductIds
                false, //showStatementIds
                true, //showDomesticTransactionIds
                false, //showInternationalTransactionIds
                false, //showDomesticBeneficiaryIds
                false, //showInternationalBeneficiaryIds
                false, //showDomesticScheduledPaymentIds
                false, //showInternationalScheduledPaymentIds
                false, //showDomesticStandingOrderIds
                false); //showInternationalStandingOrderIds
    }

    private AccountDataInternalIdFilter filterWithOnlyInternationalTransactionIdsDisplayed() {
        return new AccountDataInternalIdFilter(
                false, //showAllAccountDataInternalIds
                false, //showDirectDebitIds
                false, //showOfferIds
                false, //showProductIds
                false, //showStatementIds
                false, //showDomesticTransactionIds
                true, //showInternationalTransactionIds
                false, //showDomesticBeneficiaryIds
                false, //showInternationalBeneficiaryIds
                false, //showDomesticScheduledPaymentIds
                false, //showInternationalScheduledPaymentIds
                false, //showDomesticStandingOrderIds
                false); //showInternationalStandingOrderIds
    }
}