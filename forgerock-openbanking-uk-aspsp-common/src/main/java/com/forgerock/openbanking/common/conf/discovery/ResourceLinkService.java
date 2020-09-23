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
package com.forgerock.openbanking.common.conf.discovery;


import com.forgerock.openbanking.common.model.openbanking.event.FREventSubscription1;
import com.forgerock.openbanking.common.model.openbanking.forgerock.PaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_0.event.FRCallbackUrl1;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmation1;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.model.openbanking.forgerock.PaymentSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.org.openbanking.datamodel.account.Links;

import java.util.function.Function;

@Service
public class ResourceLinkService {
    private DiscoveryConfigurationProperties discoveryConfigurationProperties;

    @Autowired
    public ResourceLinkService(DiscoveryConfigurationProperties discoveryConfigurationProperties) {
        this.discoveryConfigurationProperties = discoveryConfigurationProperties;
    }

    public Links toSelfLink(PaymentConsent consent, Function<DiscoveryConfigurationProperties.PaymentApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getPayments()), consent.getId());
    }

    public Links toSelfLink(PaymentSubmission payment, Function<DiscoveryConfigurationProperties.PaymentApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getPayments()), payment.getId());
    }

    public Links toSelfLink(FRFundsConfirmationConsent1 fundsConfirmation, Function<DiscoveryConfigurationProperties.FundsConfirmationApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getFundsConfirmations()), fundsConfirmation.getId());
    }

    public Links toSelfLink(FRFundsConfirmation1 fundsConfirmation, Function<DiscoveryConfigurationProperties.FundsConfirmationApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getFundsConfirmations()), fundsConfirmation.getId());
    }

    public Links toSelfLink(FREventSubscription1 event, Function<DiscoveryConfigurationProperties.EventNotificationApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getEventNotifications()), event.getId());
    }

    public Links toSelfLink(Function<DiscoveryConfigurationProperties.EventNotificationApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getEventNotifications()));
    }

    public Links toSelfLink(FRCallbackUrl1 callback, Function<DiscoveryConfigurationProperties.EventNotificationApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getEventNotifications()), callback.getId());
    }

    private static Links resourceToLink(String url, String id) {
        if (url==null) return null;
        return new Links().self(url.replaceAll("\\{.*}", id));
    }

    private static Links resourceToLink(String url) {
        if (url==null) return null;
        return new Links().self(url);
    }

}
