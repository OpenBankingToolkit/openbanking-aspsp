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
import com.forgerock.openbanking.commons.model.openbanking.v3_1.payment.FRInternationalConsent2;
import com.forgerock.openbanking.commons.model.rcs.consentdetails.InternationalPaymentConsentDetails;
import com.forgerock.openbanking.commons.services.store.payment.InternationalPaymentService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBExchangeRate2;
import uk.org.openbanking.datamodel.payment.OBInternational2;
import uk.org.openbanking.datamodel.payment.OBRemittanceInformation1;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSInternationalPaymentDetailsApi implements RCSDetailsApi {

    private final InternationalPaymentService paymentService;
    private final RCSErrorService rcsErrorService;
    private final TppStoreService tppStoreService;
    private final AccountService accountService;

    public RCSInternationalPaymentDetailsApi(InternationalPaymentService paymentService, RCSErrorService rcsErrorService, TppStoreService tppStoreService, AccountService accountService) {
        this.paymentService = paymentService;
        this.rcsErrorService = rcsErrorService;
        this.tppStoreService = tppStoreService;
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRInternationalConsent2 payment = paymentService.getPayment(consentId);

        // Only show the debtor account if specified in consent
        if (payment.getInitiation().getDebtorAccount() != null) {
            Optional<FRAccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(payment.getInitiation().getDebtorAccount().getIdentification(), accounts);
            if (matchingUserAccount.isEmpty()) {
                log.error("The PISP '{}' created the payment request '{}' but the debtor account: {} on the payment consent " +
                        " is not one of the user's accounts: {}.", payment.getPispId(), consentId, payment.getInitiation().getDebtorAccount(), accounts);
                return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND,
                        payment.getPispId(), consentId, accounts);
            }
            accounts = Collections.singletonList(matchingUserAccount.get());
        }

        Optional<Tpp> isTpp = tppStoreService.findById(payment.getPispId());
        if (isTpp.isEmpty()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", payment.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        payment.setUserId(username);
        paymentService.updatePayment(payment);

        final OBInternational2 initiation = payment.getInitiation();
        final OBExchangeRate2 exchangeRateInformation = payment.getCalculatedExchangeRate();
        return ResponseEntity.ok(InternationalPaymentConsentDetails.builder()
                .instructedAmount(initiation.getInstructedAmount())
                .rate(new OBExchangeRate2()
                        .exchangeRate(exchangeRateInformation.getExchangeRate())
                        .rateType(exchangeRateInformation.getRateType())
                        .contractIdentification(exchangeRateInformation.getContractIdentification())
                        .unitCurrency(exchangeRateInformation.getUnitCurrency()))
                .accounts(accounts)
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(payment.getPispName())
                .clientId(clientId)
                .currencyOfTransfer(initiation.getCurrencyOfTransfer())
                .paymentReference(Optional.ofNullable(
                        initiation.getRemittanceInformation())
                        .map(OBRemittanceInformation1::getReference)
                        .orElse(""))
                .build());
    }
}
