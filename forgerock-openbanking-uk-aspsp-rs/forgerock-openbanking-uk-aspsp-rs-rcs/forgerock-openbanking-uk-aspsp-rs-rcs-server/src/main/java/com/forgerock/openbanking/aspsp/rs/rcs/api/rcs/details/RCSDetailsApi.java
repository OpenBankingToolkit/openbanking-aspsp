/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;


import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RCSDetailsApi {
    Logger log = LoggerFactory.getLogger(RCSDetailsApi.class);

    ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username,
                                  String consentId, String clientId) throws OBErrorException;

    /**
     * TPP making the consent request to the user should be same one that created the new payment consent in RS
     */
    default void verifyTppCreatedPayment(String clientIdRequestingConsent, String clientIdThatCreatedPayment, String consentId) throws OBErrorException {
        //Verify the pisp is the same than the one that created this payment
        log.debug("PISP that is requesting consent in RCS Details service => '{}'", clientIdRequestingConsent);
        log.debug("PISP that created the payment consent in RS-STORE => '{}'", clientIdThatCreatedPayment);
        if (!clientIdRequestingConsent.equals(clientIdThatCreatedPayment)) {
            log.error("The PISP with client id: '{}' created this payment request '{}' but it's a PISP with client id '{}' that is trying to get" +
                    " consent for it.", clientIdThatCreatedPayment, consentId, clientIdRequestingConsent);
            throw new OBErrorException(OBRIErrorType.RCS_CONSENT_REQUEST_INVALID_PAYMENT_REQUEST, clientIdThatCreatedPayment, consentId, clientIdRequestingConsent);
        }
    }
}
