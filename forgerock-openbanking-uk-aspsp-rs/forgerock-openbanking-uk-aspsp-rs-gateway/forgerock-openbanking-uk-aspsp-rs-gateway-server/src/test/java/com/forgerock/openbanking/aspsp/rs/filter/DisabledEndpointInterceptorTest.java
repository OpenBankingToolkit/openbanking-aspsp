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
package com.forgerock.openbanking.aspsp.rs.filter;

import com.forgerock.openbanking.common.conf.discovery.ControllerEndpointBlacklist;
import com.forgerock.openbanking.common.conf.discovery.RSAPIsConfigurationProperties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DisabledEndpointInterceptorTest {

    RSAPIsConfigurationProperties rsapiConfigurationProperties;
    ControllerEndpointBlacklist controllerEndpointBlacklist;
    DisabledEndpointInterceptor disabledEndpointInterceptor;
    HttpServletRequest request;
    HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        controllerEndpointBlacklist = new ControllerEndpointBlacklist();
        rsapiConfigurationProperties = mock(RSAPIsConfigurationProperties.class);
        given(rsapiConfigurationProperties.getControllerEndpointBlacklist()).willReturn(controllerEndpointBlacklist);
        disabledEndpointInterceptor = new DisabledEndpointInterceptor(rsapiConfigurationProperties);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    public void preHandle_noMethodMapping_ignore() {
        assertThat(disabledEndpointInterceptor.preHandle(request, response, "")).isTrue();
    }

    @Test
    public void preHandle_enabled_permit() throws Exception{
        HandlerMethod handlerMethod = new HandlerMethod(TestClass.class, TestClass.class.getMethod("foo"));

        assertThat(disabledEndpointInterceptor.preHandle(request, response, handlerMethod)).isTrue();
    }

    @Test
    public void preHandle_disabled_return404() throws Exception{
        controllerEndpointBlacklist.add(TestClass.class, TestClass.class.getMethod("foo"));
        HandlerMethod handlerMethod = new HandlerMethod(new TestClass(), TestClass.class.getMethod("foo"));

        assertThat(disabledEndpointInterceptor.preHandle(request, response, handlerMethod)).isFalse();
    }

    public static class TestClass {
        public void foo() {

        }
    }
}