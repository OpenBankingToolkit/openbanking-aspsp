/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1_1;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationRepository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import com.forgerock.openbanking.commons.services.openbanking.FundsAvailabilityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("FundsConfirmationsApiV3.1.1")
@Slf4j
public class FundsConfirmationsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1.FundsConfirmationsApiController implements FundsConfirmationsApi {

    public FundsConfirmationsApiController(FundsConfirmationRepository fundsConfirmationRepository, FundsConfirmationConsentRepository fundsConfirmationConsentRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        super(fundsConfirmationRepository, fundsConfirmationConsentRepository, fundsAvailabilityService, resourceLinkService);
    }
}
