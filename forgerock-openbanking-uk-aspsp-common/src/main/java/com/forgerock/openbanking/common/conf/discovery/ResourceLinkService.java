/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.conf.discovery;


import com.forgerock.openbanking.common.model.openbanking.event.FREventSubscription1;
import com.forgerock.openbanking.common.model.openbanking.forgerock.FRPaymentConsent;
import com.forgerock.openbanking.common.model.openbanking.v3_0.event.FRCallbackUrl1;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmation1;
import com.forgerock.openbanking.common.model.openbanking.v3_0.funds.FRFundsConfirmationConsent1;
import com.forgerock.openbanking.common.model.openbanking.v3_1.payment.FRPaymentSubmission;
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

    public Links toSelfLink(FRPaymentConsent consent, Function<DiscoveryConfigurationProperties.PaymentApis, String> getUrl) {
        return resourceToLink(getUrl.apply(discoveryConfigurationProperties.getApis().getPayments()), consent.getId());
    }

    public Links toSelfLink(FRPaymentSubmission payment, Function<DiscoveryConfigurationProperties.PaymentApis, String> getUrl) {
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
