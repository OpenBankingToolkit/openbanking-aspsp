/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.domesticscheduledpayments;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledConsent2Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import org.springframework.stereotype.Controller;

@Controller("DomesticScheduledPaymentConsentsApiV3.1.2")
public class DomesticScheduledPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticscheduledpayments.DomesticScheduledPaymentConsentsApiController implements DomesticScheduledPaymentConsentsApi {
    public DomesticScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, DomesticScheduledConsent2Repository domesticScheduledConsentRepository, TppRepository tppRepository, ResourceLinkService resourceLinkService) {
        super(consentMetricService, domesticScheduledConsentRepository, tppRepository, resourceLinkService);
    }
}
