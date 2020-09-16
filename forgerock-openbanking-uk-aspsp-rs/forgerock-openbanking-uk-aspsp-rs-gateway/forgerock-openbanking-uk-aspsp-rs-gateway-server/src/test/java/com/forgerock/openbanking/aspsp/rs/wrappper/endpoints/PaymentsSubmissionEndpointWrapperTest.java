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
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_1_5.payment.FRDomesticConsent5;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.ErrorCode;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import dev.openbanking4.spring.security.multiauth.model.authentication.PasswordLessUserNameAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PaymentsSubmissionEndpointWrapperTest {

    private static PaymentsSubmissionsEndpointWrapper wrapper;

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

    @Before
    public void setup() {
        // setting required objects to the perform test
        UUID uuid = UUID.randomUUID();
        // create required object to initialise the wrapper properly
        RSEndpointWrapperService rsEndpointWrapperService = new RSEndpointWrapperService(obHeaderCheckerService, cryptoApiClient,
                null, null, rsConfiguration, null, null, false,
                null, rsConfiguration.financialId, amOpenBankingConfiguration, null,
                null, null, amResourceServerService, null,
                null, null, null);

        wrapper = new PaymentsSubmissionsEndpointWrapper(rsEndpointWrapperService) {
            @Override
            protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
                return super.run(main);
            }
        };

        wrapper.principal(new PasswordLessUserNameAuthentication("test-tpp", Collections.EMPTY_LIST));
        wrapper.xFapiFinancialId(uuid.toString());

        // generic mock handled stubb
        when(obHeaderCheckerService.verifyFinancialIdHeader(uuid.toString())).thenReturn(true);
    }

    @Test
    public void verifyAccessUsing_GrantTypeOK() throws Exception {

        FRPaymentConsent payment = FRDomesticConsent5.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        wrapper.authorization("Bearer " + jws);
        // mock handled stubbed
        when(amResourceServerService.verifyAccessToken("Bearer " + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));

        // then
        assertThatCode(() -> {
            wrapper.payment(payment).applyFilters();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccessUsing_GrantTypeWrong() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent5.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();
        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        wrapper.authorization("Bearer " + jws);
        // mock handled stubbed
        when(amResourceServerService.verifyAccessToken("Bearer " + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));
        // then
        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).applyFilters(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(403);
        assertThat(obErrorException.getOBError().getErrorCode()).isEqualTo(ErrorCode.OBRI_ACCESS_TOKEN_INVALID.getValue());
        assertThat(obErrorException.getMessage()).isEqualTo("The access token grant type CLIENT_CREDENTIAL doesn't match one of the expected grant types [AUTHORIZATION_CODE, HEADLESS_AUTH]");
    }

    @Test
    public void verifyPaymentStatus_complete() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent5.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();
        // then
        assertThatCode(() -> {
            wrapper.payment(payment).verifyPaymentStatus();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyPaymentStatus_notConsented() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent5.builder()
                .status(ConsentStatusCode.ACCEPTEDTECHNICALVALIDATION)
                .build();

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).verifyPaymentStatus(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(406);
        assertThat(obErrorException.getMessage()).isEqualTo("Payment invalid. Payment request hasn't been authorised by the PSU yet. Payment request status: 'ACCEPTEDTECHNICALVALIDATION'");
    }

    @Test
    public void verifyPaymentStatus_pending() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent5.builder()
                .status(ConsentStatusCode.PENDING)
                .build();

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).verifyPaymentStatus(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(406);
        assertThat(obErrorException.getMessage()).isEqualTo("Payment invalid. Payment still pending. Payment request status: 'PENDING'");
    }

    @Test
    public void verifyPaymentStatus_rejected() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent5.builder()
                .status(ConsentStatusCode.REJECTED)
                .build();

        // When
        OBErrorException obErrorException = catchThrowableOfType(
                () -> wrapper.payment(payment).verifyPaymentStatus(),
                OBErrorException.class
        );

        assertThat(obErrorException.getObriErrorType().getHttpStatus().value()).isEqualTo(401);
        assertThat(obErrorException.getMessage()).isEqualTo("Payment invalid. Payment has been rejected. Payment request status: 'REJECTED'");
    }
}
