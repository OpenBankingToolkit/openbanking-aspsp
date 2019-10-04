/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.integration.test.support;

import brave.Span;
import brave.propagation.TraceContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public final class Tracer {

    public static brave.Tracer mockTracer() {
        final brave.Tracer tracer = mock(brave.Tracer.class);
        final Span span = mock(Span.class);
        final TraceContext traceCxt = TraceContext.newBuilder()
                .spanId(1)
                .traceId(1)
                .build();

        given(tracer.currentSpan()).willReturn(span);
        given(tracer.nextSpan()).willReturn(span);
        given(span.name(any())).willReturn(span);
        given(span.context()).willReturn(traceCxt);
        return tracer;
    }
}
