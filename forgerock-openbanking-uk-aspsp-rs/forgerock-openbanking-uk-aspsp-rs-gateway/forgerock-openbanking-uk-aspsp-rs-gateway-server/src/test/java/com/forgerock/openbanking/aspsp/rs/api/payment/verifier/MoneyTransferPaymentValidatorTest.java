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

public class MoneyTransferPaymentValidatorTest {
    @Test
    public void validate_disabled() throws Exception {
        // Given
        MoneyTransferPaymentValidator disabledMoneyTransferPaymentValidator = new MoneyTransferPaymentValidator(false);
        OBWriteDomesticConsent2 invalidConsent = new OBWriteDomesticConsent2()
                .data(new OBWriteDataDomesticConsent2()
                        .initiation(new OBDomestic2().creditorAccount(new OBCashAccount3()))
                );

        // When
        disabledMoneyTransferPaymentValidator.validate(invalidConsent);

        // Then
        // No exception because validation is disabled
    }

    @Test
    public void validate_enabled_notAMoneyTransfer_ignore() throws Exception {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 bacsConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        bacsConsent.getData()
                .getInitiation()
                .localInstrument("UK.OBIE.BACS")
                .debtorAccount(null);

        // When
        moneyTransferPaymentValidator.validate(bacsConsent);

        // Then
        // No exception because this is not a money transfer payment
    }

    @Test
    public void validate_enabled_validMoneyTransfer_noException() throws Exception {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        moneyTransferPaymentValidator.validate(moneyTransferConsent);

        // Then
        // No exception because this is not a money transfer payment
    }

    @Test
    public void validate_enabled_invalidMoneyTransfer_wrongCreditorAccountType_exception() {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());  // Payment must be to an account not a card
        moneyTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  moneyTransferPaymentValidator.validate(moneyTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_MONEY_TRANSFER_INVALID_CREDITOR_ACCOUNT.getMessage());
    }

    @Test
    public void validate_enabled_invalidMoneyTransfer_wrongDebtorAccountType_exception() {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue()); // Debtor bank account not credit card - invalid
        moneyTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  moneyTransferPaymentValidator.validate(moneyTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_MONEY_TRANSFER_INVALID_DEBTOR_ACCOUNT.getMessage());
    }

    @Test
    public void validate_enabled_invalidMoneyTransfer_missingDebtorAccount_exception() {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().debtorAccount(null); // Missing debtor account
        moneyTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  moneyTransferPaymentValidator.validate(moneyTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_MONEY_TRANSFER_INVALID_DEBTOR_ACCOUNT.getMessage());
    }

    @Test
    public void validate_enabled_invalidMoneyTransfer_missingReference_exception() {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getRemittanceInformation().reference(null); // missing ref field
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  moneyTransferPaymentValidator.validate(moneyTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_MONEY_TRANSFER_INVALID_REMITTANCE_REFERENCE.getMessage());
    }

    @Test
    public void validate_enabled_invalidMoneyTransfer_notRemittance_exception() {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().remittanceInformation(null); // missing remittance
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.PARTYTOPARTY);

        // When
        assertThatThrownBy(() ->  moneyTransferPaymentValidator.validate(moneyTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_MONEY_TRANSFER_INVALID_REMITTANCE_REFERENCE.getMessage());
    }

    @Test
    public void validate_enabled_invalidMoneyTransfer_wrongContextCode_exception() {
        // Given
        MoneyTransferPaymentValidator moneyTransferPaymentValidator = new MoneyTransferPaymentValidator(true);
        OBWriteDomesticConsent2 moneyTransferConsent = JMockData.mock(OBWriteDomesticConsent2.class);
        moneyTransferConsent.getData().getInitiation().localInstrument(LocalInstrument.UK_OBIE_MoneyTransfer.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getCreditorAccount().schemeName(AccountSchemeName.UK_OBIE_SORTCODEACCOUNTNUMBER.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getDebtorAccount().schemeName(AccountSchemeName.UK_OBIE_PAN.getNamespacedValue());
        moneyTransferConsent.getData().getInitiation().getRemittanceInformation().reference("ref123");
        moneyTransferConsent.getRisk().paymentContextCode(OBExternalPaymentContext1Code.BILLPAYMENT); // Wrong code

        // When
        assertThatThrownBy(() ->  moneyTransferPaymentValidator.validate(moneyTransferConsent))
                // Then
                .isInstanceOf(OBErrorException.class)
                .hasMessage(OBRIErrorType.PAYMENT_MONEY_TRANSFER_INVALID_PAYMENT_CONTEXT_CODE.getMessage());
    }

}