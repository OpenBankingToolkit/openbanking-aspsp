/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticscheduledpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticScheduledPaymentSubmission2Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import org.springframework.stereotype.Controller;

@Controller("DomesticScheduledPaymentsApiV3.1.1")
public class DomesticScheduledPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticscheduledpayments.DomesticScheduledPaymentsApiController implements DomesticScheduledPaymentsApi {

    public DomesticScheduledPaymentsApiController(DomesticScheduledConsent2Repository domesticScheduledConsentRepository, DomesticScheduledPaymentSubmission2Repository domesticScheduledPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(domesticScheduledConsentRepository, domesticScheduledPaymentSubmissionRepository, resourceLinkService);
    }
}
