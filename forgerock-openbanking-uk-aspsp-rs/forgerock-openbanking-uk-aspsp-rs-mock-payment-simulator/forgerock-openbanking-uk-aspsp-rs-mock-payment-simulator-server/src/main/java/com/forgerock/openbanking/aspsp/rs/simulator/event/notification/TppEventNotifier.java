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
package com.forgerock.openbanking.aspsp.rs.simulator.event.notification;

import brave.Span;
import brave.Tracer;
import brave.propagation.ExtraFieldPropagation;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import uk.org.openbanking.OBHeaders;

import java.util.UUID;

/**
 * Sends a notification to a TPP
 */
@Service
@Slf4j
public class TppEventNotifier {


    // These values can be config values in the future but maybe not necessary right now.
    /**
     * Maximum wait period for retries - no matter how many retries are set, we will never wait longer than this period between retries.
     */
    private static final int MAX_INTERVAL_MS = 30000; // 30 sec

    /**
     * Total number of retries before giving up
     */
    private static final int MAX_RETRIES = 3;

    /**
     * This is the initial interval between retries, however it will increase exponentially (multiplier of 2) with each retry until MAX_RETRIES or MAX_WAIT reached
     */
    private static final int INITIAL_RETRY_INTERVAL_MS = 1000; // 1 sec

    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate;
    private final Tracer tracer;

    @Autowired
    public TppEventNotifier(
            RestTemplate restTemplate,
            Tracer tracer
            ) {
        this.restTemplate = restTemplate;
        this.retryTemplate = getRetryTemplate();
        this.tracer = tracer;
    }

    public void sendNotification(String url, String body) {
        Preconditions.checkArgument(!StringUtils.isEmpty(url), "Empty or null URL");

        final String xFapiInteractionId = UUID.randomUUID().toString();
        final Span span = setupSpanWithInteractionId(xFapiInteractionId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(OBHeaders.X_FAPI_INTERACTION_ID, xFapiInteractionId);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/jwt");
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);

        try (Tracer.SpanInScope spanInScope = tracer.withSpanInScope(span.start())){
            log.debug("Sending POST {} , httpEntity: {}", url, httpEntity);
            retryTemplate.execute((RetryCallback<String, HttpStatusCodeException>) context ->
                    restTemplate.postForObject(url, httpEntity, String.class)
            );
            log.info("Sent event notification to callback URL: '{}' with interactionId: {}", url, xFapiInteractionId);
        } catch (HttpStatusCodeException e) {
            log.error("Failed to POST request to URL: {} with response: {} : {} . httpEntity: {}", url, e.getStatusCode(), e.getResponseBodyAsString(), httpEntity, e);
            throw e;
        } finally {
            span.finish();
        }
    }

    private Span setupSpanWithInteractionId(final String xFapiInteractionId) {
        // Generate a new interaction id for the outgoing request (that may be triggered by batch jobs)
        log.debug("InteractionID:{}", xFapiInteractionId);
        final Span span = tracer
                .nextSpan() // Returns a new child span if there's one already or else a new trace.
                .name(xFapiInteractionId)
                .start();
        span.tag(OBHeaders.X_FAPI_INTERACTION_ID, xFapiInteractionId);
        ExtraFieldPropagation.set(span.context(), OBHeaders.X_FAPI_INTERACTION_ID, xFapiInteractionId);
        return span;
    }

    // Not using the common spring bean as it is for multiple internal services and this is configured specifically to this common class.
    private RetryTemplate getRetryTemplate() {
        // See https://openbanking.atlassian.net/wiki/spaces/DZ/pages/645367055/Event+Notification+API+Specification+-+v3.0#EventNotificationAPISpecification-v3.0-EventNotificationRetryPolicy
        final RetryTemplate template = new RetryTemplate();

        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_RETRY_INTERVAL_MS); // Default 100ms
        backOffPolicy.setMaxInterval(MAX_INTERVAL_MS);
        template.setBackOffPolicy(backOffPolicy);

        final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_RETRIES);
        template.setRetryPolicy(retryPolicy);

        return template;
    }

}
