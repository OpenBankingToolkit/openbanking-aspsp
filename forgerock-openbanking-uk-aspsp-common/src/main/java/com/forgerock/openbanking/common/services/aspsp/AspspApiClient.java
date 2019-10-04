/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.aspsp;

import com.forgerock.openbanking.model.oidc.OIDCRegistrationResponse;
import org.springframework.web.client.RestTemplate;

public interface AspspApiClient {

    String testMatls(RestTemplate restTemplate, String testMatlsEndpoint);

    OIDCRegistrationResponse getOnboardingResult(RestTemplate restTemplate, String onboardingEndpoint);

    OIDCRegistrationResponse onboard(RestTemplate restTemplate, String onboardingEndpoint, String registrationRequestJwtSerialised);

    void offBoard(RestTemplate restTemplate, String onboardingEndpoint);

}
