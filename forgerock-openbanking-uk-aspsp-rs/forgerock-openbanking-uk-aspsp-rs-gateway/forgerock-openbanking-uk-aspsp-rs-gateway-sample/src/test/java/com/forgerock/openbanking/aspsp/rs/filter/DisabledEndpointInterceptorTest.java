/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.filter;

import com.forgerock.openbanking.common.conf.discovery.ControllerEndpointBlacklist;
import com.forgerock.openbanking.common.conf.discovery.RSAPIsConfigurationProperties;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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