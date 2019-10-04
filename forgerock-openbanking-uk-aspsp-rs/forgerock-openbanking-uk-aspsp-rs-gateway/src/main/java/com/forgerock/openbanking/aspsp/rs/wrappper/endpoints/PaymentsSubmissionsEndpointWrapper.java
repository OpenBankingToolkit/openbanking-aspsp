/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.payment.OBRisk1;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

public class PaymentsSubmissionsEndpointWrapper extends RSEndpointWrapper<PaymentsSubmissionsEndpointWrapper, PaymentsSubmissionsEndpointWrapper.PaymentRestEndpointContent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsSubmissionsEndpointWrapper.class);

    private FRPaymentConsent payment;

    public PaymentsSubmissionsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    public FRPaymentConsent getPayment() {
        return payment;
    }

    public PaymentsSubmissionsEndpointWrapper payment(FRPaymentConsent payment) {
        this.payment = payment;
        return this;
    }

    @Override
    protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
        return main.run(tppId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS),
                Arrays.asList(
                        OIDCConstants.GrantType.AUTHORIZATION_CODE,
                        OIDCConstants.GrantType.HEADLESS_AUTH
                )
        );

        verifyMatlsFromAccessToken();

    }

    public void verifyPaymentIdWithAccessToken() throws OBErrorException {
        try {
            String paymentIdFromAccessToken = rsEndpointWrapperService.accessTokenService.getIntentId(accessToken);

            LOGGER.info("Payment id {} associated with the access token", payment.getId());
            if (!payment.getId().equals(paymentIdFromAccessToken)) {
                LOGGER.error("Payment id {} associated with the access token is not the same than the payment id {} " +
                        "associated with the payment submission", paymentIdFromAccessToken, payment.getId());
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_PAYMENT_ID,
                        paymentIdFromAccessToken, payment.getId()
                );
            }
        } catch (ParseException | IOException e) {
            LOGGER.error("Can't retrieve claims from access token: {}", accessToken);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_FORMAT);
        }
    }

    public void verifyPaymentStatus() throws OBErrorException {
        switch (payment.getStatus()) {
            case ACCEPTEDTECHNICALVALIDATION:
                LOGGER.warn("Payment ({}) not consented by PSU yet.", payment.getId());
                throw new OBErrorException(OBRIErrorType.PAYMENT_WAITING_PSU_CONSENT,
                        payment.getStatus()
                );
            case PENDING:
                LOGGER.warn("Payment ({}) still pending.", payment.getId());
                throw new OBErrorException(OBRIErrorType.PAYMENT_STILL_PENDING,
                        payment.getStatus()
                );
            case REJECTED:
                LOGGER.warn("Payment ({}) was rejected.", payment.getId());
                throw new OBErrorException(OBRIErrorType.PAYMENT_REJECTED,
                        payment.getStatus()
                );
            case ACCEPTEDSETTLEMENTCOMPLETED:
            case ACCEPTEDSETTLEMENTINPROCESS:
                // This may be an idempotent request for a submission that ios already accepted so valid here but will be verified in idempotency key check
            case ACCEPTEDCUSTOMERPROFILE:
            case AUTHORISED:
                //The customer has consent the payment, we can continue
        }
    }

    public void verifyRiskAndInitiation(Object initiation, OBRisk1 risk1) throws OBErrorException {
        //Verify risk and initiation are equals to initial request
        verifyInitiation(initiation);
        verifyRisk(risk1);
    }

    public void verifyRisk(OBRisk1 risk1) throws OBErrorException {
        //Verify risk are equals to initial request
        if (!payment.getRisk().equals(risk1)) {
            LOGGER.debug("Risk received doesn't match payment setup request. Received:'{}' , expected:'{}'",
                    risk1, payment.getRisk());
            throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_RISK);
        }
    }

    public void verifyInitiation(Object initiation) throws OBErrorException {
        //Verify initiation are equals to initial request
        if (!payment.getInitiation().equals(initiation)) {
            LOGGER.debug("Initiation received doesn't match payment setup request. Received:'{}' , expected:'{}'",
                    initiation, payment.getInitiation());
            throw new OBErrorException(OBRIErrorType.PAYMENT_INVALID_INITIATION);
        }
    }

    public interface PaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
