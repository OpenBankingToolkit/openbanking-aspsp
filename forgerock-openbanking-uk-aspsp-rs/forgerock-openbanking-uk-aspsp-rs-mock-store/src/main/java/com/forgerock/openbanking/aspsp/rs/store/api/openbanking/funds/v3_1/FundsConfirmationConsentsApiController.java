/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_1;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_0.funds.FundsConfirmationConsentRepository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("FundsConfirmationConsentsApiV3.1")
@Slf4j
public class FundsConfirmationConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.funds.v3_0.FundsConfirmationConsentsApiController implements FundsConfirmationConsentsApi {

    public FundsConfirmationConsentsApiController(FundsConfirmationConsentRepository fundsConfirmationConsentRepository, TppRepository tppRepository, ConsentMetricService consentMetricService, ResourceLinkService resourceLinkService) {
        super(fundsConfirmationConsentRepository, tppRepository, consentMetricService, resourceLinkService);
    }
}
