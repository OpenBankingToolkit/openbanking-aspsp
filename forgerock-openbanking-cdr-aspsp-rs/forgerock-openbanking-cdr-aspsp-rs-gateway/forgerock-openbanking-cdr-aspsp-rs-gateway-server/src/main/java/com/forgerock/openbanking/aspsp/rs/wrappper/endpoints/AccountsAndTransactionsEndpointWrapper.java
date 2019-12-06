/**
 * Copyright 2019 ForgeRock AS.
 *
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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRAccountRequest;
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
