/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.domesticstandingorders;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.DomesticStandingOrderConsent3Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("DomesticStandingOrderConsentsApiV3.1.2")
@Slf4j
public class DomesticStandingOrderConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorders.DomesticStandingOrderConsentsApiController implements DomesticStandingOrderConsentsApi {
    public DomesticStandingOrderConsentsApiController(ConsentMetricService consentMetricService, DomesticStandingOrderConsent3Repository domesticStandingOrderConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        super(consentMetricService, domesticStandingOrderConsentRepository, tppRepository, resourceLinkService);
    }
}
