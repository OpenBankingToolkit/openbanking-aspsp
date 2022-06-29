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
package com.forgerock.openbanking.aspsp.rs.api.payment.v3_1_10.vrp;

import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.DomesticVrpPaymentsEndpointWrapper;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.services.store.RsStoreGateway;
import com.forgerock.openbanking.common.services.store.vrp.DomesticVrpPaymentConsentService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.github.jsonzou.jmockdata.JMockData;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPResponse;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPRequestTestDataFactory3_1_10;
import uk.org.openbanking.testsupport.vrp.OBDomesticVRPResponseTestDataFactory3_1_10;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static uk.org.openbanking.testsupport.payment.OBRisk1TestDataFactory.aValidOBRisk1_3_1_10;

/**
 * Integration test for {@link DomesticVrpConsentsApiController}
 */
@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class DomesticVrpApiControllerTest {

    private static final String HOST = "https://rs-api:";
    private static final String VRP_CONTXT_PATH = "/open-banking/v3.1.10/pisp/domestic-vrps";
    private static final String PORT = "8080";


    @Mock
    private RSEndpointWrapperService rsEndpointWrapperService;
    @Mock
    private RsStoreGateway rsStoreGateway;

    @Mock
    private DomesticVrpPaymentConsentService vrpConsentService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Principal principal;

    @Mock
    private DomesticVrpPaymentsEndpointWrapper domesticVrpPaymentsEndpointWrapper;

    private String ISSUER_ID = "issuerId";
    private String FINANCIAL_ID = "FinancialId";
    private String JWKS_URI = "jwks_uri";
    private boolean CUSTOMER_INFO_ENABLE = false;

    private RSConfiguration rsConfiguration;

    private DomesticVrpsApiController domesticVrpController;

    @Before
    public void setUp()
    {
         rsConfiguration = new RSConfiguration(ISSUER_ID, FINANCIAL_ID, JWKS_URI,CUSTOMER_INFO_ENABLE);
        domesticVrpController = new DomesticVrpsApiController(rsEndpointWrapperService, rsStoreGateway
                , vrpConsentService);
        given(rsEndpointWrapperService.vrpPaymentEndpoint()).willReturn(domesticVrpPaymentsEndpointWrapper);

        given(rsEndpointWrapperService.getRsConfiguration()).willReturn(rsConfiguration);
        given(domesticVrpPaymentsEndpointWrapper.authorization(any())).willReturn(domesticVrpPaymentsEndpointWrapper);
        given(domesticVrpPaymentsEndpointWrapper.xFapiFinancialId(any())).willReturn(domesticVrpPaymentsEndpointWrapper);
        given(domesticVrpPaymentsEndpointWrapper.principal(any())).willReturn(domesticVrpPaymentsEndpointWrapper);
        given(domesticVrpPaymentsEndpointWrapper.filters(any())).willReturn(domesticVrpPaymentsEndpointWrapper);
    }

    @Test
    public void createVrpPaymentConsent() throws Exception {
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);

        OBDomesticVRPRequest obDomesticVRPRequest = OBDomesticVRPRequestTestDataFactory3_1_10.aValidOBDomesticVRPRequest();
        OBDomesticVRPResponse domesticVRPResponse = aValidOBDomesticVRPResponse(obDomesticVRPRequest);
        ResponseEntity<OBDomesticVRPResponse> validResponse =
                new ResponseEntity(domesticVRPResponse, HttpStatus.CREATED);
        given(domesticVrpPaymentsEndpointWrapper.execute(any())).willReturn(validResponse);
        // When
        ResponseEntity<OBDomesticVRPResponse> response = domesticVrpController.domesticVrpPost("test", "idempotencyKey1", "test_sig",
                obDomesticVRPRequest,  new DateTime().toString(),
                "127..0.0.1", "x-fapi-interaction_id", "user-agent", null, request, principal);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private static FRDomesticVRPConsent aValidFRDomesticVRPConsent(){
        return JMockData.mock(FRDomesticVRPConsent.class);
    }

    private OBDomesticVRPResponse aValidOBDomesticVRPResponse(OBDomesticVRPRequest request) {
        return (new OBDomesticVRPResponse())
                .data(OBDomesticVRPResponseTestDataFactory3_1_10.aValidOBDomesticVRPResponseData(request))
                .risk(aValidOBRisk1_3_1_10())
                .links(new Links().self(HOST + PORT + VRP_CONTXT_PATH));
    }
}
