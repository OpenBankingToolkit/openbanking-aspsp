/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalScheduledConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalScheduledPaymentSubmission2Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("InternationalScheduledPaymentsApiV3.1.1")
@Slf4j
public class InternationalScheduledPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalscheduledpayments.InternationalScheduledPaymentsApiController implements InternationalScheduledPaymentsApi {
    public InternationalScheduledPaymentsApiController(InternationalScheduledConsent2Repository internationalScheduledConsentRepository, InternationalScheduledPaymentSubmission2Repository internationalScheduledPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(internationalScheduledConsentRepository, internationalScheduledPaymentSubmissionRepository, resourceLinkService);
    }
}
