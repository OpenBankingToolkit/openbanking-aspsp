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
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;

/**
 * A custom validator for Domestic Single Payments that represent Balance Transfer payments from Payment Card to Payment Card
 */
@Component
@Slf4j
public class BalanceTransferPaymentValidator {
    // NOTE: The requirements here come from LBG. We do not know how much of this is generic to all banks but as this validator is configurable it can be
    // switched off for sandboxes that do not want these rules.

    private final boolean enabled;

    public BalanceTransferPaymentValidator( @Value("${rs.api.payment.validate.balancetransfer:true}") boolean enabled) {
        this.enabled = enabled;
        log.info("Balance Transfer Payment validation enabled: {} ", enabled);
    }

    public void validate(final OBWriteDomesticConsent2 consent) throws OBErrorException {
        Preconditions.checkNotNull(consent.getData().getInitiation(), "Consent initiation should never be null at this point as it's already validated by framework");

        if (!handles(consent)) {
            log.trace("This consent is not handled by this validator: {}", consent);
            return;
        }

        // Creditor account must be present and a payment card
        if (!AccountSchemeName.isAccountOfType(consent.getData().getInitiation().getCreditorAccount(), AccountSchemeName.UK_OBIE_PAN)) {
            log.debug("Balance Transfer Consent creditor account failed validation. Creditor Account was: {}", consent.getData().getInitiation().getCreditorAccount());
            throw new OBErrorException(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_CREDITOR_ACCOUNT);
        }

        // Debtor account must be present and a payment card
        if (!AccountSchemeName.isAccountOfType(consent.getData().getInitiation().getDebtorAccount(), AccountSchemeName.UK_OBIE_PAN)) {
            log.debug("Balance Transfer Consent debtor account failed validation. Debtor Account was: {}", consent.getData().getInitiation().getDebtorAccount());
            throw new OBErrorException(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_DEBTOR_ACCOUNT);
        }

        // Remittance reference must be not empty (should contain an offer code/id for balance transfer)
        if (consent.getData().getInitiation().getRemittanceInformation() == null ||
                StringUtils.isEmpty(consent.getData().getInitiation().getRemittanceInformation().getReference())) {
            log.debug("Balance Transfer remittance information reference failed validation. Remittance was: {}", consent.getData().getInitiation().getRemittanceInformation());
            throw new OBErrorException(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_REMITTANCE_REFERENCE);
        }

        // PaymentContextCode must be party to party
        if (consent.getRisk() == null || !OBExternalPaymentContext1Code.PARTYTOPARTY.equals(consent.getRisk().getPaymentContextCode())) {
            log.debug("Balance Transfer payment 'Risk.PaymentContextCode' failed validation as it was not {}. The Risk was: {}", OBExternalPaymentContext1Code.PARTYTOPARTY, consent.getRisk());
            throw new OBErrorException(OBRIErrorType.PAYMENT_BALANCE_TRANSFER_INVALID_PAYMENT_CONTEXT_CODE);
        }
        log.debug("Balance Transfer Consent with identification {} passed validation.", consent.getData().getInitiation().getInstructionIdentification());

    }

    private boolean handles(OBWriteDomesticConsent2 consent) {
        return enabled && LocalInstrument.UK_OBIE_BalanceTransfer.isEqual(consent.getData().getInitiation().getLocalInstrument());
    }
}
