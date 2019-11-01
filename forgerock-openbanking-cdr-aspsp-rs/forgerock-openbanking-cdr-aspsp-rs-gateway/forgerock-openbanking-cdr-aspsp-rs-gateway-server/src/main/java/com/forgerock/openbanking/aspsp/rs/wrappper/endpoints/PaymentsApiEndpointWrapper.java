/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

public class PaymentsApiEndpointWrapper extends RSEndpointWrapper<PaymentsApiEndpointWrapper, PaymentsApiEndpointWrapper.PaymentRestEndpointContent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsApiEndpointWrapper.class);
    private FRPaymentConsent payment;


    public PaymentsApiEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    public PaymentsApiEndpointWrapper payment(FRPaymentConsent payment) {
        this.payment = payment;
        return this;
    }

    @Override
    protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
        return main.run(tppId);
    }


    public void verifyConsentStatusForConfirmationOfFund() throws OBErrorException {
        switch (payment.getStatus()) {
            case AUTHORISED:
                //The customer has consent the payment, we can continue
                break;
            default:
                LOGGER.warn("Consent ({}) not authorised.", payment.getId());
                throw new OBErrorException(OBRIErrorType.CONSENT_STATUS_NOT_AUTHORISED,
                        payment.getStatus()
                );

        }
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS),
                Arrays.asList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL
                )
        );

        verifyMatlsFromAccessToken();
    }

    public interface PaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
