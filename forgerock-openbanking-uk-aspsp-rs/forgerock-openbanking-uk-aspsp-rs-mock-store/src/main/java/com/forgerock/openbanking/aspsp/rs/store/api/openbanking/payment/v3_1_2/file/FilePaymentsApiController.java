/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.file;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FileConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FilePaymentSubmission2Repository;
import com.forgerock.openbanking.commons.configuration.discovery.ResourceLinkService;
import com.forgerock.openbanking.commons.model.openbanking.forgerock.filepayment.v3_1.report.PaymentReportFile2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("FilePaymentsApiV3.1.2")
@Slf4j
public class FilePaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.file.FilePaymentsApiController implements FilePaymentsApi {

    public FilePaymentsApiController(FileConsent2Repository fileConsentRepository, FilePaymentSubmission2Repository filePaymentSubmissionRepository, PaymentReportFile2Service paymentReportFile1Service, ResourceLinkService resourceLinkService) {
        super(fileConsentRepository, filePaymentSubmissionRepository, paymentReportFile1Service, resourceLinkService);
    }
}
