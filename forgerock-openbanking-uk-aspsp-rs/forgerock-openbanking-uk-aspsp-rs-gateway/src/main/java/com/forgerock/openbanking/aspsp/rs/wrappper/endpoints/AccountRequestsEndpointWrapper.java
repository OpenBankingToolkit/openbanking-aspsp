/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

public class AccountRequestsEndpointWrapper extends AccountsApiEndpointWrapper<AccountRequestsEndpointWrapper, AccountRequestsEndpointWrapper.RestEndpointContent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRequestsEndpointWrapper.class);

    private String accountRequestId;

    public AccountRequestsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    @Override
    protected ResponseEntity run(RestEndpointContent main) throws OBErrorException {
        return main.run(tppId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.ACCOUNTS),
                Arrays.asList(
                        OIDCConstants.GrantType.CLIENT_CREDENTIAL
                )
        );

        verifyMatlsFromAccessToken();

        verifyAccountRequest();
    }

    public void verifyAccountRequest() throws OBErrorException {
        if (accountRequestId != null) {
            Optional<FRAccountRequest> accountRequest = rsEndpointWrapperService.accountRequestStore.get(accountRequestId);
            if (!accountRequest.isPresent()) {
                LOGGER.warn("AISP {} is trying to delete an account request {} that doesn't exist",
                        tppId, accountRequestId);
                throw new OBErrorException(OBRIErrorType.ACCOUNT_REQUEST_NOT_FOUND,
                        accountRequestId
                );
            }
            if (!accountRequest.get().getClientId().equals(tppId)) {
                LOGGER.warn("AISP {} is trying to delete an account request {} that it doesn't own",
                        tppId, accountRequestId);
                throw new OBErrorException(OBRIErrorType.ACCESS_TOKEN_INVALID,
                        "The access token is associated with the TPP ID '" + tppId + "' whereas the " +
                                "account request is associated with the TPP ID '" + accountRequest.get().getClientId() + "'"
                );
            }
        }
    }

    public void setAccountRequestId(String accountRequestId) {
        this.accountRequestId = accountRequestId;
    }

    public interface RestEndpointContent {
        ResponseEntity run(String aispId) throws OBErrorException;
    }
}
