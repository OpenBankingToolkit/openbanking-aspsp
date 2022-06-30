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

import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.FRDomesticConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.payment.PaymentConsent;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.ErrorCode;
import com.forgerock.spring.security.multiauth.model.authentication.PasswordLessUserNameAuthentication;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PaymentsRequestPaymentIdEndpointWrapperTest {

    private static PaymentsRequestPaymentIdEndpointWrapper wrapper;

    @Mock(name = "cryptoApiClient")
    private CryptoApiClient cryptoApiClient;

    @Mock(name = "amOpenBankingConfiguration")
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    @Mock(name = "amResourceServerService")
    private AMResourceServerService amResourceServerService;

    @Mock(name = "rsConfiguration")
    RSConfiguration rsConfiguration;

    @Mock(name = "obHeaderCheckerService")
    OBHeaderCheckerService obHeaderCheckerService;

    @Mock(name = "tppStoreService")
    TppStoreService tppStoreService;

    @Before
    public void setup() {
        // setting required objects to the perform test
        UUID uuid = UUID.randomUUID();

        RSEndpointWrapperService rsEndpointWrapperService = new RSEndpointWrapperService(obHeaderCheckerService, cryptoApiClient,
                null, null, rsConfiguration, null,
                null, false, null, rsConfiguration.financialId, amOpenBankingConfiguration, null,
                null, null, amResourceServerService, null, null, null, null, false);

        wrapper = new PaymentsRequestPaymentIdEndpointWrapper(rsEndpointWrapperService, tppStoreService) {
            @Override
            protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
                return super.run(main);
            }
        };

        wrapper.xFapiFinancialId(uuid.toString());
        wrapper.principal(new PasswordLessUserNameAuthentication("test-tpp", Collections.EMPTY_LIST));
    }

    @Test
    public void verifyAccessToken() throws Exception {
        // given
        PaymentConsent payment = FRDomesticConsent.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        wrapper.authorization("Bearer " + jws);
        when(amResourceServerService.verifyAccessToken("Bearer " + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));

        // then
        assertThatCode(() -> {
            wrapper.payment(payment).verifyAccessToken(Arrays.asList("payments"
            ), Arrays.asList(
                    OIDCConstants.GrantType.CLIENT_CREDENTIAL
            ));
        }).doesNotThrowAnyException();
    }


    @Test
    public void verifyAccessUsing_GrantTypeOK() throws Exception {

        PaymentConsent payment = FRDomesticConsent.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        wrapper.authorization("Bearer " + jws);
        when(amResourceServerService.verifyAccessToken("Bearer " + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));

        // then
        assertThatCode(() -> {
            wrapper.payment(payment).applyFilters();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccessUsing_GrantTypeWrong() throws Exception {
        // given
        PaymentConsent payment = FRDomesticConsent.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        wrapper.authorization("Bearer " + jws);
        when(amResourceServerService.verifyAccessToken("Bearer " + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));

        // then
        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).applyFilters(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(403);
        assertThat(obErrorException.getOBError().getErrorCode()).isEqualTo(ErrorCode.OBRI_ACCESS_TOKEN_INVALID.getValue());
        assertThat(obErrorException.getMessage()).isEqualTo("The access token grant type AUTHORIZATION_CODE doesn't match one of the expected grant types [CLIENT_CREDENTIAL]");
    }
}
