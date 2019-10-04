/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.internationalpayments;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalConsent2Repository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import com.forgerock.openbanking.commons.services.openbanking.FundsAvailabilityService;
import org.springframework.stereotype.Controller;

@Controller("InternationalPaymentConsentsApiV3.1.2")
public class InternationalPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalpayments.InternationalPaymentConsentsApiController implements InternationalPaymentConsentsApi {
    public InternationalPaymentConsentsApiController(ConsentMetricService consentMetricService, InternationalConsent2Repository internationalConsentRepository, TppRepository tppRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        super(consentMetricService, internationalConsentRepository, tppRepository, fundsAvailabilityService, resourceLinkService);
    }
}
