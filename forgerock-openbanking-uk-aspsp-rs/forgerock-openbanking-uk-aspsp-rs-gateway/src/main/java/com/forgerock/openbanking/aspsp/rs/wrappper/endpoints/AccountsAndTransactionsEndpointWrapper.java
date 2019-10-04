/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.FRAccountRequest;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.OBExternalPermissions1Code;

import java.util.Arrays;
import java.util.List;

public class AccountsAndTransactionsEndpointWrapper extends AccountsApiEndpointWrapper<AccountsAndTransactionsEndpointWrapper, AccountsAndTransactionsEndpointWrapper.RestEndpointContentMultiPermissions> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsAndTransactionsEndpointWrapper.class);


    public AccountsAndTransactionsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService) {
        super(RSEndpointWrapperService);
    }

    @Override
    protected ResponseEntity run(RestEndpointContentMultiPermissions main) throws OBErrorException, JsonProcessingException {
        return main.run(getAccountRequest(), getAccountRequest().getPermissions(), getPageNumber());
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        super.applyFilters();

        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.ACCOUNTS),
                Arrays.asList(
                        OIDCConstants.GrantType.AUTHORIZATION_CODE,
                        OIDCConstants.GrantType.HEADLESS_AUTH
                )
        );

        verifyMatlsFromAccountRequest();

        verifyAccountRequestStatus();

        verifyExpirationTime();

        verifyAccountId();

        verifyPermissions();
    }

    public interface RestEndpointContentMultiPermissions {
        ResponseEntity run(FRAccountRequest accountRequest, List<OBExternalPermissions1Code> permissions, int pageNumber) throws OBErrorException, JsonProcessingException;
    }
}
