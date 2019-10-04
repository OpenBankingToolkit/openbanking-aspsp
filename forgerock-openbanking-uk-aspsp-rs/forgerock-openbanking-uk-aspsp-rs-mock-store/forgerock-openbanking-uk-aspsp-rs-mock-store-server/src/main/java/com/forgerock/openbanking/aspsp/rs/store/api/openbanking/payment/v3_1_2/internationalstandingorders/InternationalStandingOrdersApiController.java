/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.internationalstandingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.InternationalStandingOrderConsent3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.InternationalStandingOrderPaymentSubmission3Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("InternationalStandingOrdersApiV3.1.2")
@Slf4j
public class InternationalStandingOrdersApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalstandingorders.InternationalStandingOrdersApiController implements InternationalStandingOrdersApi {

    public InternationalStandingOrdersApiController(InternationalStandingOrderConsent3Repository internationalStandingOrderConsentRepository, InternationalStandingOrderPaymentSubmission3Repository internationalStandingOrderPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(internationalStandingOrderConsentRepository, internationalStandingOrderPaymentSubmissionRepository, resourceLinkService);
    }
}
