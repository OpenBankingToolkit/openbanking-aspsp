/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.service;

import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.model.openbanking.IntentType;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Helper to change a payment type into a generic event subject to pass to the event notification services
 */
@Component
public class PaymentToResourceConverter {

    private final DiscoveryConfigurationProperties discoveryConfig;

    @Autowired
    public PaymentToResourceConverter(DiscoveryConfigurationProperties discoveryConfig) {
        this.discoveryConfig = discoveryConfig;
    }

    EventSubject toResource(String resourceId, String apiVersion) {
        final IntentType intentType = Optional.ofNullable(IntentType.identify(resourceId))
                .orElseThrow(() -> new IllegalArgumentException("Unable to determine type of resource from id: "+resourceId));

        switch (intentType) {
            case PAYMENT_SINGLE_REQUEST:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_2_0().getGetPaymentSubmission().replace("{PaymentSubmissionId}", resourceId),
                        "single-payment");
            case PAYMENT_DOMESTIC_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetDomesticPayment()
                                .replace("{DomesticPaymentId}", resourceId),
                        "domestic-payment");
            case PAYMENT_DOMESTIC_SCHEDULED_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetDomesticScheduledPayment()
                                .replace("{DomesticScheduledPaymentId}", resourceId),
                        "domestic-scheduled-payment");
            case PAYMENT_DOMESTIC_STANDING_ORDERS_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetDomesticStandingOrder()
                                .replace("{DomesticStandingOrderId}", resourceId),
                        "domestic-standing-order");
            case PAYMENT_INTERNATIONAL_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetInternationalPayment()
                                .replace("{InternationalPaymentId}", resourceId),
                        "international-payment");
            case PAYMENT_INTERNATIONAL_SCHEDULED_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetInternationalScheduledPayment()
                                .replace("{InternationalScheduledPaymentId}", resourceId),
                        "international-scheduled-payment");
            case PAYMENT_INTERNATIONAL_STANDING_ORDERS_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetInternationalStandingOrder()
                                .replace("{InternationalStandingOrderId}", resourceId),
                        "international-standing-order");
            case PAYMENT_FILE_CONSENT:
                return new EventSubject(
                        resourceId,
                        apiVersion,
                        discoveryConfig.getApis().getPayments().getV_3_1().getGetFilePayment()
                                .replace("{FilePaymentId}", resourceId),
                        "file-payment");
            default:
                throw new UnsupportedOperationException("Not yet implemented for "+intentType);
        }
    }

}
