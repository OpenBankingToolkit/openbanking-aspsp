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
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;

/**
 * A custom validator for Domestic Single Payments that represent Paym payments to a mobile device
 */
@Component
@Slf4j
public class PaymPaymentValidator {
    // NOTE: The requirements here come from LBG. We do not know how much of this is generic to all banks but as this validator is configurable it can be
    // switched off for sandboxes that do not want these rules.

    private final boolean enabled;

    public PaymPaymentValidator(@Value("${rs.api.payment.validate.paym:true}") boolean enabled) {
        this.enabled = enabled;
        log.info("Paym (mobile) Payment validation enabled: {} ", enabled);
    }

    public void validate(final OBWriteDomesticConsent2 consent) throws OBErrorException {
        Preconditions.checkNotNull(consent.getData().getInitiation(), "Consent initiation should never be null at this point as it's already validated by framework");
        Preconditions.checkNotNull(consent.getData().getInitiation().getCreditorAccount(), "Consent initiation creditor account is mandatory in model so should never be null at this point as it's already validated by framework");

        if (!handles(consent)) {
            log.trace("This consent is not handled by this validator: {}", consent);
            return;
        }

        // Creditor account must be scheme PAYM
        if (!AccountSchemeName.UK_OBIE_PAYM.isEqual(consent.getData().getInitiation().getCreditorAccount().getSchemeName())) {
            log.debug("Paym payment 'CreditorAccount.SchemeName' failed validation as it was not {}. The Creditor Account was: {}", AccountSchemeName.UK_OBIE_PAYM, consent.getData().getInitiation().getCreditorAccount());
            throw new OBErrorException(OBRIErrorType.PAYMENT_PAYM_WRONG_CREDITOR_ACCOUNT_SCHEME);
        }

        // Creditor account identification cannot be empty (it is expected to identify mobile number)
        if (StringUtils.isEmpty(consent.getData().getInitiation().getCreditorAccount().getIdentification())
            || (consent.getData().getInitiation().getCreditorAccount().getIdentification().trim().isEmpty())) {
            log.debug("Paym payment 'CreditorAccount.Identification' failed validation as it is mandatory but empty. The Creditor Account was: {}", consent.getData().getInitiation().getCreditorAccount());
            throw new OBErrorException(OBRIErrorType.PAYMENT_PAYM_MISSING_CUSTOMER_IDENTIFICATION);
        }

        // Creditor account identification should contain a UK mobile number
        if (!UKMobilePhoneFormat.isValid(consent.getData().getInitiation().getCreditorAccount().getIdentification())) {
            log.debug("Paym payment 'CreditorAccount.Identification' failed validation as it is not a UK mobile phone number. The Creditor Account was: {}", consent.getData().getInitiation().getCreditorAccount());
            throw new OBErrorException(OBRIErrorType.PAYMENT_PAYM_CUSTOMER_IDENTIFICATION_NOT_A_UK_MOBILE_NUMBER);
        }

        // PaymentContextCode must be party to party
        if (consent.getRisk() == null || !OBExternalPaymentContext1Code.PARTYTOPARTY.equals(consent.getRisk().getPaymentContextCode())) {
            log.debug("Paym payment 'Risk.PaymentContextCode' failed validation as it was not {}. The Risk was: {}", OBExternalPaymentContext1Code.PARTYTOPARTY, consent.getRisk());
            throw new OBErrorException(OBRIErrorType.PAYMENT_PAYM_INVALID_PAYMENT_CONTEXT_CODE);
        }

        // Local Instrument may be empty or Paym
        if (!StringUtils.isEmpty(consent.getData().getInitiation().getLocalInstrument())
                && !LocalInstrument.UK_OBIE_Paym.isEqual(consent.getData().getInitiation().getLocalInstrument())) {
            log.debug("Paym Consent local instrument failed validation as it was not {}. Local instrument was: {}", LocalInstrument.UK_OBIE_Paym, consent.getData().getInitiation().getLocalInstrument());
            throw new OBErrorException(OBRIErrorType.PAYMENT_PAYM_INVALID_LOCAL_INSTRUMENT);
        }
        log.debug("Paym Consent with identification {} passed validation.", consent.getData().getInitiation().getInstructionIdentification());
    }

    private boolean handles(OBWriteDomesticConsent2 consent) {
        // If paying to a mobile PAYM account or using local instrument of PAym then this must be Paym payment by definition
        return enabled &&
                (AccountSchemeName.UK_OBIE_PAYM.isEqual(consent.getData().getInitiation().getCreditorAccount().getSchemeName())
                || LocalInstrument.UK_OBIE_Paym.isEqual(consent.getData().getInitiation().getLocalInstrument())
                );
    }



}
