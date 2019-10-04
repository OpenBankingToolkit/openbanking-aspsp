/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.domesticpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.DomesticPaymentSubmission2Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import org.springframework.stereotype.Controller;

@Controller("DomesticPaymentsApiV3.1.1")
public class DomesticPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.domesticpayments.DomesticPaymentsApiController implements DomesticPaymentsApi {

    public DomesticPaymentsApiController(DomesticConsent2Repository domesticConsentRepository, DomesticPaymentSubmission2Repository domesticPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(domesticConsentRepository, domesticPaymentSubmissionRepository, resourceLinkService);
    }
}
