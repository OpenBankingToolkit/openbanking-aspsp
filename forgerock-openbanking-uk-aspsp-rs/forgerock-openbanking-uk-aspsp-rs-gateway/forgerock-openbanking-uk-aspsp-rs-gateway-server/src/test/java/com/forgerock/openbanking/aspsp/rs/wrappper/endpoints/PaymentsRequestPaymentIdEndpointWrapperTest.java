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
import com.forgerock.openbanking.common.model.openbanking.forgerock.ConsentStatusCode;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_0.payment.FRDomesticConsent1;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.error.ErrorCode;
import dev.openbanking4.spring.security.multiauth.model.authentication.PasswordLessUserNameAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentsRequestPaymentIdEndpointWrapperTest {
    private static PaymentsRequestPaymentIdEndpointWrapper wrapper;

    @MockBean(name = "cryptoApiClient")
    private CryptoApiClient cryptoApiClient;

    @MockBean(name = "amOpenBankingConfiguration")
    private AMOpenBankingConfiguration amOpenBankingConfiguration;

    @MockBean(name = "amResourceServerService")
    private RSEndpointWrapperService rsEndpointWrapperService;

    @Before
    public void setup() {
        // setting required objects to the perform test
        ReflectionTestUtils.setField(amOpenBankingConfiguration, "audiences", Arrays.asList("https://am.dev-ob.forgerock.financial:443/oauth2/auth"));
        AMResourceServerService amService = new AMResourceServerService();
        ReflectionTestUtils.setField(amService, "cryptoApiClient", cryptoApiClient);
        ReflectionTestUtils.setField(amService, "amOpenBankingConfiguration", amOpenBankingConfiguration);
        ReflectionTestUtils.setField(rsEndpointWrapperService, "amResourceServerService", amService);

        wrapper = new PaymentsRequestPaymentIdEndpointWrapper(rsEndpointWrapperService) {
            @Override
            protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
                return super.run(main);
            }
        };
        wrapper.principal(new PasswordLessUserNameAuthentication("test-tpp", Collections.EMPTY_LIST));
    }

    @Test
    public void verifyAccessToken() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        wrapper.authorization("Bearer " + jws);
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

        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        wrapper.authorization("Bearer " + jws);

        // then
        assertThatCode(() -> {
            wrapper.payment(payment).applyFilters();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccessUsing_GrantTypeWrong() throws Exception {
        // given
        FRPaymentConsent payment = FRDomesticConsent1.builder()
                .status(ConsentStatusCode.AUTHORISED)
                .build();

        String jws = jws("payments", OIDCConstants.GrantType.AUTHORIZATION_CODE);
        wrapper.authorization("Bearer " + jws);

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
