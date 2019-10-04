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

public class PaymPaymentValidatorTest {

    @Test
    public void validate_disabled() throws Exception {
        // Given
        PaymPaymentValidator disabledValidator = new PaymPaymentValidator(false);
        OBWriteDomesticConsent2 invalidConsent = new OBWriteDomesticConsent2()
                .data(new OBWriteDataDomesticConsent2()
                        .initiation(new OBDomestic2().creditorAccount(new OBCashAccount3()))
                );

        // When
        disabledValidator.validate(invalidConsent);

        // Then
        // No exception because validation is disabled
    }

    @Test
    public void validate_enabled_notAPaym_ignore() throws Exception {
        // Given
        PaymPaymentValidator paymPaymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 bacsConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        bacsConsent.getData()
                .getInitiation()
                .localInstrument("UK.OBIE.BACS")
                .debtorAccount(null);
        bacsConsent.getData().getInitiation().getCreditorAccount().schemeName("SortCodeAccountNumber");

        // When
        paymPaymentValidator.validate(bacsConsent);

        // Then
        // No exception because this is not a Paym payment
    }

    @Test
    public void validate_enabled_validPaymScheme_blankLocalInstrument_noException() throws Exception {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("");
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue());
        paymConsent.getData().getInitiation().getCreditorAccount().identification("07777777777");
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        paymentValidator.validate(paymConsent);

        // Then
        // No exception because this is a valid Paym Consent payment
    }

    @Test
    public void validate_enabled_validPaymScheme_paymLocalInstrument_noException() throws Exception {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("paym");
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue());
        paymConsent.getData().getInitiation().getCreditorAccount().identification("07777777777");
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        paymentValidator.validate(paymConsent);

        // Then
        // No exception because this is a valid Paym Consent payment
    }

    @Test
    public void validate_enabled_invalidPaym_wrongCreditorAccountScheme_exception() {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("paym"); // Local instrument identifies payment as paym...
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue()); // ...but creditor is not a paym account
        paymConsent.getData().getInitiation().getCreditorAccount().identification("07777777777");
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  paymentValidator.validate(paymConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_PAYM_WRONG_CREDITOR_ACCOUNT_SCHEME.getMessage());
    }

    @Test
    public void validate_enabled_invalidPaym_emptyCreditorAccountIdentification_exception() {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("paym");
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue());
        paymConsent.getData().getInitiation().getCreditorAccount().identification(""); // No identification mobile number submitted
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  paymentValidator.validate(paymConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_PAYM_MISSING_CUSTOMER_IDENTIFICATION.getMessage());
    }

    @Test
    public void validate_enabled_invalidPaym_whitespaceCreditorAccountIdentification_exception() {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("paym");
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue());
        paymConsent.getData().getInitiation().getCreditorAccount().identification("     "); // Whitespace identification mobile number submitted
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  paymentValidator.validate(paymConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_PAYM_MISSING_CUSTOMER_IDENTIFICATION.getMessage());
    }

    @Test
    public void validate_enabled_invalidPaym_wrongLocalInstrument_exception() {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue()); // Creditor is a paym account...
        paymConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_BalanceTransfer.getNamespacedValue()); // ...but the local instrument is for a balance transfer
        paymConsent.getData().getInitiation().getCreditorAccount().identification("07777777777");
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  paymentValidator.validate(paymConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_PAYM_INVALID_LOCAL_INSTRUMENT.getMessage());
    }

    @Test
    public void validate_enabled_invalidPaym_wrongPaymentContextCode_exception() {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("paym");
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue());
        paymConsent.getData().getInitiation().getCreditorAccount().identification("07777777777");
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.ECOMMERCESERVICES); // Must be party to party for paym

        // When
        assertThatThrownBy(() ->  paymentValidator.validate(paymConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_PAYM_INVALID_PAYMENT_CONTEXT_CODE.getMessage());
    }

    @Test
    public void validate_enabled_invalidPaym_notValidPhoneNumber_exception() {
        // Given
        PaymPaymentValidator paymentValidator = new PaymPaymentValidator(true);
        OBWriteDomesticConsent2 paymConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        paymConsent.getData().getInitiation().localInstrument("paym");
        paymConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAYM.getNamespacedValue());
        paymConsent.getData().getInitiation().getCreditorAccount().identification("+547777777777"); // Not uk number
        paymConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  paymentValidator.validate(paymConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_PAYM_CUSTOMER_IDENTIFICATION_NOT_A_UK_MOBILE_NUMBER.getMessage());
    }

}