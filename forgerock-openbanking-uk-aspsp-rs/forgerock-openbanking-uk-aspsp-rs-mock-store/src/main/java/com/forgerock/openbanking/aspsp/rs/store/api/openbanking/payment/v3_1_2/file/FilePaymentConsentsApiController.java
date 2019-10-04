/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.file;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.aspsp.rs.store.repository.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.FileConsent2Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("FilePaymentConsentsApiV3.1.2")
@Slf4j
public class FilePaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.file.FilePaymentConsentsApiController implements FilePaymentConsentsApi {

    public FilePaymentConsentsApiController(ConsentMetricService consentMetricService, TppRepository tppRepository, FileConsent2Repository fileConsentRepository, ResourceLinkService resourceLinkService) {
        super(consentMetricService, tppRepository, fileConsentRepository, resourceLinkService);
    }
}
