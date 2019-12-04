/**
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
package com.forgerock.openbanking.aspsp.rs.rcs.api.rcs.details;

import com.forgerock.openbanking.aspsp.rs.rcs.services.RCSErrorService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountWithBalance;
import com.forgerock.openbanking.common.model.openbanking.v1_1.payment.FRPaymentSetup1;
import com.forgerock.openbanking.common.model.rcs.consentdetails.SinglePaymentConsentDetails;
import com.forgerock.openbanking.common.services.store.payment.SinglePaymentService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.payment.OBRemittanceInformation1;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RCSSinglePaymentDetailsApi implements RCSDetailsApi {

    @Autowired
    private SinglePaymentService singlePaymentService;
    @Autowired
    private RCSErrorService rcsErrorService;
    @Autowired
    private TppStoreService tppStoreService;

    @Override
    public ResponseEntity consentDetails(String remoteConsentRequest, List<FRAccountWithBalance> accounts, String username, String consentId, String clientId) throws OBErrorException {
        log.debug("Received a consent request with consent_request='{}'", remoteConsentRequest);
        log.debug("=> The payment id '{}'", consentId);

        log.debug("Populate the model with the payment and consent data");

        FRPaymentSetup1 payment = singlePaymentService.getPayment(consentId);
        Optional<Tpp> isTpp = tppStoreService.findById(payment.getPispId());
        if (!isTpp.isPresent()) {
            log.error("The TPP '{}' (Client ID {}) that created this consent id '{}' doesn't exist anymore.", payment.getPispId(), clientId, consentId);
            return rcsErrorService.invalidConsentError(remoteConsentRequest, OBRIErrorType.RCS_CONSENT_REQUEST_NOT_FOUND_TPP,
                    clientId, consentId);
        }
        Tpp tpp = isTpp.get();

        //Verify the pisp is the same than the one that created this payment ^
        verifyTppCreatedPayment(clientId, isTpp.get().getClientId(), consentId);

        //Associate the payment to this user
        payment.setUserId(username);
        singlePaymentService.updatePayment(payment);

        return ResponseEntity.ok(SinglePaymentConsentDetails.builder()
                .instructedAmount(payment.getInitiation().getInstructedAmount())
                .accounts(accounts)
                .username(username)
                .logo(tpp.getLogo())
                .merchantName(payment.getPispName())
                .clientId(clientId)
                .pispName(payment.getPispName())
                .paymentReference(Optional.ofNullable(
                        payment.getInitiation().getRemittanceInformation())
                        .map(OBRemittanceInformation1::getReference)
                        .orElse(""))
                .build());
    }
}
