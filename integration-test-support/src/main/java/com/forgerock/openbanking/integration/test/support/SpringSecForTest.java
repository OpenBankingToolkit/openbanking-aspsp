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

import com.forgerock.spring.security.multiauth.configurers.MultiAuthenticationCollectorConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecForTest extends WebSecurityConfigurerAdapter {

    public MockAuthCollector mockAuthCollector = new MockAuthCollector();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .csrf().disable() // We don't need CSRF for JWT based authentication
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .apply(new MultiAuthenticationCollectorConfigurer<HttpSecurity>()
                        .collector(mockAuthCollector)
                )
        ;
    }
}