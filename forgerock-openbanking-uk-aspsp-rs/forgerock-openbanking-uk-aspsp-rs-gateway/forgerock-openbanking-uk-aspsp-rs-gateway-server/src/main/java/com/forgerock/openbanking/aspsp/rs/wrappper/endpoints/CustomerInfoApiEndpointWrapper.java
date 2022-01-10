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
import com.forgerock.openbanking.common.model.openbanking.domain.account.common.FRExternalPermissionsCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.account.AccountRequest;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class CustomerInfoApiEndpointWrapper extends AccountsApiEndpointWrapper<CustomerInfoApiEndpointWrapper,
        CustomerInfoApiEndpointWrapper.CustomerInfoEndpointContent>{

    public CustomerInfoApiEndpointWrapper(RSEndpointWrapperService rsEndpointWrapperService, TppStoreService tppStoreService) {
        super(rsEndpointWrapperService, tppStoreService);
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

        verifyMatlsFromAccessToken();

        verifyCustomerInfoRequest();

        verifyExpirationTime();

        verifyPermissions();
    }

    public void verifyCustomerInfoRequest() throws OBErrorException {
        log.debug("Verifying Customer Info Consent");
        verifyAccountRequestStatus();
        AccountRequest accountConsent = getAccountRequest();
        List<FRExternalPermissionsCode> permissions = accountConsent.getPermissions();
        for(FRExternalPermissionsCode permssion : permissions){
            if(permssion != FRExternalPermissionsCode.READCUSTOMERINFOCONSENT){
                log.info("The associated AccountRequest contains a permission that is not '{}'",
                        FRExternalPermissionsCode.READCUSTOMERINFOCONSENT);
                throw new OBErrorException(OBRIErrorType.PERMISSIONS_INVALID,
                        List.of(FRExternalPermissionsCode.READCUSTOMERINFOCONSENT, permissions));
            }
        }
        log.debug("verifyCustomerInfoRequest() - request has correct permissions for customer info");
    }


    @Override
    protected ResponseEntity run(CustomerInfoEndpointContent main) throws OBErrorException, JsonProcessingException {
        AccountRequest accountAccessConsent = getAccountRequest();
        return main.run(accountAccessConsent, accountAccessConsent.getPermissions());
    }

    public interface CustomerInfoEndpointContent {
        ResponseEntity run(AccountRequest accountAccessConsent, List<FRExternalPermissionsCode> permissions)
                throws OBErrorException;
    }

}
