/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.Collections;

import static com.forgerock.openbanking.model.error.OBRIErrorType.REQUEST_FIELD_INVALID;

@Slf4j
public class FundsConfirmationApiEndpointWrapper extends RSEndpointWrapper<FundsConfirmationApiEndpointWrapper, FundsConfirmationApiEndpointWrapper.FundsConfirmationRestEndpointContent> {
    private FRFundsConfirmationConsent1 consent;

    public FundsConfirmationApiEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    public FRFundsConfirmationConsent1 getConsent() {
        return consent;
    }

    public FundsConfirmationApiEndpointWrapper consent(FRFundsConfirmationConsent1 consent) {
        this.consent = consent;
        return this;
    }


    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Collections.singletonList(OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS),
                Arrays.asList(
                        OIDCConstants.GrantType.AUTHORIZATION_CODE, OIDCConstants.GrantType.HEADLESS_AUTH
                )
        );

        verifyMatlsFromAccessToken();
    }

    @Override
    protected ResponseEntity run(FundsConfirmationApiEndpointWrapper.FundsConfirmationRestEndpointContent main) throws OBErrorException {
        try {
            return main.run(tppId);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND == e.getStatusCode()) {
                // Change the 404 to 400 in line with V3 spec - implication is that client has submitted a non-existent id in URL therefore a bad request as opposed to resource not found
                // https://openbanking.atlassian.net/wiki/spaces/DZ/pages/641992418/Read+Write+Data+API+Specification+-+v3.0#Read/WriteDataAPISpecification-v3.0-400(BadRequest)v/s404(NotFound)
                log.debug("Resource server returned 404 so an incorrect resource has been requested.");
                throw new OBErrorException(REQUEST_FIELD_INVALID, "resource does not exist");
            }
            throw e;
        }
    }


    public void verifyConsentExpiration() throws OBErrorException {
        final DateTime expirationDateTime = consent.getFundsConfirmationConsent().getData().getExpirationDateTime();
        if (expirationDateTime!=null
                && expirationDateTime.isBefore(DateTime.now())) {
            log.debug("Funds confirmation ({}) has expired.", consent.getId());
            throw new OBErrorException(OBRIErrorType.FUNDS_CONFIRMATION_EXPIRED,
                    expirationDateTime.toString()
            );
        }
    }

    public void verifyConsentStatus() throws OBErrorException {
        switch (consent.getStatus()) {
            case AUTHORISED:
                //The customer has authorised the funds confirmation, we can continue
                break;
            case AWAITINGAUTHORISATION:
                log.warn("Funds confirmation ({}) still awaiting authorisation.", consent.getId());
                throw new OBErrorException(OBRIErrorType.FUNDS_CONFIRMATION_STILL_PENDING,
                        consent.getStatus()
                );
            default:
                log.warn("Funds confirmation ({}) was rejected.", consent.getId());
                throw new OBErrorException(OBRIErrorType.FUNDS_CONFIRMATION_REJECTED,
                        consent.getStatus()
                );
        }
    }

    public interface FundsConfirmationRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
