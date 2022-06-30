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
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.OBRisk1Validator;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.integration.test.support.SupportConstants;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.spring.security.multiauth.model.authentication.PasswordLessUserNameAuthentication;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.payment.OBWriteDomesticConsent2;

import java.util.Collections;

import static com.forgerock.openbanking.integration.test.support.JWT.jws;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PaymentsApiEndpointWrapperTest {

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

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void validatePaymentCodeContext_throwsException() throws OBErrorException {
        // given
        expectedEx.expect(OBErrorException.class);
        expectedEx.expectMessage("The 'OBRisk1.PaymentCodeContext' field must be set and be valid");
        OBRisk1Validator riskValidator = new OBRisk1Validator(true);
        OBWriteDomesticConsent2 consent = new OBWriteDomesticConsent2();

        // then
        getEndpointWrapper(riskValidator).validateRisk(consent.getRisk());
    }


    @Test
    public void validatePaymentCodeContext_no_validator() throws OBErrorException {
        // given
        expectedEx.expect(NullPointerException.class);
        expectedEx.expectMessage("validatePaymentCodeContext called but no validator present");
        OBWriteDomesticConsent2 consent = new OBWriteDomesticConsent2();

        // then
        getEndpointWrapper(null).validateRisk(consent.getRisk());
    }

    @Test
    public void verifyAccessToken_grantType_payment() throws Exception {
        // given
        String jws = jws(OpenBankingConstants.Scope.PAYMENTS, OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        when(amResourceServerService.verifyAccessToken(SupportConstants.BEARER_PREFIX + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));
        when(obHeaderCheckerService.verifyFinancialIdHeader(any())).thenReturn(true);

        // then
        assertThatCode(() -> {
            getEndpointWrapper(null)
                    .principal(new PasswordLessUserNameAuthentication(SupportConstants.USER_AUDIENCE, Collections.EMPTY_LIST))
                    .authorization(SupportConstants.BEARER_PREFIX + jws)
                    .applyFilters();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccessToken_grantType_fundsConfirmation() throws Exception {
        // given
        String jws = jws(OpenBankingConstants.Scope.PAYMENTS, OIDCConstants.GrantType.AUTHORIZATION_CODE);
        when(amResourceServerService.verifyAccessToken(SupportConstants.BEARER_PREFIX + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));
        when(obHeaderCheckerService.verifyFinancialIdHeader(any())).thenReturn(true);

        // then
        assertThatCode(() -> {
            getEndpointWrapper(null)
                    .principal(new PasswordLessUserNameAuthentication(SupportConstants.USER_AUDIENCE, Collections.EMPTY_LIST))
                    .authorization(SupportConstants.BEARER_PREFIX + jws)
                    .isFundsConfirmationRequest(true)
                    .applyFilters();
        }).doesNotThrowAnyException();
    }

    @Test
    public void verifyAccessToken_wrong_grantType() throws Exception {
        // given
        expectedEx.expect(OBErrorException.class);
        expectedEx.expectMessage("The access token grant type AUTHORIZATION_CODE doesn't match one of the expected grant types");
        String jws = jws(OpenBankingConstants.Scope.PAYMENTS, OIDCConstants.GrantType.AUTHORIZATION_CODE);
        when(amResourceServerService.verifyAccessToken(SupportConstants.BEARER_PREFIX + jws)).thenReturn((SignedJWT) JWTParser.parse(jws));
        when(obHeaderCheckerService.verifyFinancialIdHeader(any())).thenReturn(true);

        // then
        getEndpointWrapper(null)
                .principal(new PasswordLessUserNameAuthentication(SupportConstants.USER_AUDIENCE, Collections.EMPTY_LIST))
                .authorization(SupportConstants.BEARER_PREFIX + jws)
                .applyFilters();
    }

    private PaymentsApiEndpointWrapper getEndpointWrapper(OBRisk1Validator riskValidator) {

        RSEndpointWrapperService rsEndpointWrapperService = new RSEndpointWrapperService(
                obHeaderCheckerService,
                cryptoApiClient,
                null,
                null,
                rsConfiguration,
                null,
                null,
                false,
                null,
                rsConfiguration.financialId,
                amOpenBankingConfiguration,
                null,
                null,
                null,
                amResourceServerService,
                null,
                null,
                null,
                null, false);

        return new PaymentsApiEndpointWrapper(
                rsEndpointWrapperService,
                tppStoreService,
                null,
                null,
                null,
                riskValidator) {

            @Override
            protected ResponseEntity run(PaymentRestEndpointContent main) throws OBErrorException {
                return super.run(main);
            }

        };
    }
}
