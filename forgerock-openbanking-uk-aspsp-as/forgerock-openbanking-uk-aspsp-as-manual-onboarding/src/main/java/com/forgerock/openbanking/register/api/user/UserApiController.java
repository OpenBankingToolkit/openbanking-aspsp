/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.register.api.user;

import com.forgerock.openbanking.am.gateway.AMAuthGateway;
import com.forgerock.openbanking.analytics.model.entries.SessionCounterType;
import com.forgerock.openbanking.analytics.services.SessionCountersKPIService;
import com.forgerock.openbanking.auth.model.AuthorisationResponse;
import com.forgerock.openbanking.auth.model.ExchangeCodeResponse;
import com.forgerock.openbanking.auth.services.CookieService;
import com.forgerock.openbanking.auth.services.SessionService;
import com.forgerock.openbanking.commons.services.store.data.GenerateFakeDataService;
import com.forgerock.openbanking.exceptions.OIDCException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.model.UserContext;
import com.forgerock.openbanking.model.oidc.AccessTokenResponse;
import com.forgerock.openbanking.oidc.services.OpenIdService;
import com.forgerock.openbanking.register.model.ManualRegUser;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.forgerock.openbanking.auth.services.CookieService.OIDC_ORIGIN_URI_CONTEXT_COOKIE_NAME;


@RestController
@Slf4j
public class UserApiController implements UserApi {
    @Autowired
    private GenerateFakeDataService generateFakeDataService;
    @Autowired
    private OpenIdService openIdService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private CookieService cookieService;
    @Value("${am.internal.oidc.endpoint.accesstoken}")
    public String amAccessTokenEndpoint;
    @Autowired
    private AMAuthGateway amGateway;
    @Value("${jwt-auth.redirect-uri}")
    public String redirectUri;
    @Autowired
    private SessionCountersKPIService sessionCountersKPIService;


    @Override
    public ResponseEntity<String> startAuthorisationCodeFlow(
            @RequestParam(value = "originUrl") String originUrl,
            HttpServletResponse response,
            Principal principal
    ) {
        String state = UUID.randomUUID().toString();
        String authorisationRequest = openIdService.generateAuthorisationRequest(state, redirectUri, Arrays.asList("openid", "obie", "eidas", "directoryID"), Arrays.asList("ob-directory"));
        cookieService.createCookie(response, OIDC_ORIGIN_URI_CONTEXT_COOKIE_NAME, originUrl);
        return ResponseEntity.ok(authorisationRequest);
    }

    @Override
    public ResponseEntity<ExchangeCodeResponse> login(
            @CookieValue(value = OIDC_ORIGIN_URI_CONTEXT_COOKIE_NAME) String originURL,
            @RequestBody AuthorisationResponse authorisationResponse,
            HttpServletResponse response,
            Principal principal
    ) throws OIDCException, ParseException, InvalidTokenException, CertificateEncodingException {
        //TODO handle exception better

        AccessTokenResponse accessTokenResponse = openIdService.exchangeCode(amGateway, redirectUri, amAccessTokenEndpoint, authorisationResponse.getCode());
        log.debug("Access token response received: {}", accessTokenResponse);

        try {
            UserContext userContext = openIdService.fromIdToken(accessTokenResponse.getIdToken());


            sessionCountersKPIService.incrementSessionCounter(SessionCounterType.MANUAL_ONBOARDING);
            String sessionContextJwt = sessionService.generateSessionContextJwt(userContext);
            cookieService.createSessionCookie(response, sessionContextJwt);
            return ResponseEntity.ok(ExchangeCodeResponse.builder().originalRequest(originURL).build());
        } catch (IOException | JOSEException e) {
            throw new OIDCException("Couldn't read ID token", e);
        }
    }


    @Override
    public ResponseEntity logout(
            HttpServletResponse response,
            Principal principal
    ) {
        UserContext userContext = (UserContext) ((Authentication) principal).getPrincipal();
        try {
            cookieService.deleteSessionCookie(response, sessionService.expiredSessionContext(userContext));
            return ResponseEntity.ok(true);
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't logout");
        }
    }

    @Override
    public ResponseEntity getUser(
            HttpServletResponse response,
            Authentication principal
    ) throws CertificateEncodingException {
        UserContext userContext = (UserContext) principal.getPrincipal();

        String username = principal.getName().toLowerCase();
        try {
            if (userContext.getSessionClaims().getStringClaim("name") != null) {
                username = userContext.getSessionClaims().getStringClaim("name");
            }
        } catch (ParseException e) {
            log.debug("This user doesn't have a claim name, using the default sub instead", e);
        }
        log.debug("username :" + username);

        try {
            cookieService.createSessionCookie(response, sessionService.generateSessionContextJwt(userContext));
            return ResponseEntity.ok(fromUserContext(userContext));
        } catch (JOSEException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't logout");
        }
    }

    @Override
    public ManualRegUser fromUserContext(UserContext userContext) {
        ManualRegUser user = new ManualRegUser();
        user.setId(userContext.getUsername().toLowerCase());
        user.setAuthorities(userContext.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        try {
            String directoryID = userContext.getSessionClaims().getStringClaim("directoryID");
            user.setDirectoryID(directoryID);
            if ("EIDAS".equals(directoryID)) {
                user.setAppId(userContext.getSessionClaims().getStringClaim("app_id"));
                user.setOrganisationId(userContext.getSessionClaims().getStringClaim("org_id"));
                user.setPsd2Roles(userContext.getSessionClaims().getStringClaim("psd2_roles"));
            }
        } catch (ParseException e) {
            log.error("Couldn't read claims from user context", e);
        }
        return user;
    }

    @Override
    public ResponseEntity register(@RequestParam(value = "username") String username,
                                   Principal principal) {
        generateFakeDataService.generateFakeData(username.toLowerCase());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity authenticate(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {
        try {
            AccessTokenResponse accessTokenResponse = openIdService.passwordGrantFlow(amGateway, amAccessTokenEndpoint, username, password);
            return ResponseEntity.ok(accessTokenResponse.access_token);
        } catch (OIDCException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Couldn't authenticate the user");
        }
    }
}
