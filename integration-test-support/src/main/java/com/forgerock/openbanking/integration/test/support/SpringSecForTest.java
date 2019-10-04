/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.integration.test.support;

import com.forgerock.openbanking.authentication.configurers.MultiAuthenticationCollectorConfigurer;
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