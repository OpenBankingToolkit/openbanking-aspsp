/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.rcs.consentdetails.AccountsConsentDetails;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Service
@Slf4j
public class RCSAccountDetailsApi implements RCSDetailsApi {

    @Autowired
    private RCSErrorService rcsErrorService;
    @Autowired
    private AccountRequestStoreService accountRequestStoreService;
    @Autowired
    private TppStoreService tppStoreService;

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String accountRequestId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The account request id '{}''", accountRequestId);

        Optional<FRAccountRequest> isAccountRequest = accountRequestStoreService.get(accountRequestId);
        if (!isAccountRequest.isPresent()) {
            log.error("The AISP '{}' is referencing an account request {} that doesn't exist",  clientId,
                    accountRequestId);
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_UNKNOWN_ACCOUNT_REQUEST,
                    clientId, accountRequestId);
        }
        FRAccountRequest accountRequest = isAccountRequest.get();

        //Verify the aisp is the same than the one that created this accountRequest ^
        if (!clientId.equals(accountRequest.getClientId())) {
            log.error("The AISP '{}' created the account request '{}' but it's AISP '{}' that is trying to get" +
                    " consent for it.", accountRequest.getClientId(), clientId, accountRequestId);
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_CONSENT,
                    accountRequest.getClientId(), clientId, accountRequestId);
        }


        Optional<Tpp> isTpp = tppStoreService.findById(accountRequest.getAispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", accountRequest.getAispId(), clientId, accountRequest.getId());
            return rcsErrorService.error(OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, accountRequest.getId());
        }
        Tpp tpp = isTpp.get();

        log.debug("Populate the model with the payment and consent data");
        accountRequest.setUserId(username);
        accountRequestStoreService.save(accountRequest);

        log.debug("Populate the model with the payment and consent data");

        return ok(AccountsConsentDetails.builder()
                .permissions(accountRequest.getPermissions())
                .fromTransaction(accountRequest.getTransactionFromDateTime())
                .toTransaction(accountRequest.getTransactionToDateTime())
                .accounts(accounts)
                .username(username)
                .logo(tpp.getLogo())
                .clientId(clientId)
                .aispName(accountRequest.getAispName())
                .expiredDate(accountRequest.getExpirationDateTime())
                .build());
    }
}
