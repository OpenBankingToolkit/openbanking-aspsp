/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.internationalscheduledpayments;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalScheduledConsent2Repository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import com.forgerock.openbanking.commons.services.openbanking.FundsAvailabilityService;
import org.springframework.stereotype.Controller;

@Controller("InternationalScheduledPaymentConsentsApiV3.1.2")
public class InternationalScheduledPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalscheduledpayments.InternationalScheduledPaymentConsentsApiController implements InternationalScheduledPaymentConsentsApi {
    public InternationalScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, InternationalScheduledConsent2Repository internationalScheduledConsentRepository, TppRepository tppRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        super(consentMetricService, internationalScheduledConsentRepository, tppRepository, fundsAvailabilityService, resourceLinkService);
    }
}
