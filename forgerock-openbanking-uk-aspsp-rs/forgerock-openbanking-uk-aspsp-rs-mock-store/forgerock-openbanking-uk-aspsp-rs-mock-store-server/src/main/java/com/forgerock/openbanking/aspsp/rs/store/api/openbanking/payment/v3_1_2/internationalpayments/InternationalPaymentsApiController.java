/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_2.internationalpayments;

import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalConsent2Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1.payments.InternationalPaymentSubmission2Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller("InternationalPaymentsApiV3.1.2")
@Slf4j
public class InternationalPaymentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalpayments.InternationalPaymentsApiController implements InternationalPaymentsApi {

    public InternationalPaymentsApiController(InternationalConsent2Repository internationalConsentRepository, InternationalPaymentSubmission2Repository internationalPaymentSubmissionRepository, ResourceLinkService resourceLinkService) {
        super(internationalConsentRepository, internationalPaymentSubmissionRepository, resourceLinkService);
    }
}
