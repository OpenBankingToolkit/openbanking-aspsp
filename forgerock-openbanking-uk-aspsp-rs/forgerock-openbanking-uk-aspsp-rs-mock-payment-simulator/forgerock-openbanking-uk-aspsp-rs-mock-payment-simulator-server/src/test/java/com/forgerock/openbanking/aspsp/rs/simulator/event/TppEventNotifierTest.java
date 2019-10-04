/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.simulator.event;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.forgerock.openbanking.aspsp.rs.simulator.event.notification.TppEventNotifier;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class TppEventNotifierTest {

    private static final String FINANCIAL_ID = "F123";

    private TppEventNotifier tppEventNotifier;
    private MockRestServiceServer mockRestServiceServer;

    @Before
    public void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        tppEventNotifier = new TppEventNotifier(restTemplate, setupMockTracer());
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void sendNotification_success() {
        //  Given
        final String body = UUID.randomUUID().toString();
        final String url = "http://unittest/111";
        mockRestServiceServer
                .expect(requestTo(url))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(body))
                .andRespond(withStatus(HttpStatus.ACCEPTED));

        // When
        tppEventNotifier.sendNotification(url, body);

        // Then
        mockRestServiceServer.verify();
    }

    @Test
    public void sendNotification_endpointNotAvailable_retryThreeTimes_thenFail() {
        //  Given
        final String body = UUID.randomUUID().toString();
        final String url = "http://unittest/111";
        mockRestServiceServer
                .expect(times(3), requestTo(url))  // Retry 3 times then fail
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string(body))
                .andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));


        // When
        final AbstractThrowableAssert<?, ? extends Throwable> expectedException = assertThatThrownBy(
                () -> tppEventNotifier.sendNotification(url, body));

        // Then
        mockRestServiceServer.verify();
        expectedException.isInstanceOf(HttpStatusCodeException.class);
        expectedException.hasFieldOrPropertyWithValue("statusCode", HttpStatus.SERVICE_UNAVAILABLE);
    }

    private Tracer setupMockTracer() {
        Tracer tracer = mock(Tracer.class);
        Span span = mock(Span.class);
        when(tracer.nextSpan()).thenReturn(span);
        when(span.name(any())).thenReturn(span);
        when(span.start()).thenReturn(span);
        TraceContext traceContext = TraceContext.newBuilder().spanId(1).traceId(2).build();
        when(span.context()).thenReturn(traceContext);
        return tracer;
    }
}