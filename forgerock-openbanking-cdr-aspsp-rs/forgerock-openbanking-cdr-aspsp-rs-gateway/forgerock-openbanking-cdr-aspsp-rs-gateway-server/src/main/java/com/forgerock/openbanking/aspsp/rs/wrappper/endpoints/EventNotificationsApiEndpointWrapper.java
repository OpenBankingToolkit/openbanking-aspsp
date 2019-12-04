/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import uk.org.openbanking.OBConstants;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;


@Component
@Slf4j
public class EventNotificationsApiEndpointWrapper extends RSEndpointWrapper<EventNotificationsApiEndpointWrapper, EventNotificationsApiEndpointWrapper.EventNotificationRestEndpointContent> {

    public EventNotificationsApiEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    @Override
    protected ResponseEntity run(EventNotificationRestEndpointContent main) throws OBErrorException {
        return main.run(tppId);
    }

    @Override
    public void verifyAccessToken(List<String> expectedScopes, List<OIDCConstants.GrantType> expectedGrantTypes) throws OBErrorException {
        try {
            //Verify access token
            log.info("Verify the access token {}", authorization);
            accessToken = rsEndpointWrapperService.amResourceServerService.verifyAccessToken(authorization);
            List<String> scopes = (List<String>) accessToken.getJWTClaimsSet().getClaim(OBConstants.OIDCClaim.SCOPE);

            String grantTypeSerialised = accessToken.getJWTClaimsSet().getStringClaim(OBConstants.OIDCClaim.GRANT_TYPE);
            if (grantTypeSerialised == null) {
                log.error("We managed to get an access token that doesn't have a grant type claim defined: {}", authorization);
                throw new OBErrorException(SERVER_ERROR,
                        "Access token grant type is undefined"
                );
            }
            OIDCConstants.GrantType grantType = OIDCConstants.GrantType.fromType(grantTypeSerialised);

            if (!OIDCConstants.GrantType.REFRESH_TOKEN.equals(grantType) && !expectedGrantTypes.contains(grantType)) {
                log.debug("The access token grant type {} doesn't match one of the expected grant types {}", grantType, expectedGrantTypes);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_GRANT_TYPE,
                        grantType, expectedGrantTypes
                );
            }


            if (scopes.stream().noneMatch(expectedScopes::contains)) {
                log.warn("The access token {} contains scopes: {} but needs at least one of the expected scopes: {}", authorization, scopes, expectedScopes);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_SCOPE,
                        expectedScopes
                );
            }
        } catch (ParseException e) {
            log.warn("Couldn't parse the the access token {}. It's probably not stateless and therefore, not " +
                    "an access token generated by our ASPSP-AS", authorization);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID_FORMAT);
        } catch (InvalidTokenException e) {
            log.warn("Invalid access token {}", authorization);
            throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID,
                    e.getMessage()
            );
        } catch (IOException e) {
            log.error("IO exception", e);
            throw new OBErrorException(SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        // Do not verify financial id as it is not required for events API from 3.1.2 onwards
        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS, OpenBankingConstants.Scope.ACCOUNTS, OpenBankingConstants.Scope.FUNDS_CONFIRMATIONS),
                Collections.singletonList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL
                )
        );

        verifyMatlsFromAccessToken();
    }

    public interface EventNotificationRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }
}
