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
