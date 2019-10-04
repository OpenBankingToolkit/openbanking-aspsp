/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalPaymentSubmission2Repository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("InternationalPaymentsApiV3.1.1")
@Slf4j
public class InternationalPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalpayments.InternationalPaymentsApiController implements InternationalPaymentsApi {

    public InternationalPaymentsApiController(InternationalConsent2Repository internationalConsentRepository, InternationalPaymentSubmission2Repository internationalPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(internationalConsentRepository, internationalPaymentSubmissionRepository, resourceLinkService);
    }
}
