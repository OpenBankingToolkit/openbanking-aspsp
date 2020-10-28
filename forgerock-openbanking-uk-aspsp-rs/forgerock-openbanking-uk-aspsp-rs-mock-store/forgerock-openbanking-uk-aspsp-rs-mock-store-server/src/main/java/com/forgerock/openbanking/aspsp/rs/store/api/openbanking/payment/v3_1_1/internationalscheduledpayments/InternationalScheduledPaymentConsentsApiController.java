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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_1.internationalscheduledpayments;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_5.payments.InternationalScheduledConsent5Repository;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import com.forgerock.openbanking.common.services.openbanking.FundsAvailabilityService;
import org.springframework.stereotype.Controller;

@Controller("InternationalScheduledPaymentConsentsApiV3.1.1")
public class InternationalScheduledPaymentConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1.internationalscheduledpayments.InternationalScheduledPaymentConsentsApiController implements InternationalScheduledPaymentConsentsApi {
    public InternationalScheduledPaymentConsentsApiController(ConsentMetricService consentMetricService, InternationalScheduledConsent5Repository internationalScheduledConsentRepository, TppRepository tppRepository, FundsAvailabilityService fundsAvailabilityService, ResourceLinkService resourceLinkService) {
        super(consentMetricService, internationalScheduledConsentRepository, tppRepository, fundsAvailabilityService, resourceLinkService);
    }
}
