/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.domesticpayments;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticConsent2Repository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import com.forgerock.openbanking.commons.services.openbanking.FundsAvailabilityService;
import org.springframework.stereotype.Controller;

@Controller("DomesticPaymentConsentsApiV3.1.2")
public class DomesticPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticpayments.DomesticPaymentConsentsApiController implements DomesticPaymentConsentsApi {

    public DomesticPaymentConsentsApiController(DomesticConsent2Repository domesticConsentRepository, TppRepository tppRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService, ConsentMetricService consentMetricService) {
        super(domesticConsentRepository, tppRepository, fundsAvailabilityService, resourceLinkService, consentMetricService);
    }
}
