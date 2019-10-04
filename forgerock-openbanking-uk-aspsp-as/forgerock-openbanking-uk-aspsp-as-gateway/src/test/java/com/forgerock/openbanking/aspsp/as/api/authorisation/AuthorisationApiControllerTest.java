/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.as.api.authorisation;

import com.forgerock.openbanking.am.services.AMGatewayService;
import com.forgerock.openbanking.aspsp.as.api.authorisation.redirect.AuthorisationApiController;
import com.forgerock.openbanking.aspsp.as.service.headless.authorisation.HeadLessAuthorisationService;
import com.forgerock.openbanking.commons.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static com.forgerock.openbanking.aspsp.as.api.authorisation.JwtTestHelper.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class AuthorisationApiControllerTest {

    @Mock
    private HeadLessAuthorisationService headLessAuthorisationService;
    @Mock
    private TppStoreService tppStoreService;
    @Mock
    private CryptoApiClient cryptoApiClient;
    @Mock
    private AMGatewayService amGatewayService;
    @InjectMocks
    private AuthorisationApiController authorisationApiController;

    @Test
    public void shouldNotThrowExceptionWhenContainAllScopes() throws OBErrorException, OBErrorResponseException {
        // Given
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(null)).willReturn(Optional.of(tpp));

        // When
        authorisationApiController.getAuthorisation(null, null, null, null, "openid accounts payments", null, jwt, true, null, null, null, null, null);

        // Then no exception
    }

    @Test
    public void shouldNotThrowExceptionContainAllScopesAnyOrder() throws OBErrorException, OBErrorResponseException {
        // Given
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(null)).willReturn(Optional.of(tpp));

        // When
        authorisationApiController.getAuthorisation(null, null, null, null, "payments openid accounts", null, jwt, true, null, null, null, null, null);

        // Then no exception
    }

    @Test
    public void shouldNotThrowExceptionWhenNoUserInfo() throws OBErrorException, OBErrorResponseException {
        // Given
        String jwt = toEncodedSignedTestJwt("jwt/authorisation-no-user-info.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(null)).willReturn(Optional.of(tpp));

        // When
        authorisationApiController.getAuthorisation(null, null, null, null, "payments openid accounts", null, jwt, true, null, null, null, null, null);

        // Then no exception
    }

    @Test(expected = OBErrorException.class)
    public void shouldThrowExceptionWhenJwtScopesDoNotMatchQueryParamScope() throws OBErrorException, OBErrorResponseException {
        // Given
        String jwt = toEncodedSignedTestJwt("jwt/authorisation.jwt");
        Tpp tpp = new Tpp();
        OIDCRegistrationResponse registrationResponse = new OIDCRegistrationResponse();
        registrationResponse.setJwks_uri("");
        tpp.setRegistrationResponse(registrationResponse);
        given(tppStoreService.findByClientId(null)).willReturn(Optional.of(tpp));

        // When
        authorisationApiController.getAuthorisation(null, null, null, null, "openid accounts", null, jwt, true, null, null, null, null, null);

        // Then exception
    }

    @Test
    public void shouldConvertQueryToFragment() {
        // Given
        URI uri = UriComponentsBuilder.fromHttpUrl("https://google.com?test=toto").build().toUri();

        // When
        ResponseEntity responseEntity = authorisationApiController.convertQueryToFragment(uri, new HttpHeaders(), "state1");

        // Then
        assertThat(responseEntity.getHeaders().getLocation().toString()).isEqualTo("https://google.com#test=toto&state=state1");
    }

    private static String toEncodedSignedTestJwt(final String jwtPayloadFilePath) {
        return utf8FileToString
                .andThen(jsonStringToClaimsSet)
                .andThen(claimsSetToRsa256Jwt)
                .andThen(signJwt)
                .andThen(serializeJwt)
                .apply(jwtPayloadFilePath);
    }

}
