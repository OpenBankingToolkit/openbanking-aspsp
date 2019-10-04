/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;


import com.forgerock.openbanking.common.model.openbanking.obie.payment.AccountSchemeName;
import com.forgerock.openbanking.common.model.openbanking.obie.payment.LocalInstrument;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.github.jsonzou.jmockdata.JMockData;
import org.junit.Test;
import uk.org.openbanking.datamodel.account.OBCashAccount3;
import uk.org.openbanking.datamodel.payment.OBDomestic2;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.payment.OBWriteDataDomesticConsent2;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BalanceTransferPaymentValidatorTest {

    @Test
    public void validate_disabled() throws Exception {
        // Given
        BalanceTransferPaymentValidator disabledBalanceTransferPaymentValidator = new BalanceTransferPaymentValidator(false);
        OBWriteDomesticConsent2 invalidConsent = new OBWriteDomesticConsent2()
                .data(new OBWriteDataDomesticConsent2()
                    .initiation(new OBDomestic2().creditorAccount(new OBCashAccount3()))
                );

        // When
        disabledBalanceTransferPaymentValidator.validate(invalidConsent);

        // Then
        // No exception because validation is disabled
    }

    @Test
    public void validate_enabled_notABalanceTransfer_ignore() throws Exception {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 bacsConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        bacsConsent.getData()
                .getInitiation()
                .localInstrument("UK.OBIE.BACS")
                .debtorAccount(null);

        // When
        balanceTransferPaymentValidator.validate(bacsConsent);

        // Then
        // No exception because this is not a balance transfer payment
    }

    @Test
    public void validate_enabled_validBalanceTransfer_noException() throws Exception {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument("UK.OBIE.BalanceTransfer");
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        balanceTransferPaymentValidator.validate(balanceTransferConsent);

        // Then
        // No exception because this is not a balance transfer payment
    }

    @Test
    public void validate_enabled_invalidBalanceTransfer_wrongCreditorAccountType_exception() {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName("UK.OBIE.SortCodeAccountNumber");  // Bank account not credit card - invalid
        balanceTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  balanceTransferPaymentValidator.validate(balanceTransferConsent))
        // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_CREDITOR_ACCOUNT.getMessage());
    }

    @Test
    public void validate_enabled_invalidBalanceTransfer_wrongDebtorAccountType_exception() {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getDebtorAccount().schemeName("UK.OBIE.SortCodeAccountNumber"); // Debtor bank account not credit card - invalid
        balanceTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  balanceTransferPaymentValidator.validate(balanceTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_DEBTOR_ACCOUNT.getMessage());
    }

    @Test
    public void validate_enabled_invalidBalanceTransfer_missingDebtorAccount_exception() {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().debtorAccount(null); // Missing debtor account
        balanceTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  balanceTransferPaymentValidator.validate(balanceTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_DEBTOR_ACCOUNT.getMessage());
    }

    @Test
    public void validate_enabled_invalidBalanceTransfer_missingReference_exception() {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getRemittanceInformation().reference(null);
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  balanceTransferPaymentValidator.validate(balanceTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_REMITTANCE_REFERENCE.getMessage());
    }

    @Test
    public void validate_enabled_invalidBalanceTransfer_notRemittance_exception() {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().remittanceInformation(null);
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  balanceTransferPaymentValidator.validate(balanceTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_REMITTANCE_REFERENCE.getMessage());
    }

    @Test
    public void validate_enabled_invalidBalanceTransfer_wrongContextCode() {
        // Given
        BalanceTransferPaymentValidator balanceTransferPaymentValidator = new BalanceTransferPaymentValidator(true);
        OBWriteDomesticConsent2 balanceTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        balanceTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        balanceTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        balanceTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.ECOMMERCESERVICES); // Wrong code

        // When
        assertThatThrownBy(() ->  balanceTransferPaymentValidator.validate(balanceTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_PAYMENT_CONTEXT_CODE.getMessage());
    }


}