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
import com.forgerock.openbanking.commons.model.openbanking.v3_1_1.payment.FRDomesticStandingOrderConsent3;
import com.forgerock.openbanking.commons.model.rcs.consentdetails.DomesticStandingOrderPaymentConsentDetails;
import com.forgerock.openbanking.commons.services.openbanking.converter.FRAccountConverter;
import com.forgerock.openbanking.commons.services.openbanking.converter.OBActiveOrHistoricCurrencyAndAmountConverter;
import com.forgerock.openbanking.commons.services.store.payment.DomesticStandingOrderService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.OBStandingOrder5;
import uk.org.openbanking.datamodel.payment.OBDomesticStandingOrder3;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSDomesticStandingOrderDetailsApi implements RCSDetailsApi {

    private final RCSErrorService rcsErrorService;
    private final DomesticStandingOrderService paymentService;
    private final AccountService accountService;
    private final TppStoreService tppStoreService;

    public RCSDomesticStandingOrderDetailsApi(RCSErrorService rcsErrorService, DomesticStandingOrderService paymentService, AccountService accountService, TppStoreService tppStoreService) {
        this.rcsErrorService = rcsErrorService;
        this.paymentService = paymentService;
        this.accountService = accountService;
        this.tppStoreService = tppStoreService;
    }

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRDomesticStandingOrderConsent3 domesticConsent = paymentService.getPayment(consentId);

        // Only show the debtor account if specified in consent
        if (domesticConsent.getInitiation().getDebtorAccount() != null) {
            Optional<FRAccountWithBalance> matchingUserAccount = accountService.findAccountByIdentification(domesticConsent.getInitiation().getDebtorAccount().getIdentification(), accounts);
            if (!matchingUserAccount.isPresent()) {
                log.error("The PISP '{}' created the payment request '{}' but the debtor account: {} on the payment consent " +
                        " is not one of the user's accounts: {}.", domesticConsent.getPispId(), consentId, domesticConsent.getInitiation().getDebtorAccount(), accounts);
                return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_DEBTOR_ACCOUNT_NOT_FOUND,
                        domesticConsent.getPispId(), consentId, accounts);
            }
            accounts = Collections.singletonList(matchingUserAccount.get());
        }

        Optional<Tpp> isTpp = tppStoreService.findById(domesticConsent.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", domesticConsent.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        domesticConsent.setUserId(username);
        paymentService.updatePayment(domesticConsent);

        OBDomesticStandingOrder3 domesticStandingOrder = domesticConsent.getInitiation();
        OBStandingOrder5 standingOrder = new OBStandingOrder5()
                .accountId(domesticConsent.getAccountId())
                .standingOrderId(domesticConsent.getId())
                .finalPaymentAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(domesticStandingOrder.getFinalPaymentAmount()))
                .finalPaymentDateTime(domesticStandingOrder.getFinalPaymentDateTime())
                .firstPaymentAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(domesticStandingOrder.getFirstPaymentAmount()))
                .firstPaymentDateTime(domesticStandingOrder.getFirstPaymentDateTime())
                .nextPaymentDateTime(domesticStandingOrder.getRecurringPaymentDateTime())
                .nextPaymentAmount(OBActiveOrHistoricCurrencyAndAmountConverter.toAccountOBActiveOrHistoricCurrencyAndAmount(domesticStandingOrder.getRecurringPaymentAmount()))
                .frequency(domesticStandingOrder.getFrequency())
                .creditorAccount(FRAccountConverter.toOBCashAccount5(domesticStandingOrder.getCreditorAccount()))
                .reference(domesticStandingOrder.getReference());
        return ResponseEntity.ok(DomesticStandingOrderPaymentConsentDetails.builder()
                .standingOrder(standingOrder)
                .accounts(accounts)
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(domesticConsent.getPispName())
                .clientId(clientId)
                .paymentReference(Optional.ofNullable(
                        domesticConsent.getInitiation().getReference())
                        .orElse(""))
                .build());
    }

}