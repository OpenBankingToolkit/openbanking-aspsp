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
/**
 * NOTE: This class is auto generated by the swagger code generator program (2.3.1).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_6.internationalstandingorders;

import com.forgerock.openbanking.analytics.services.ConsentMetricService;
import com.forgerock.openbanking.repositories.TppRepository;
import com.forgerock.openbanking.aspsp.rs.store.repository.payments.InternationalStandingOrderConsentRepository;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.conf.discovery.ResourceLinkService;
import org.springframework.stereotype.Controller;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment4;

@Controller("InternationalStandingOrderConsentsApiV3.1.6")
public class InternationalStandingOrderConsentsApiController extends com.forgerock.openbanking.aspsp.rs.store.api.openbanking.payment.v3_1_5.internationalstandingorders.InternationalStandingOrderConsentsApiController implements InternationalStandingOrderConsentsApi {

    public InternationalStandingOrderConsentsApiController(ConsentMetricService consentMetricService,
                                                           InternationalStandingOrderConsentRepository internationalStandingOrderConsentRepository,
                                                           TppRepository tppRepository,
                                                           ResourceLinkService resourceLinkService) {
        super(consentMetricService, internationalStandingOrderConsentRepository, tppRepository, resourceLinkService);
    }

    @Override
    protected OBDiscoveryAPILinksPayment4 getVersion(DiscoveryConfigurationProperties.PaymentApis discovery) {
        return discovery.getV_3_1_6();
    }
}
