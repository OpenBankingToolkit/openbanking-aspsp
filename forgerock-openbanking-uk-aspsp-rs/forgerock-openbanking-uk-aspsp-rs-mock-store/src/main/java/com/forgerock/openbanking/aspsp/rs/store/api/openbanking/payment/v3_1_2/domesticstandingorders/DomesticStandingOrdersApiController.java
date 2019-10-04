/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.domesticstandingorders;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.DomesticStandingOrderConsent3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.payments.DomesticStandingOrderPaymentSubmission3Repository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("DomesticStandingOrdersApiV3.1.2")
@Slf4j
public class DomesticStandingOrdersApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticstandingorders.DomesticStandingOrdersApiController implements DomesticStandingOrdersApi {

    public DomesticStandingOrdersApiController(DomesticStandingOrderConsent3Repository domesticStandingOrderConsentRepository, DomesticStandingOrderPaymentSubmission3Repository domesticStandingOrderPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(domesticStandingOrderConsentRepository, domesticStandingOrderPaymentSubmissionRepository, resourceLinkService);
    }
}
