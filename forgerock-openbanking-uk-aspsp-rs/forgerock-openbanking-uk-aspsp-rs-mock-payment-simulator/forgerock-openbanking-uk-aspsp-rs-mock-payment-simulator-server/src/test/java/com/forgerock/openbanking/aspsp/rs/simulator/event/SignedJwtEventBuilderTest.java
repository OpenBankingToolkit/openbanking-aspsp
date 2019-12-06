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
package com.forgerock.openbanking.aspsp.rs.simulator.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.SignedJwtEventBuilder;
import com.forgerock.openbanking.common.conf.discovery.DiscoveryConfigurationProperties;
import com.forgerock.openbanking.common.model.notification.ResourceUpdateEvent;
import com.forgerock.openbanking.common.model.openbanking.forgerock.event.FREventNotification;
import com.forgerock.openbanking.common.services.notification.EventSubject;
import com.forgerock.openbanking.common.services.notification.EventType;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.forgerock.openbanking.model.Tpp;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.nimbusds.jwt.JWTClaimsSet;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment1;
import uk.org.openbanking.datamodel.discovery.OBDiscoveryAPILinksPayment3;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static com.forgerock.openbanking.constants.OpenBankingConstants.EventNotificationClaims.OB_SUBJECT_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SignedJwtEventBuilderTest {

    private static final String ISSUER_ID_VALUE = "5555";
    private static final String FAKE_SIGNED_JWT = "lijlkjlkjlkjlkjlkjlkjlkjlkjl";
    private static final String BASE_URL = "http://rs-server/open-banking";

    private static final String CLIENT_ID = "client123";
    private static final Tpp TPP = Tpp.builder().id("123").clientId(CLIENT_ID).build();


    private static final ObjectMapper objectMapper = new ObjectMapper();

    private SignedJwtEventBuilder signedJwtEventBuilder;

    // Captures JWT claims passed for signing so we can check them
    private ArgumentCaptor<JWTClaimsSet> claimsCaptor;

    @Before
    public void setUp() throws Exception {
        // Mock signing service
        claimsCaptor = ArgumentCaptor.forClass(JWTClaimsSet.class);
        CryptoApiClient cryptoApiClient = mock(CryptoApiClient.class);
        given(cryptoApiClient.signClaims(eq(ISSUER_ID_VALUE), claimsCaptor.capture(), anyBoolean())).willReturn(FAKE_SIGNED_JWT);


        signedJwtEventBuilder = new SignedJwtEventBuilder(ISSUER_ID_VALUE, cryptoApiClient, new ObjectMapper());
    }

    @Test
    public void buildClaimsForDomesticPayment_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = getEventSubject("PISC_123", "v3.1", "domestic-payment");

        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v3.1/domestic-payments", "domestic-payment", "v3.1");
    }

    @Test
    public void buildClaimsForDomesticScheduledPayment_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = getEventSubject("PISC_123", "v3.1", "domestic-scheduled-payment");

        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v3.1/domestic-scheduled-payments", "domestic-scheduled-payment", "v3.1");
    }

    @Test
    public void buildClaimsForDomesticStandingOrder_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = getEventSubject("PISC_123", "v3.1", "domestic-standing-order");

        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v3.1/domestic-standing-orders", "domestic-standing-order", "v3.1");
    }

    @Test
    public void buildClaimsForInternationalPayment_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = getEventSubject("PIC_123", "v3.1", "international-payment");

        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v3.1/international-payments", "international-payment", "v3.1");
    }

    @Test
    public void buildClaimsForInternationalScheduledPayment_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = getEventSubject("PISC_123", "v3.1", "international-scheduled-payment");

        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v3.1/international-scheduled-payments", "international-scheduled-payment", "v3.1");
    }

    private static EventSubject getEventSubject(String id, String version, String type) {
        return EventSubject.builder().id(id)
                .url(BASE_URL+String.format("/%s/%ss/%s", version, type, id))
                .type(type)
                .version(version)
                .build();
    }

    @Test
    public void buildClaimsForInternationalStandingOrder_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = getEventSubject("PISOC_123", "v3.1", "international-standing-order");

        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v3.1/international-standing-orders", "international-standing-order", "v3.1");
    }

    @Test
    public void buildClaimsForSinglePayment_validJwtClaimsBuilt() {
        // Given
        EventSubject subject = EventSubject.builder()
                .id("PR_123")
                .url(BASE_URL+"/v2.0/single-payments/PR_123")
                .type("single-payment")
                .version("v2.0")
                .build();
        // When
        FREventNotification eventNotification = signedJwtEventBuilder.build(TPP, subject, EventType.RESOURCE_UPDATE_EVENT);

        // Then
        assertThat(eventNotification.getSignedJwt()).isEqualTo(FAKE_SIGNED_JWT);
        areClaimsValidForConsentId(subject.getId(), "/v2.0/single-payments", "single-payment", "v2.0");
    }

    @Test
    public void buildClaims_checkNoSerializationErrors() {
        // when
        final EventSubject changedResource = new EventSubject(
                "123",
                "V3.1",
                "http://localhost/V3.1/domestic-payments/123",
                "domestic-payment");
        final ResourceUpdateEvent resourceUpdateEvent = ResourceUpdateEvent.builder()
                .subject(
                        ResourceUpdateEvent.Subject.builder()
                        .subjectType(OB_SUBJECT_TYPE)
                                .resourceId(changedResource.getId())
                                .subjectType(changedResource.getType())
                                .resourceLinks(
                                        Collections.singletonList(
                                            ResourceUpdateEvent.ResourceLink.builder()
                                            .version(changedResource.getVersion())
                                            .link(changedResource.getUrl())
                                            .build()
                                        )
                                )
                        .build()
                ).build();

        String expectedNotificationJwt = utf8FileToString.apply("expectedNotificationJwt.json");

        // Then
        JSONObject jwt = new JWTClaimsSet.Builder()
                .issuer(ISSUER_ID_VALUE)
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .subject("https://test")
                .audience("12345")
                .claim("txn", UUID.randomUUID().toString())
                .claim("toe", new Date())
                .claim("events", Collections.singletonMap(EventType.RESOURCE_UPDATE_EVENT.getEventName(), resourceUpdateEvent))
                .build()
                .toJSONObject();

        // Expect
        assertThat(jwt.get("sub")).isEqualTo("https://test");
        assertThat(jwt.get("iss")).isEqualTo(ISSUER_ID_VALUE);
    }


    private void areClaimsValidForConsentId(String consentId, String expectedResourcePath, String expectedRty, String expectedVersion) {
        final JWTClaimsSet jwtClaimsSet = claimsCaptor.getValue();
        assertThat(jwtClaimsSet.getIssuer()).isEqualTo(ISSUER_ID_VALUE);
        assertThat(jwtClaimsSet.getJWTID()).isNotNull();
        assertThat(jwtClaimsSet.getSubject()).isEqualTo(BASE_URL+expectedResourcePath+"/"+consentId);
        assertThat(jwtClaimsSet.getAudience().get(0)).isEqualTo(CLIENT_ID);

        String resourceUpdateEventStr = (String) ((Map)jwtClaimsSet.getClaim("events")).get(EventType.RESOURCE_UPDATE_EVENT.getEventName());

        try {
            ResourceUpdateEvent resourceUpdateEvent = objectMapper.readValue(resourceUpdateEventStr, ResourceUpdateEvent.class);
            assertThat(resourceUpdateEvent.getSubject().getSubjectType()).isEqualTo(OB_SUBJECT_TYPE);
            assertThat(resourceUpdateEvent.getSubject().getResourceId()).isEqualTo(consentId);
            assertThat(resourceUpdateEvent.getSubject().getResourceType()).isEqualTo(expectedRty);

            ResourceUpdateEvent.ResourceLink rlk = resourceUpdateEvent.getSubject().getResourceLinks().get(0);
            assertThat(rlk.getLink()).isEqualTo(BASE_URL + expectedResourcePath + "/" + consentId);
            assertThat(rlk.getVersion()).isEqualTo(expectedVersion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static final Function<String, String> utf8FileToString = fileName -> {
        try {
            return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    };

    private static DiscoveryConfigurationProperties setupDiscoveryConfiguration() {
        // Setup discover cfg
        OBDiscoveryAPILinksPayment1 v2Apis = new OBDiscoveryAPILinksPayment1();
        v2Apis.setGetPaymentSubmission(BASE_URL+"/v2.0/single-payments/{PaymentSubmissionId}");

        OBDiscoveryAPILinksPayment3 v3Apis = new OBDiscoveryAPILinksPayment3();
        v3Apis.setGetDomesticPayment(BASE_URL+"/V3.1/domestic-payments/{DomesticPaymentId}");
        v3Apis.setGetDomesticScheduledPayment(BASE_URL+"/V3.1/domestic-scheduled-payments/{DomesticScheduledPaymentId}");
        v3Apis.setGetDomesticStandingOrder(BASE_URL+"/V3.1/domestic-standing-orders/{DomesticStandingOrderId}");
        v3Apis.setGetInternationalPayment(BASE_URL+"/V3.1/international-payments/{InternationalPaymentId}");
        v3Apis.setGetInternationalScheduledPayment(BASE_URL+"/V3.1/international-scheduled-payments/{InternationalScheduledPaymentId}");
        v3Apis.setGetInternationalStandingOrder(BASE_URL+"/V3.1/international-standing-orders/{InternationalStandingOrderId}");

        DiscoveryConfigurationProperties.PaymentApis paymentApis = new DiscoveryConfigurationProperties.PaymentApis();
        paymentApis.setV_3_0(v3Apis);
        paymentApis.setV_2_0(v2Apis);

        final DiscoveryConfigurationProperties.Apis apis = new DiscoveryConfigurationProperties.Apis();
        apis.setPayments(paymentApis);

        DiscoveryConfigurationProperties cfg = new DiscoveryConfigurationProperties();
        cfg.setApis(apis);

        return cfg;
    }

}