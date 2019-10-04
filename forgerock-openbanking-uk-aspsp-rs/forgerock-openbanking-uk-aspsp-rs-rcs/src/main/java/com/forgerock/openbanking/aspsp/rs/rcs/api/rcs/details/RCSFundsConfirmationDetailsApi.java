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
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.commons.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.commons.model.rcs.consentdetails.FundsConfirmationConsentDetails;
import com.forgerock.openbanking.commons.services.store.funds.FundsConfirmationService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSFundsConfirmationDetailsApi implements RCSDetailsApi {

    private final FundsConfirmationService fundsConfirmationService;
    private final RCSErrorService rcsErrorService;
    private final AccountService accountService;
    private final TppStoreService tppStoreService;

    public RCSFundsConfirmationDetailsApi(FundsConfirmationService fundsConfirmationService, RCSErrorService rcsErrorService, AccountService accountService, TppStoreService tppStoreService) {
        this.fundsConfirmationService = fundsConfirmationService;
        this.rcsErrorService = rcsErrorService;
        this.accountService = accountService;
        this.tppStoreService = tppStoreService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRFundsConfirmationConsent1 consent = fundsConfirmationService.getConsent(consentId);

        // Verify that the 'DebtorAccount' matches one of the accounts of the user and define as the selected account.
        Optional<FRAccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(consent.getDebtorAccount().getIdentification(), accounts);
        if (!matchingUserAccount.isPresent()) {
            log.error("The PISP '{}' created the funds confirmation request '{}' but the debtor account: {} on the consent " +
                    " is not one of the user's accounts: {}.", consent.getPispId(), consentId, consent.getDebtorAccount(), accounts);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_FUNDS_CONFIRMATION_REQUEST,
                    consent.getPispId(), consentId, clientId);
        }

        Optional<Tpp> isTpp = tppStoreService.findById(consent.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", consent.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        consent.setUserId(username);
        fundsConfirmationService.updateConsent(consent);

        return ResponseEntity.ok(FundsConfirmationConsentDetails.builder()
                .expirationDateTime(consent.getFundsConfirmationConsent().getData().getExpirationDateTime())
                .accounts(Collections.singletonList(matchingUserAccount.get()))
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(consent.getPispName())
                .clientId(clientId)
                .build());
    }
}
