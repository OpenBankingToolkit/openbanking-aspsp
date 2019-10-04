/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.service;

import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.github.jsonzou.jmockdata.JMockData;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentToResourceConverterTest {
    private static final String VERSION_2 = "2.0";
    private static final String VERSION_3_1 = "3.1";
    private static final String BASE_URL = "http://localhost";

    private DiscoveryConfigurationProperties discoveryConfig;
    private PaymentToResourceConverter paymentToResourceConverter;

    @Before
    public void setUp() throws Exception {
        discoveryConfig = JMockData.mock(DiscoveryConfigurationProperties.class);
        paymentToResourceConverter = new PaymentToResourceConverter(discoveryConfig);


    }

    @Test
    public void toResource_singlePayment() {
        // Given
        String id = "PR_123";
        discoveryConfig.getApis().getPayments().getV_2_0().setGetPaymentSubmission(BASE_URL+"/{PaymentSubmissionId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_2);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_2);
        assertThat(eventSubject.getType()).isEqualTo("single-payment");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }

    @Test
    public void toResource_domesticSinglePayment() {
        // Given
        String id = "PDC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetDomesticPayment(BASE_URL+"/{DomesticPaymentId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("domestic-payment");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }

    @Test
    public void toResource_domesticScheduledPayment() {
        // Given
        String id = "PDSC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetDomesticScheduledPayment(BASE_URL+"/{DomesticScheduledPaymentId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("domestic-scheduled-payment");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }


    @Test
    public void toResource_domesticStandingOrderPayment() {
        // Given
        String id = "PDSOC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetDomesticStandingOrder(BASE_URL+"/{DomesticStandingOrderId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("domestic-standing-order");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }

    @Test
    public void toResource_internationalSinglePayment() {
        // Given
        String id = "PIC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetInternationalPayment(BASE_URL+"/{InternationalPaymentId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("international-payment");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }

    @Test
    public void toResource_internationalScheduledPayment() {
        // Given
        String id = "PISC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetInternationalScheduledPayment(BASE_URL+"/{InternationalScheduledPaymentId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("international-scheduled-payment");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }


    @Test
    public void toResource_internationalStandingOrderPayment() {
        // Given
        String id = "PISOC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetInternationalStandingOrder(BASE_URL+"/{InternationalStandingOrderId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("international-standing-order");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }

    @Test
    public void toResource_filePayment() {
        // Given
        String id = "PFC_123";
        discoveryConfig.getApis().getPayments().getV_3_1().setGetFilePayment(BASE_URL+"/{FilePaymentId}");

        //When
        EventSubject eventSubject = paymentToResourceConverter.toResource(id, VERSION_3_1);

        // Then
        assertThat(eventSubject.getId()).isEqualTo(id);
        assertThat(eventSubject.getVersion()).isEqualTo(VERSION_3_1);
        assertThat(eventSubject.getType()).isEqualTo("file-payment");
        assertThat(eventSubject.getUrl()).isEqualTo(BASE_URL+"/"+id);
    }
}