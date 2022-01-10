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
package com.forgerock.openbanking.aspsp.rs.api.account.v3_0.accountaccessconsent;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentBasicAndDetailPermissionsFilter;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountAccessConsentPermittedPermissionsFilter;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.AccountRequestsEndpointWrapper;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.*;
import uk.org.openbanking.testsupport.customerinfo.CustomerInfoTestDataFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


@RunWith(MockitoJUnitRunner.class)
public class AccountAccessConsentsApiControllerTest {

    private static final String FINANCIAL_ID = "FINANCIAL_ID";
    private static final String ISSUER_ID = "ISSUER_ID";
    private static final String JWKS_URI = "JWKS_URI";
    private static final boolean CUSTOMER_INFO_ENABLED = true;
    @Mock
    private AccountAccessConsentsApiController controller;
    @Mock
    private RsStoreGateway rsStoreGateway;
    @Mock
    private RSEndpointWrapperService rsEndpointWrapperService;
    @Mock
    private AccountAccessConsentPermittedPermissionsFilter accountaccessConsentPermittedPermissionFilter;
    @Mock
    private AccountAccessConsentBasicAndDetailPermissionsFilter accountAccessConsentBasicAndDetailedPermissionFilter;
    @Mock
    private AccountRequestsEndpointWrapper accountRequestsEndpointWrapper;

    private RSConfiguration rsConfiguration = new RSConfiguration(ISSUER_ID, FINANCIAL_ID, JWKS_URI,
            CUSTOMER_INFO_ENABLED);

    @Before
    public void setUp() throws Exception {

        given(rsEndpointWrapperService.accountRequestEndpoint()).willReturn(accountRequestsEndpointWrapper);
        this.controller = new AccountAccessConsentsApiController(rsStoreGateway, rsEndpointWrapperService,
                accountaccessConsentPermittedPermissionFilter , accountAccessConsentBasicAndDetailedPermissionFilter,
                rsConfiguration);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void success_createAccountAccessConsents() throws OBErrorResponseException {
        // given
        final OBReadConsent1 obReadConsent = new OBReadConsent1()
                .data(new OBReadData1().permissions(Collections.singletonList(OBExternalPermissions1Code.READACCOUNTSBASIC)))
                .risk(new OBRisk2());
        Principal principal = mock(Principal.class);
        String AUTHORIZATION = "Bearer " + "token";

        ResponseEntity<OBReadConsentResponse1> responseObject = mock(ResponseEntity.class);
        given(accountRequestsEndpointWrapper.authorization(AUTHORIZATION)).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.xFapiFinancialId(rsConfiguration.getFinancialId())).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.principal(principal)).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.filters(any())).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.execute(any())).willReturn(responseObject);

        // When
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<OBReadConsentResponse1> response = controller.createAccountAccessConsents(obReadConsent, rsConfiguration.getFinancialId(),
                AUTHORIZATION, null, null, null, null, request, principal);

        // Then
        // No exceptions then all good!
    }

    @Test
    public void successCustomerInfoRequest_createAccountAccessConsents() throws OBErrorResponseException {
        // given
        final OBReadConsent1 obReadConsent = CustomerInfoTestDataFactory.aValidCustomerInfoOBReadConsent1();
        Principal principal = mock(Principal.class);
        String AUTHORIZATION = "Bearer " + "token";

        RSConfiguration rsConfiguration = new RSConfiguration(ISSUER_ID, FINANCIAL_ID, JWKS_URI,
                true);

        ResponseEntity<OBReadConsentResponse1> responseObject = mock(ResponseEntity.class);
        given(accountRequestsEndpointWrapper.authorization(AUTHORIZATION)).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.xFapiFinancialId(rsConfiguration.getFinancialId())).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.principal(principal)).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.filters(any())).willReturn(accountRequestsEndpointWrapper);
        given(accountRequestsEndpointWrapper.execute(any())).willReturn(responseObject);


        // When
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<OBReadConsentResponse1> response = controller.createAccountAccessConsents(obReadConsent,
                rsConfiguration.getFinancialId(), AUTHORIZATION, null, null, null, null, request, principal);

        // Then
        // No exceptions then all good!
    }

    @Test
    @Ignore
    public void changePathToCustomerInfoEndpoint() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getServletPath()).willReturn("openbanking/api/3.1.8/account-access-consents");

        // When
        String path = controller.changePathToCustomerInfoEndpoint(request);

        // Then
        assertThat(path.contains("customer-info-consent")).isTrue();
    }
}